package com.github.alexandrenavarro.springboot3sample.service;

import com.github.alexandrenavarro.springboot3sample.persistence.TodoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public final class TodoService {

    private final TodoRepository todoRepository;

}
