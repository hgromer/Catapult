package com.catapult.listener;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This event dispatcher allows us to consume events
 * https://github.com/kwhat/jnativehook/wiki/Usage#consuming-events-unsupported
 */
public class EventConsumerDispatcher extends AbstractExecutorService {
  private static final Logger LOG = LoggerFactory.getLogger(EventConsumerDispatcher.class);

  private final AtomicBoolean running = new AtomicBoolean(true);

  @Override
  public void execute(@Nonnull Runnable command) {
    if (running.get()) {
      // The command must be run synchronously or the event cannot
      // be consumed/suppressed and will be propagated
      command.run();
    }
  }

  @Override
  public void shutdown() {
    LOG.info("EventConsumerDispatcher shutting down");
    running.set(false);
  }

  @Override
  @Nonnull
  public List<Runnable> shutdownNow() {
    shutdown();
    return ImmutableList.of();
  }

  @Override
  public boolean isShutdown() {
    return !running.get();
  }

  @Override
  public boolean isTerminated() {
    return !running.get();
  }

  @Override
  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    return false;
  }
}
