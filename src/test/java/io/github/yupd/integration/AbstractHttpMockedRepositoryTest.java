package io.github.yupd.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.VerificationException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.github.tomakehurst.wiremock.verification.NearMiss;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import java.io.File;
import java.util.List;

import static java.lang.Boolean.parseBoolean;

public abstract class AbstractHttpMockedRepositoryTest extends AbstractIntegrationTest {

    private static final boolean RECORD_SNAPSHOT = parseBoolean(System.getProperty("record.snapshot.enabled"));

    private WireMockServer server;

    @BeforeEach
    void setupWiremock(TestInfo testInfo) {
        WireMockConfiguration options = WireMockConfiguration.options().dynamicPort();

        options.withRootDirectory(computeDirectory(options.filesRoot(), testInfo));
        new File(options.filesRoot().getPath() + "/mappings").mkdirs();

        server = new WireMockServer(options);
        server.start();

        if (RECORD_SNAPSHOT) {
            server.stubFor(WireMock.proxyAllTo(getProxyUrl()).atPriority(1));
        }
    }

    private static String computeDirectory(FileSource baseDirectory, TestInfo testInfo) {
        String subDirectory = testInfo.getTestClass().get().getSimpleName() + "/" + testInfo.getTestMethod().get().getName();
        return  baseDirectory.child(subDirectory).getPath();
    }

    @AfterEach
    void stopWiremock() {
        if (RECORD_SNAPSHOT) {
            server.snapshotRecord();
        } else {
            checkForUnmatchedRequests();
        }

        server.stop();
    }

    private void checkForUnmatchedRequests() {
        List<LoggedRequest> unmatchedRequests = server.findAllUnmatchedRequests();
        if (!unmatchedRequests.isEmpty()) {
            List<NearMiss> nearMisses = server.findNearMissesForAllUnmatchedRequests();
            if (nearMisses.isEmpty()) {
                throw VerificationException.forUnmatchedRequests(unmatchedRequests);
            }
            throw VerificationException.forUnmatchedNearMisses(nearMisses);
        }
    }

    abstract String getProxyUrl();

    String getServerUrl() {
        return server.baseUrl();
    }

    String getToken() {
        if (RECORD_SNAPSHOT) {
            return System.getProperty("record.snapshot.token");
        }
        return "foo";
    }

}
