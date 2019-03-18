package suyeq.suyeschedule;

import suyeq.SuyeThreadPool;

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

    @Override
    public void schedule(Runnable task, long time, TimeUnit unit) {
        super.getBlockQueue().offer(new ScheduledFutureTask<Void>(task,time,unit));
        executeDelay();
    }


    private boolean executeDelay(){
        int poolState=suyeThreadPoolState.getPoolState();
        int workThreadSize=suyeThreadPoolState.getWorkThreadSize();
        if (poolState<suyeThreadPoolState.StopState() && workThreadSize<bestPoolThreadSize){
            return super.addWorkThread(null);
        }
        return false;
    }



    @Override
    public <T> Future<T> schedule(Runnable task, long time, TimeUnit unit, T result) {
        return null;
    }

    @Override
    public void scheduleAtFixedRate(Runnable task, long time, TimeUnit unit) {

    }

    @Override
    public void scheduleAtFixedDelayRate(Runnable task, long time, long delay, TimeUnit unit) {

    }

}
