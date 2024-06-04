package io.camunda.connector.inbound;

import io.camunda.connector.inbound.subscription.WatchServiceSubscriptionEvent;

/**
 * Data model of an event produced by the inbound Connector
 *
 * @param event
 */
public record FileWatchConnectorEvent(WatchServiceSubscriptionEvent event) {}
