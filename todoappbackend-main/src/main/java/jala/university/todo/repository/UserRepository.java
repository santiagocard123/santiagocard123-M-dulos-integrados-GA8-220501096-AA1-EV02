package jala.university.todo.repository;


import jala.university.todo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}

