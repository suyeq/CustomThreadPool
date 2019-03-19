package suyeq.suyeschedule;

import suyeq.SuyeThreadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-18
 * @time: 9:02
 */
public class SuyeScheduleThreadPool extends SuyeThreadPool implements ScheduledExecutorService {

    public SuyeScheduleThreadPool(int core){
        super(core,new DelayWorkQueue());
    }

    /**
     * 定时任务提交
     * @param task
     * @param time
     * @param unit
     */
    @Override
    public void schedule(Runnable task, long time, TimeUnit unit) {
        super.getBlockQueue().offer(new ScheduledFutureTask<Void>(task,time,unit));
        executeDelay();
    }

    /**
     * 定时执行
     * @return
     */
    private boolean executeDelay(){
        int poolState=suyeThreadPoolState.getPoolState();
        int workThreadSize=suyeThreadPoolState.getWorkThreadSize();
        if (poolState<suyeThreadPoolState.StopState() && workThreadSize<bestPoolThreadSize){
            return super.addWorkThread(null);
        }
        return false;
    }

    /**
     * 定时结果任务提交
     * @param task
     * @param time
     * @param unit
     * @param result
     * @param <T>
     * @return
     */
    @Override
    public <T> Future<T> schedule(Runnable task, long time, TimeUnit unit, final T result) {
        Callable<T> callable= Executors.callable(task,result);
        Future<T> future=new ScheduledFutureTask<T>(callable,time,unit);
        super.getBlockQueue().offer(future);
        executeDelay();
        return future;
    }

    @Override
    public void scheduleAtFixedRate(Runnable task, long time, TimeUnit unit) {

    }

    @Override
    public void scheduleAtFixedDelayRate(Runnable task, long time, long delay, TimeUnit unit) {

    }

}
