package com.wazooinc.springboottodoapplication.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;

import com.wazooinc.springboottodoapplication.models.TodoItem;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long>  {

    TodoItem findByOwner(String owner);

    TodoItem findById(long id);

    TodoItem findByDescription(String description);

    void deleteById(long id);

    TodoItem save(Optional<TodoItem> newTodoItem);

    


    
}
