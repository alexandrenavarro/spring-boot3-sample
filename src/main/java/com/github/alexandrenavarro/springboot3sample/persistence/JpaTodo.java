package com.github.alexandrenavarro.springboot3sample.persistence;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Entity
@AllArgsConstructor
@Getter
@Builder
public final class JpaTodo {

    @Id
    private UUID uuid;

}
