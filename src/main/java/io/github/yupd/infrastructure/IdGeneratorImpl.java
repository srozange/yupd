package io.github.yupd.infrastructure;

import io.github.yupd.domain.ports.out.IdGenerator;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class IdGeneratorImpl implements IdGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}