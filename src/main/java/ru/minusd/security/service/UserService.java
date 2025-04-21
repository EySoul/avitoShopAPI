package ru.minusd.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.minusd.security.domain.model.Role;
import ru.minusd.security.domain.model.User;
import ru.minusd.security.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public User save(User user) {
        return repository.save(user);
    }


    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            // Заменить на свои исключения
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }


    /**
     * Выдача прав администратора текущему пользователю
     * <p>
     * Нужен для демонстрации
     */
    @Deprecated
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }

    public Optional<User> findById(Long id){
        return repository.findById(id);
    }

    //отправка от пользователя к пользователю
    public boolean sendMoney(String usernameSender, String usernameReceiver, Integer money){
        User userReceiver = repository.findByUsername(usernameReceiver).orElse(null);
        User userSender = repository.findByUsername(usernameSender).orElse(null);
        if(userSender.getMoney() < money) return false;
        userReceiver.setMoney(userReceiver.getMoney() + money);
        userSender.setMoney(userSender.getMoney() - money);
        save(userReceiver);
        save(userSender);
        return true;
    }

    public void giveMoney(String username, Integer money){
        User userReceiver = repository.findByUsername(username).orElse(null);
        userReceiver.setMoney(userReceiver.getMoney() + money);
        save(userReceiver);
        System.out.println("Выдано " + money + "мистеру " + username);
        System.out.println(userReceiver.getMoney() + userReceiver.getUsername());
    }


}
