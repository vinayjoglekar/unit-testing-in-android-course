package com.techyourchance.mockitofundamentals.exercise5;

import com.techyourchance.mockitofundamentals.example7.eventbus.LoggedInEvent;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class UpdateUsernameUseCaseSyncTest {

    UpdateUsernameUseCaseSync SUT;
    UpdateUsernameHttpEndpointSync updateUsernameHttpEndpointSyncMock;
    UsersCache usersCacheMock;
    EventBusPoster eventBusPosterMock;

    public static final String USERID = "userid";
    public static final String USERNAME = "username";

    @Before
    public void setUp() throws NetworkErrorException {
        updateUsernameHttpEndpointSyncMock = Mockito.mock(UpdateUsernameHttpEndpointSync.class);
        usersCacheMock = Mockito.mock(UsersCache.class);
        eventBusPosterMock = Mockito.mock(EventBusPoster.class);
        SUT = new UpdateUsernameUseCaseSync(updateUsernameHttpEndpointSyncMock, usersCacheMock,
                eventBusPosterMock);
    }

    private void success() throws NetworkErrorException {
        when(updateUsernameHttpEndpointSyncMock.updateUsername(any(String.class), any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS, USERID, USERNAME));
    }


    @Test
    public void updateUserNameSync_success_UserIdUserNamePassedToEndPoint() throws Exception {
        success();
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.updateUsernameSync(USERID, USERNAME);
        verify(updateUsernameHttpEndpointSyncMock).updateUsername(ac.capture(), ac.capture());
        List<String> captures = ac.getAllValues();
        Assert.assertThat(captures.get(0), is(USERID));
        Assert.assertThat(captures.get(1), is(USERNAME));
    }


    @Test
    public void updateUserNameSync_success_UserCached() throws Exception {
        success();
        SUT.updateUsernameSync(USERID, USERNAME);
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        verify(usersCacheMock).cacheUser(ac.capture());
        User captures = ac.getValue();
        Assert.assertThat(captures.getUserId(), is(USERID));
        Assert.assertThat(captures.getUsername(), is(USERNAME));
    }

    @Test
    public void updateUserNameSync_success_SuccessReturned() throws Exception {
        success();
        UpdateUsernameUseCaseSync.UseCaseResult useCaseResult = SUT.updateUsernameSync(USERID, USERNAME);
        Assert.assertThat(useCaseResult, is(UpdateUsernameUseCaseSync.UseCaseResult.SUCCESS));
    }

    @Test
    public void updateUserNameSync_success_eventPosted() throws Exception {
        success();
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        SUT.updateUsernameSync(USERID, USERNAME);
        verify(eventBusPosterMock).postEvent(argumentCaptor.capture());
        Assert.assertThat(argumentCaptor.getValue(), is(instanceOf(UserDetailsChangedEvent.class)));
    }



    private void generalError() throws NetworkErrorException {
        when(updateUsernameHttpEndpointSyncMock
                .updateUsername(any(String.class), any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult
                        (UpdateUsernameHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,
                                "",
                                ""));
    }

    @Test
    public void updateUserNameSync_generalError_UserNotCached() throws Exception {
        generalError();
        SUT.updateUsernameSync(USERID, USERNAME);
        // usersCacheMock is not touched or interacted with as there was general error
        verifyNoMoreInteractions(usersCacheMock);
    }

    @Test
    public void updateUserNameSync_generalError_eventNotPosted() throws Exception {
        generalError();
        SUT.updateUsernameSync(USERID, USERNAME);
        // usersCacheMock is not touched or interacted with as there was general error
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void updateUserNameSync_generalError_failure() throws Exception {
        generalError();
        UpdateUsernameUseCaseSync.UseCaseResult useCaseResult = SUT.updateUsernameSync(USERID, USERNAME);
        Assert.assertThat(useCaseResult, is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    private void authError() throws NetworkErrorException {
        when(updateUsernameHttpEndpointSyncMock
                .updateUsername(any(String.class), any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult
                        (UpdateUsernameHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,
                                "",
                                ""));
    }

    @Test
    public void updateUserNameSync_authError_UserNotCached() throws Exception {
        authError();
        SUT.updateUsernameSync(USERID, USERNAME);
        // usersCacheMock is not touched or interacted with as there was auth error
        verifyNoMoreInteractions(usersCacheMock);
    }


    @Test
    public void updateUserNameSync_authError_eventNotPosted() throws Exception {
        authError();
        SUT.updateUsernameSync(USERID, USERNAME);
        // usersCacheMock is not touched or interacted with as there was general error
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void updateUserNameSync_authError_failure() throws Exception {
        authError();
        UpdateUsernameUseCaseSync.UseCaseResult useCaseResult = SUT.updateUsernameSync(USERID, USERNAME);
        Assert.assertThat(useCaseResult, is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    private void serverError() throws NetworkErrorException {
        when(updateUsernameHttpEndpointSyncMock
                .updateUsername(any(String.class), any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult
                        (UpdateUsernameHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,
                                "",
                                ""));
    }

    @Test
    public void updateUserNameSync_serverError_UserNotCached() throws Exception {
        serverError();
        SUT.updateUsernameSync(USERID, USERNAME);
        // usersCacheMock is not touched or interacted with as there was auth error
        verifyNoMoreInteractions(usersCacheMock);
    }

    @Test
    public void updateUserNameSync_serverError_eventNotPosted() throws Exception {
        serverError();
        SUT.updateUsernameSync(USERID, USERNAME);
        // usersCacheMock is not touched or interacted with as there was general error
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void updateUserNameSync_serverError_failure() throws Exception {
        serverError();
        UpdateUsernameUseCaseSync.UseCaseResult useCaseResult = SUT.updateUsernameSync(USERID, USERNAME);
        Assert.assertThat(useCaseResult, is(UpdateUsernameUseCaseSync.UseCaseResult.FAILURE));
    }

    private void networkError() throws NetworkErrorException {
        doThrow(new NetworkErrorException()).when(updateUsernameHttpEndpointSyncMock)
                .updateUsername(any(String.class), any(String.class));
    }


    @Test
    public void updateUserNameSync_networkError_UserNotCached() throws Exception {
        networkError();
        SUT.updateUsernameSync(USERID, USERNAME);
        // usersCacheMock is not touched or interacted with as there was auth error
        verifyNoMoreInteractions(usersCacheMock);
    }

    @Test
    public void updateUserNameSync_networkError_eventNotPosted() throws Exception {
        networkError();
        SUT.updateUsernameSync(USERID, USERNAME);
        // usersCacheMock is not touched or interacted with as there was general error
        verifyNoMoreInteractions(eventBusPosterMock);
    }

}