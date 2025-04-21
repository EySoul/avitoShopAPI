package ru.minusd.security;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.minusd.security.domain.model.User;
import ru.minusd.security.repository.UserRepository;
import ru.minusd.security.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    public void testFindUserByUsername() {
        // Подготовка данных
        String username = "testUser";
        User expectedUser = new User();
        expectedUser.setUsername(username);
        expectedUser.setEmail("test@example.com");
        expectedUser.setPassword("password");

        // Настройка мока
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        // Вызов метода
        User actualUser = userService.getByUsername(username);

        // Проверка результатов
        assertNotNull(actualUser);
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());

        // Проверка, что метод репозитория был вызван
        verify(userRepository).findByUsername(username);
    }

    @Test
    public void testGiveMoney_UserExists() {
        // Подготовка данных
        String username = "testUser";
        Integer moneyToGive = 100;
        User userReceiver = new User();
        userReceiver.setUsername(username);
        userReceiver.setMoney(200);

        // Настройка мока
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userReceiver));

        // Вызов метода
        userService.giveMoney(username, moneyToGive);

        // Проверка, что деньги были добавлены
        assertEquals(300, userReceiver.getMoney());

    }

    @Test
    public void testSendMoney_Success() {
        // Подготовка данных
        String usernameSender = "senderUser";
        String usernameReceiver = "receiverUser";
        Integer moneyToSend = 50;

        User userSender = new User();
        userSender.setUsername(usernameSender);
        userSender.setMoney(100);

        User userReceiver = new User();
        userReceiver.setUsername(usernameReceiver);
        userReceiver.setMoney(200);

        // Настройка мока
        when(userRepository.findByUsername(usernameSender)).thenReturn(Optional.of(userSender));
        when(userRepository.findByUsername(usernameReceiver)).thenReturn(Optional.of(userReceiver));

        // Вызов метода
        userService.sendMoney(usernameSender, usernameReceiver, moneyToSend);

        // Проверка, что деньги были переведены
        assertEquals(50, userSender.getMoney());
        assertEquals(250, userReceiver.getMoney());

    }
}
