package io.github.yupd.integration;

import io.github.yupd.Yupd;
import io.github.yupd.domain.ports.out.IdGenerator;
import io.github.yupd.domain.service.YamlRepoUpdaterImpl;
import io.github.yupd.domain.model.YamlUpdateResult;
import io.github.yupd.infrastructure.IdGeneratorImpl;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.doAnswer;

public abstract class AbstractIntegrationTest {

    @Inject
    Yupd yupd;

    @InjectSpy
    YamlRepoUpdaterImpl yamlRepoUpdater;

    @InjectMock
    IdGenerator idGenerator;

    ResultCaptor<YamlUpdateResult> yamlRepoUpdaterResultCaptor;

    @BeforeEach
    void setup() {
        yamlRepoUpdaterResultCaptor = new ResultCaptor<>();
        doAnswer(yamlRepoUpdaterResultCaptor).when(yamlRepoUpdater).update(Mockito.any());
    }

    public static class ResultCaptor<T> implements Answer {
        private T result = null;
        public T getResult() {
            return result;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T answer(InvocationOnMock invocationOnMock) throws Throwable {
            result = (T) invocationOnMock.callRealMethod();
            return result;
        }
    }
}