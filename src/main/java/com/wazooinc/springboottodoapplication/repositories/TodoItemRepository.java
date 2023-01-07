package com.wazooinc.springboottodoapplication.repositories;

import org.springframework.data.repository.CrudRepository;

import com.wazooinc.springboottodoapplication.models.TodoItem;

public interface TodoItemRepository extends CrudRepository<TodoItem, Long>  {
    
}
