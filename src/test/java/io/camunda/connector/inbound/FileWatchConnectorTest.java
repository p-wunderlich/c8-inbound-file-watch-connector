package io.camunda.connector.inbound;

import io.camunda.connector.test.inbound.InboundConnectorContextBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class FileWatchConnectorTest {

    String eventToMonitor = "ENTRY_CREATE";
    String directory = File.separator+"Users"+File.separator+"patrickschalk"+File.separator+"Downloads";
    String pollingInterval = "30";

    @Test
    void shouldFailWhenValidate_NoEventToMonitor() {
        // given
        var input = new FileWatchConnectorProperties(eventToMonitor, directory, pollingInterval);
        var context = InboundConnectorContextBuilder.create().properties(input).build();

        // when
        var connectorInput = context.bindProperties(FileWatchConnectorProperties.class);

        // then
        assertThat(connectorInput)
                .isInstanceOf(FileWatchConnectorProperties.class)
                .extracting("eventToMonitor")
                .isEqualTo("ENTRY_CREATE");
    }

    @Test
    void shouldFailWhenValidate_NoDirectory() {
        // given
        var input = new FileWatchConnectorProperties(eventToMonitor, directory, pollingInterval);
        var context = InboundConnectorContextBuilder.create().properties(input).build();

        // when
        var connectorInput = context.bindProperties(FileWatchConnectorProperties.class);

        // then
        assertThat(connectorInput)
                .isInstanceOf(FileWatchConnectorProperties.class)
                .extracting("directory")
                .isEqualTo(directory);
    }

    @Test
    void shouldFailWhenValidate_NoPollingInterval() {
        // given
        var input = new FileWatchConnectorProperties(eventToMonitor, directory, pollingInterval);
        var context = InboundConnectorContextBuilder.create().properties(input).build();

        // when
        var connectorInput = context.bindProperties(FileWatchConnectorProperties.class);

        // then
        assertThat(connectorInput)
                .isInstanceOf(FileWatchConnectorProperties.class)
                .extracting("pollingInterval")
                .isEqualTo("30");
    }

}
