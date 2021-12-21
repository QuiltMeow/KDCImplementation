package ew.sr.x1c.quilt.meow.endpoint.schedule;

import ew.sr.x1c.quilt.meow.endpoint.MainGUI;
import ew.sr.x1c.quilt.meow.endpoint.util.Randomizer;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public abstract class TaskScheduler {

    public static class ScheduleTimer extends TaskScheduler {

        private static final ScheduleTimer INSTANCE = new ScheduleTimer();

        private ScheduleTimer() {
            name = "ScheduleTimer";
        }

        public static ScheduleTimer getInstance() {
            return INSTANCE;
        }
    }

    private ScheduledThreadPoolExecutor stpe;
    protected String name;
    private static final AtomicInteger THREAD_NUMBER = new AtomicInteger(1);

    public void start() {
        if (stpe != null && !stpe.isShutdown() && !stpe.isTerminated()) {
            return;
        }
        stpe = new ScheduledThreadPoolExecutor(10, new RejectThreadFactory());
        stpe.setKeepAliveTime(10, TimeUnit.MINUTES);
        stpe.allowCoreThreadTimeOut(true);
        stpe.setMaximumPoolSize(Integer.MAX_VALUE);
        stpe.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
    }

    public ScheduledThreadPoolExecutor getSTPE() {
        return stpe;
    }

    public void stop() {
        if (stpe != null) {
            stpe.shutdown();
        }
    }

    public ScheduledFuture<?> register(Runnable runnable, long repeatTime, long delay) {
        if (stpe == null) {
            return null;
        }
        return stpe.scheduleAtFixedRate(new LoggingSaveRunnable(runnable), delay, repeatTime, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> registerMinute(Runnable runnable, long repeatTime, long delay) {
        if (stpe == null) {
            return null;
        }
        return stpe.scheduleAtFixedRate(new LoggingSaveRunnable(runnable), delay, repeatTime, TimeUnit.MINUTES);
    }

    public ScheduledFuture<?> registerHour(Runnable runnable, long repeatTime, long delay) {
        if (stpe == null) {
            return null;
        }
        return stpe.scheduleAtFixedRate(new LoggingSaveRunnable(runnable), delay, repeatTime, TimeUnit.HOURS);
    }

    public ScheduledFuture<?> register(Runnable runnable, long repeatTime) {
        if (stpe == null) {
            return null;
        }
        return stpe.scheduleAtFixedRate(new LoggingSaveRunnable(runnable), 0, repeatTime, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> schedule(Runnable runnable, long delay) {
        if (stpe == null) {
            return null;
        }
        return stpe.schedule(new LoggingSaveRunnable(runnable), delay, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> scheduleMinute(Runnable runnable, long delay) {
        if (stpe == null) {
            return null;
        }
        return stpe.schedule(new LoggingSaveRunnable(runnable), delay, TimeUnit.MINUTES);
    }

    public ScheduledFuture<?> scheduleSecond(Runnable runnable, long delay) {
        if (stpe == null) {
            return null;
        }
        return stpe.schedule(new LoggingSaveRunnable(runnable), delay, TimeUnit.SECONDS);
    }

    public ScheduledFuture<?> scheduleAtTimestamp(Runnable runnable, long timestamp) {
        return schedule(runnable, timestamp - System.currentTimeMillis());
    }

    private static class LoggingSaveRunnable implements Runnable {

        private final Runnable runnable;

        public LoggingSaveRunnable(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            try {
                runnable.run();
            } catch (Exception ex) {
                MainGUI.getInstance().getLogger().log(Level.WARNING, ex.toString());
            }
        }
    }

    private class RejectThreadFactory implements ThreadFactory {

        private final AtomicInteger threadNumber2;
        private final String threadName;

        public RejectThreadFactory() {
            threadNumber2 = new AtomicInteger(1);
            threadName = name + "-" + Randomizer.nextInt();
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName(threadName + "-Worker-" + THREAD_NUMBER.getAndIncrement() + "-" + threadNumber2.getAndIncrement());
            return thread;
        }
    }
}
