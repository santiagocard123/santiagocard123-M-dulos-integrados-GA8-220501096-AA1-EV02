package jala.university.todo.repository;


import jala.university.todo.model.TodoList;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TodoListRepository extends MongoRepository<TodoList, String> {
    List<TodoList> findByUserId(String userId);
}
