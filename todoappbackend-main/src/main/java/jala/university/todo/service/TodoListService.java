package jala.university.todo.service;


import jala.university.todo.model.Task;
import jala.university.todo.model.TodoList;
import jala.university.todo.model.User;
import jala.university.todo.repository.TodoListRepository;
import jala.university.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodoListService {

    @Autowired
    private final TodoListRepository todoListRepository;

    @Autowired
    private UserRepository userRepository;

    public List<TodoList> getTodoListsByUserId(String userId) {
        return todoListRepository.findByUserId(userId);
    }

    public TodoList getTodoListById(String id) {
        return todoListRepository.findById(id).orElse(null);
    }

    public TodoList createTodoList(String userId, TodoList todoList) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        todoList.setUserId(userId);
        TodoList savedTodoList = todoListRepository.save(todoList);
        user.getTodoLists().add(savedTodoList);
        userRepository.save(user);
        return savedTodoList;
    }


    public void deleteTodoList(String id) {
        TodoList todoList = todoListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TodoList not found"));
        String userId = todoList.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.getTodoLists().removeIf(list -> list.getId().equals(id));
        userRepository.save(user);
        todoListRepository.deleteById(id);
    }

    public Optional<TodoList> updateTodoList(String id, TodoList updatedList) {
        return todoListRepository.findById(id)
                .map(existingList -> {
                    existingList.setName(updatedList.getName());
                    existingList.setDescription(updatedList.getDescription());

                    if (updatedList.getTasks() != null && !updatedList.getTasks().isEmpty()) {
                        existingList.setTasks(updatedList.getTasks());
                    }

                    return todoListRepository.save(existingList);
                });
    }

    public Optional<TodoList> addTask(String listId, Task task) {
        task.setId(UUID.randomUUID().toString());
        return todoListRepository.findById(listId)
                .map(todoList -> {
                    todoList.getTasks().add(task);
                    return todoListRepository.save(todoList);
                });
    }

    public Optional<TodoList> updateTask(String listId, String taskId, Task updatedTask) {
        return todoListRepository.findById(listId)
                .map(todoList -> {
                    List<Task> list = new ArrayList<>();
                    for (Task task1 : todoList.getTasks()) {
                        if (task1.getId() != null && task1.getId().equals(taskId)) {
                            task1.setName(updatedTask.getName());
                            task1.setDescription(updatedTask.getDescription());
                            task1.setState(updatedTask.getState());
                            task1.setDueDate(updatedTask.getDueDate());
                            task1.setCompletedDate(updatedTask.getCompletedDate());
                        }
                        Task apply = task1;
                        list.add(apply);
                    }
                    todoList.setTasks(list);
                    return todoListRepository.save(todoList);
                });
    }


    public Optional<TodoList> deleteTask(String listId, String taskId) {
        return todoListRepository.findById(listId)
                .map(todoList -> {
                    todoList.setTasks(todoList.getTasks().stream()
                            .filter(task -> !task.getId().equals(taskId))
                            .toList());
                    return todoListRepository.save(todoList);
                });
    }

}
