package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

public class FetchUserProfileUseCaseSyncTest {

    FetchUserProfileUseCaseSync SUT;
    UserProfileHttpEndpointSyncTD userProfileHttpEndpointSyncTD;
    UsersCacheTD usersCacheTD;

    public static final String userID = "userId";
    public static final String fullName = "fullName";
    public static final String imageUrl = "imageUrl";

    @Before
    public void setup() {
        userProfileHttpEndpointSyncTD = new UserProfileHttpEndpointSyncTD();
        usersCacheTD = new UsersCacheTD();
        SUT = new FetchUserProfileUseCaseSync(userProfileHttpEndpointSyncTD, usersCacheTD);
    }

    @Test
    public void fetchUserProfileSync_success_userIdPassedToEndPoint() {
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(userID);
        Assert.assertThat(userProfileHttpEndpointSyncTD._userId, is(userID));
    }

    @Test
    public void fetchUserProfileSync_success_UserCached() {
        FetchUserProfileUseCaseSync.UseCaseResult useCaseResult = SUT.fetchUserProfileSync(userID);
        User cachedUser = usersCacheTD.user;
        Assert.assertThat(useCaseResult, is(FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS));
        Assert.assertThat(cachedUser.getUserId(), is(userID));
        Assert.assertThat(cachedUser.getFullName(), is(fullName));
        Assert.assertThat(cachedUser.getImageUrl(), is(imageUrl));
    }

    @Test
    public void fetchUserProfileSync_failure_UserNotCachedGeneralError() {
        userProfileHttpEndpointSyncTD.isGeneralError = true;
        FetchUserProfileUseCaseSync.UseCaseResult useCaseResult = SUT.fetchUserProfileSync(userID);
        User cachedUser = usersCacheTD.user;
        Assert.assertThat(useCaseResult, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
        Assert.assertThat(cachedUser, is(nullValue()));

    }

    @Test
    public void fetchUserProfileSync_failure_UserNotCachedAuthError() {
        userProfileHttpEndpointSyncTD.isGeneralError = true;
        FetchUserProfileUseCaseSync.UseCaseResult useCaseResult = SUT.fetchUserProfileSync(userID);
        User cachedUser = usersCacheTD.user;
        Assert.assertThat(useCaseResult, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
        Assert.assertThat(cachedUser, is(nullValue()));
    }

    @Test
    public void fetchUserProfileSync_failure_UserNotCachedServerError() {
        userProfileHttpEndpointSyncTD.isServerError = true;
        FetchUserProfileUseCaseSync.UseCaseResult useCaseResult = SUT.fetchUserProfileSync(userID);
        User cachedUser = usersCacheTD.user;
        Assert.assertThat(useCaseResult, is(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE));
        Assert.assertThat(cachedUser, is(nullValue()));
    }

    @Test
    public void fetchUserProfileSync_failure_UserNotCachedNetworkError() {
        userProfileHttpEndpointSyncTD.isNetworkError = true;
        FetchUserProfileUseCaseSync.UseCaseResult useCaseResult = SUT.fetchUserProfileSync(userID);
        User cachedUser = usersCacheTD.user;
        Assert.assertThat(useCaseResult, is(FetchUserProfileUseCaseSync.UseCaseResult.NETWORK_ERROR));
        Assert.assertThat(cachedUser, is(nullValue()));
    }

    //----------------------------------------------------------------------------------------------
    //Dependencies for helper class
    private static class UserProfileHttpEndpointSyncTD implements UserProfileHttpEndpointSync {
        public boolean isNetworkError;
        boolean isGeneralError;
        public boolean usAuthError;
        public boolean isServerError;
        String _userId = "";

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            _userId = userId; // user id passed
            if (isGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", "", "");
            } else if (usAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", "", "");
            } else if (isServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", "", "");
            } else if (isNetworkError) {
                throw new NetworkErrorException();
            } else
                return new EndpointResult(EndpointResultStatus.SUCCESS, _userId, fullName, imageUrl);
        }
    }

    private static class UsersCacheTD implements UsersCache {
        User user;

        @Override
        public void cacheUser(User user) {
            this.user = user;
        }

        @Nullable
        @Override
        public User getUser(String userId) {
            return user;
        }
    }

}