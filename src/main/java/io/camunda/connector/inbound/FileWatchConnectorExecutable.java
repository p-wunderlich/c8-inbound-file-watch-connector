package io.camunda.connector.inbound;

import io.camunda.connector.api.annotation.InboundConnector;
import io.camunda.connector.api.inbound.InboundConnectorContext;
import io.camunda.connector.api.inbound.InboundConnectorExecutable;
import io.camunda.connector.inbound.subscription.WatchServiceSubscription;
import io.camunda.connector.inbound.subscription.WatchServiceSubscriptionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@InboundConnector(
        name = "Watch Service Inbound Connector",
        type = "io.camunda:watchserviceinbound:1"
)
public class FileWatchConnectorExecutable implements InboundConnectorExecutable<InboundConnectorContext> {

    private final static Logger LOG = LoggerFactory.getLogger(FileWatchConnectorExecutable.class);

    private WatchServiceSubscription subscription;
    private InboundConnectorContext context;
    public CompletableFuture<?> future;

    @Override
    public void activate(InboundConnectorContext context) {

        FileWatchConnectorProperties props = context.bindProperties(FileWatchConnectorProperties.class);
        LOG.info("Start Connector for event: {}", props.eventToMonitor());
        this.context = context;
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        this.future = CompletableFuture.runAsync(
                () -> subscription = new WatchServiceSubscription(
                        props.eventToMonitor(),
                        props.directory(),
                        props.pollingInterval(),
                        this::onEvent),
                executorService);
    }

    private void onEvent(WatchServiceSubscriptionEvent rawEvent) {
        FileWatchConnectorEvent connectorEvent = new FileWatchConnectorEvent(rawEvent);
        context.correlateWithResult(connectorEvent);
    }

    @Override
    public void deactivate() {
        subscription.stop();
    }
}
