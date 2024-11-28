package jala.university.todo.service;

import jala.university.todo.model.User;
import jala.university.todo.repository.TodoListRepository;
import jala.university.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TodoListRepository todoListRepository;
    private final PasswordEncoder passwordEncoder;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }


    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return null; // User already exists
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreationDate(LocalDateTime.now());
        return userRepository.save(user);
    }

    public Optional<User> updateUser(String id, User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setNameUser(user.getNameUser());
                    existingUser.setEmail(user.getEmail());
                    if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    }
                    existingUser.setTodoLists(user.getTodoLists());
                    return userRepository.save(existingUser);
                });
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public Optional<User> removeTodoListFromUser(String userId, String todoListId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setTodoLists(user.getTodoLists().stream()
                            .filter(id -> !id.equals(todoListId))
                            .toList());
                    return userRepository.save(user);
                });
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

}
