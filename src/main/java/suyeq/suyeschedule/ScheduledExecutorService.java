package suyeq.suyeschedule;

import suyeq.ExecutorService;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-18
 * @time: 13:27
 */
public interface ScheduledExecutorService extends ExecutorService {

    /**
     * 延迟任务
     * @param task
     * @param time
     * @param unit
     */
    void schedule(Runnable task, long time, TimeUnit unit);

    /**
     * 有结果的周期任务
     * @param task
     * @param time
     * @param unit
     * @param result
     * @param <T>
     * @return
     */
     <T> Future<T> schedule(Runnable task, long time, TimeUnit unit,T result);

    /**
     * 周期任务
     * @param task
     * @param time
     * @param unit
     */
    void scheduleAtFixedRate(Runnable task, long time, TimeUnit unit);

    /**
     * 延迟周期任务
     * @param task
     * @param time
     * @param delay
     * @param unit
     */
    void scheduleAtFixedDelayRate(Runnable task, long time,long delay, TimeUnit unit);
}
