package io.github.yupd.infrastructure.utils;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class UniqueIdGenerator {

    public String generate() {
        return UUID.randomUUID().toString();
    }

}
