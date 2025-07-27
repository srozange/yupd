package io.github.yupd.integration;

import io.github.yupd.Yupd;
import io.github.yupd.domain.ports.out.IdGenerator;
import io.github.yupd.domain.service.GitFileUpdaterImpl;
import io.github.yupd.domain.model.GitFileUpdateResult;
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
    GitFileUpdaterImpl gitFileUpdater;

    @InjectMock
    IdGenerator idGenerator;

    ResultCaptor<GitFileUpdateResult> gitFileUpdaterResultCaptor;

    @BeforeEach
    void setup() {
        gitFileUpdaterResultCaptor = new ResultCaptor<>();
        doAnswer(gitFileUpdaterResultCaptor).when(gitFileUpdater).update(Mockito.any());
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