package io.github.yupd.integration;

import io.github.yupd.Yupd;
import io.github.yupd.business.YamlRepoUpdater;
import io.github.yupd.infrastructure.utils.UniqueIdGenerator;
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
    YamlRepoUpdater yamlRepoUpdater;

    @InjectSpy
    UniqueIdGenerator uniqueIdGenerator;

    ResultCaptor<YamlRepoUpdater.YamlUpdateResult> yamlRepoUpdaterResultCaptor;

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