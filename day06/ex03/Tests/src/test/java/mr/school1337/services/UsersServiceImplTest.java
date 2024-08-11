package test.java.mr.school1337.services;

import main.java.mr.school1337.models.User;
import main.java.mr.school1337.repositories.UserRepositorie;
import main.java.mr.school1337.services.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import java.nio.channels.AlreadyBoundException;

import org.junit.jupiter.api.Test;

public class UsersServiceImplTest {
    final User TEST_USER = new User("reda", "amali");

    @Test
    public void isAuthenticatedValidForCorrectCredentials() throws AlreadyBoundException {
        UserRepositorie mockRepository = mock(UserRepositorie.class);
        UserServiceImpl usersService = new UserServiceImpl(mockRepository);
        when(mockRepository.findByLogin(TEST_USER.getLogin())).thenReturn(TEST_USER);
        assertDoesNotThrow(() -> {
            assertTrue(usersService.authenticate(TEST_USER.getLogin(), TEST_USER.getPassword()));
        });
        // Verify that update was called
        verify(mockRepository, times(1)).update(any(User.class));
    }

    @Test
    public void isAuthenticatedInvalidForIncorrectLogin() {
        UserRepositorie mockRepository = mock(UserRepositorie.class);
        UserServiceImpl usersService = new UserServiceImpl(mockRepository);
        when(mockRepository.findByLogin(TEST_USER.getLogin())).thenReturn(null);
        assertFalse(usersService.authenticate(TEST_USER.getLogin(), TEST_USER.getPassword()));
        // Verify that findByLogin was called exactly once
        verify(mockRepository, times(1)).findByLogin(TEST_USER.getLogin());
    }


    @Test
    public void isAuthenticatedInvalidForIncorrectPassword() {
        UserRepositorie mockRepository = mock(UserRepositorie.class);
        UserServiceImpl usersService = new UserServiceImpl(mockRepository);
        when(mockRepository.findByLogin(TEST_USER.getLogin())).thenReturn(TEST_USER);
        assertFalse(usersService.authenticate(TEST_USER.getLogin(), "hngdfsah"));
        // Verify that findByLogin was called exactly once
        verify(mockRepository, times(1)).findByLogin(TEST_USER.getLogin());
    }
}
