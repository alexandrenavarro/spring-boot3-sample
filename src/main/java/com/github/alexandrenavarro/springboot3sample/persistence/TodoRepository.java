package com.github.alexandrenavarro.springboot3sample.persistence;

import com.github.alexandrenavarro.springboot3sample.persistence.model.JpaTodo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface TodoRepository extends CrudRepository<JpaTodo, UUID> {

}
