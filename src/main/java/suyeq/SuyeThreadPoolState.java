package suyeq;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-07
 * @time: 20:05
 */
public class SuyeThreadPoolState {

        //   State:
    //        /**
    //         * 线程池运行的状态
    //         */
    //        RUN,
    //        /**
    //         * 线程池要停止时的状态，该状态不能接受新的任务，
    //         * 但是可以处理阻塞队列中的任务
    //         */
    //        STOP,
    //        /**
    //         * 所有的任务已完成，且线程都已销毁时的状态
    //         * 表明线程池可以被回收了
    //         */
    //        DESTROY

//    private final int RUN=Integer.MAX_VALUE-3;
//
//    private final int STOP=Integer.MAX_VALUE-2;
//
//    private final int DESTROY=Integer.MAX_VALUE-1;

    private final int RUN=0;

    private final int STOP=1;

    private final int DESTROY=2;

    private final int poolState=(1<<2)-1;

    private final int poolThreadSize=Integer.MAX_VALUE-3;

    private final AtomicInteger poolStateAndWorkThreadSize=new AtomicInteger(poolStateMergeSize(RUN,0));

    private static SuyeThreadPoolState suyeThreadPoolState;

    private SuyeThreadPoolState(){}

    /**
     * 获得线程池中工作者线程的数量
     * @return
     */
    public int getWorkThreadSize(){
        return (getPoolSizeAndState()&poolThreadSize)>>2;
    }

    /**
     * 获得线程池的状态
     * @return
     */
    public int getPoolState(){
        return getPoolSizeAndState()&poolState;
    }

    /**
     * 获得状态与数量的合并二进制编码
     * @param poolState
     * @param poolThreadSize
     * @return
     */
    private int poolStateMergeSize(int poolState,int poolThreadSize){
        return poolState | (poolThreadSize<<2);
    }

    /**
     * 让线程池的状态变为STOP
     * @return
     */
    public boolean setPoolStateToStop(){
        return poolStateAndWorkThreadSize.compareAndSet(getPoolSizeAndState(),poolStateMergeSize(STOP,getWorkThreadSize()));
    }

    /**
     * 让线程池的状态变为DESTROY
     * @return
     */
    public boolean setPoolStateToDestroy(){
        return poolStateAndWorkThreadSize.compareAndSet(getPoolSizeAndState(),poolStateMergeSize(DESTROY,getWorkThreadSize()));
    }

    public int increasePoolThreadSize(){
        int poolThreadSize=getWorkThreadSize();
        int newPoolThreadSize=poolThreadSize+1;
        poolStateAndWorkThreadSize.compareAndSet(getPoolSizeAndState(),poolStateMergeSize(getPoolState(),newPoolThreadSize));
        return newPoolThreadSize;
    }

    /**
     * 获得单例
     * @return
     */
    public static SuyeThreadPoolState getInstance(){
        synchronized (SuyeThreadPool.class){
            if (suyeThreadPoolState==null){
                suyeThreadPoolState=new SuyeThreadPoolState();
                return suyeThreadPoolState;
            }
            return suyeThreadPoolState;
        }
    }

    public int getPoolSizeAndState(){
        return poolStateAndWorkThreadSize.get();
    }

    public int RunState(){
        return RUN;
    }

    public int StopState(){
        return STOP;
    }

    public int DestroyState(){
        return DESTROY;
    }

}
