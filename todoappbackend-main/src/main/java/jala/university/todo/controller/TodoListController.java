package jala.university.todo.controller;

import jala.university.todo.model.Task;
import jala.university.todo.model.TodoList;
import jala.university.todo.service.TodoListService;
import jala.university.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todolists")
@RequiredArgsConstructor
public class TodoListController {

    private final TodoListService todoListService;
    private final UserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TodoList>> getTodoListsByUserId(@PathVariable String userId) {
        List<TodoList> todoLists = todoListService.getTodoListsByUserId(userId);
        return ResponseEntity.ok(todoLists);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<TodoList> createTodoList(@PathVariable String userId, @RequestBody TodoList todoList) {
        try {
            TodoList createdList = todoListService.createTodoList(userId, todoList);
            return ResponseEntity.ok(createdList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoList> getTodoListById(@PathVariable String id) {
        TodoList todoList = todoListService.getTodoListById(id);
        if (todoList != null) {
            return ResponseEntity.ok(todoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoList> updateTodoList(@PathVariable String id, @RequestBody TodoList todoList) {
        return todoListService.updateTodoList(id, todoList)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoList(@PathVariable String id) {
        todoListService.deleteTodoList(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{listId}/tasks")
    public ResponseEntity<TodoList> addTask(@PathVariable String listId, @RequestBody Task task) {
        return todoListService.addTask(listId, task)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{listId}/tasks/{taskId}")
    public ResponseEntity<TodoList> updateTask(@PathVariable String listId, @PathVariable String taskId, @RequestBody Task task) {
        return todoListService.updateTask(listId, taskId, task)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{listId}/tasks/{taskId}")
    public ResponseEntity<TodoList> deleteTask(@PathVariable String listId, @PathVariable String taskId) {
        return todoListService.deleteTask(listId, taskId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}