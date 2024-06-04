package io.camunda.connector.inbound.subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class WatchServiceSubscription {
    private final static Logger LOG = LoggerFactory.getLogger(WatchServiceSubscription.class);

    private boolean watch = true;

    public WatchServiceSubscription(String eventToMonitor, String directory, String pollingInterval, Consumer<WatchServiceSubscriptionEvent> callback) {
        LOG.info("Activating WatcherService subscription");
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(directory);

            path.register(watchService, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

            while (watch) {
                WatchKey watchKey = watchService.poll(Long.parseLong(pollingInterval), TimeUnit.SECONDS);
                if (watchKey != null) {
                    for (WatchEvent<?> event : watchKey.pollEvents()) {
                        LOG.info("Event kind : {} - File : {} event to monitor: {}", event.kind(), event.context(), eventToMonitor);
                        if (event.kind().toString().equals(eventToMonitor)) {
                            WatchServiceSubscriptionEvent wsse = new WatchServiceSubscriptionEvent(eventToMonitor, directory, event.context().toString());
                            callback.accept(wsse);
                        }
                    }
                    watchKey.reset();

                } else {
                    LOG.info("No files during interval");
                }
            }

        } catch (Exception e) {
            LOG.error("Problem with connector: ", e);
        }
    }

    public void stop() {
        watch = false;
        LOG.info("Deactivating file watcher service");
    }
}
