package io.camunda.connector.inbound;

public record FileWatchConnectorProperties(
        String eventToMonitor,
        String directory,
        String pollingInterval
) {}
