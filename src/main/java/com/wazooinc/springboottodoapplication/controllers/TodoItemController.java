package com.wazooinc.springboottodoapplication.controllers;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
//import java.util.List;
import java.util.Map;
//import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wazooinc.springboottodoapplication.models.TodoItem;
import com.wazooinc.springboottodoapplication.repositories.TodoItemRepository;


import jakarta.validation.Valid;

@Controller
public class TodoItemController {
    private final Logger logger = LoggerFactory.getLogger(TodoItemController.class);
    
    @Autowired
    private TodoItemRepository todoItemRepository;
    
    // this is called when the page is directed to >> Home Page << - Working
    @GetMapping("/")
    public ModelAndView index() {
        logger.info("request to GET index");
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("todoItems", todoItemRepository.findAll());
        modelAndView.addObject("today", Instant.now().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek());
        return modelAndView;
    }

    // this is called when the >> Add Todo! << is hit - Working
    @PostMapping("/todo")
    public String createTodoItem(@Valid TodoItem todoItem, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-todo-item";
        }

        todoItem.setCreatedDate(Instant.now());
        todoItem.setModifiedDate(Instant.now());
        todoItemRepository.save(todoItem);
        return "redirect:/";
    }

    // this is called when we hit >> Update <<  
    @PostMapping("/todo/{id}")
    public String updateTodoItem(@PathVariable("id") long id, @Valid TodoItem todoItem, BindingResult result, Model model) {
        if (result.hasErrors()) {
            todoItem.setId(id);
            return "update-todo-item";
        }
        todoItem.setModifiedDate(Instant.now());
        todoItemRepository.save(todoItem);
        return "redirect:/";
    }

    //Create todo with postman - server side rendering means we give the whole data with html to the frontend 
    @PostMapping("/v2/todo")
    public String createTodoItem1(@RequestBody TodoItem todoItem, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-todo-item";
        }

        todoItem.setCreatedDate(Instant.now());
        todoItem.setModifiedDate(Instant.now());
        todoItemRepository.save(todoItem);
        return "redirect:/";
    }

    // Create todo with description and owner
    @PostMapping("/v3/todo")
    @ResponseBody
    public TodoItem createTodoItem2(@RequestBody TodoItem todoItem) {
        todoItem.setCreatedDate(Instant.now());
        todoItem.setModifiedDate(Instant.now());
        return todoItemRepository.save(todoItem);
        
    }

    // find by owner 
    @GetMapping("/view/owner") 
    @ResponseBody
    public TodoItem findByOwner(@RequestParam("owner") String owner) {
        return todoItemRepository.findByOwner(owner);
    }

    // find by id by giving id in request param 
    @GetMapping("/view")
    @ResponseBody
    public TodoItem findById(@RequestParam("id") long id) {
        return todoItemRepository.findById(id);
    }
    
    // find by id by giving id in path variable - {} means we are giving the value not the variable
    @GetMapping("/view/id/{id}")
    @ResponseBody
    public TodoItem findById1(@PathVariable("id") long id) {
        return todoItemRepository.findById(id);
    }

    // find by description
    @GetMapping("/view/description") 
    @ResponseBody
    public TodoItem findByDescription(@RequestParam("description") String description) {
        return todoItemRepository.findByDescription(description);
    }

    // list all todo
    @GetMapping("/list")
    @ResponseBody
    public List<TodoItem> getAllTodo() {
        return todoItemRepository.findAll();
    }
    
    // update
    @RequestMapping(value = "/v3/todo", method = RequestMethod.PUT)
    @ResponseBody
    public TodoItem update(@RequestBody TodoItem todoItem) {
        TodoItem currentTodoItem = todoItemRepository.findById(todoItem.getId().longValue());
        currentTodoItem.setDescription(todoItem.getDescription());
        currentTodoItem.setComplete(todoItem.isComplete());
        currentTodoItem.setOwner(todoItem.getOwner());
        currentTodoItem.setModifiedDate(Instant.now());
        return todoItemRepository.save(currentTodoItem);
    }

    // find by id - but fetch id by headers instead of query param
    @GetMapping("/view/header")
    @ResponseBody
    public TodoItem findById(@RequestHeader Map<String,String> headers) {
        String idStr = headers.get("id");
        long id = Long.valueOf(idStr);
        return todoItemRepository.findById(id);
    }

    // delete by id
    @org.springframework.transaction.annotation.Transactional
    @RequestMapping(value = "/v2/delete/id", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteById(@RequestParam("id") long id) {
        todoItemRepository.deleteById(id);
    }
    
}