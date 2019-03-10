package suyeq;

import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-10
 * @time: 14:23
 */
public interface ExecutorService extends Executor{
    /**
     * 停止线程池，将线程池的状态变为STOP
     */
    void shutdown();

    /**
     * 判断线程池是否已经停止
     * @return
     */
    boolean isShutdown();

    /**
     * 指定返回结果的提交方法
     * @param task
     * @param result
     * @param <T>
     * @return
     */
    <T>Future<T> submit(Runnable task,T result);


}
