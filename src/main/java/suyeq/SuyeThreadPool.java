package suyeq;


import javafx.concurrent.Worker;

import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-07
 * @time: 19:57
 */
public class SuyeThreadPool implements Executor {

    /**
     * 线程池最高效率执行的工作线程数
     */
    private int bestPoolThreadSize;
    /**
     * 线程池中活跃的最大工作者线程数量
     */
    private int theMostPoolThreadSize;
    /**
     * 任务阻塞队列
     */
    private final BlockingQueue<Runnable> taskQueue;
    /**
     * 存放工作者线程的集合
     */
    private final HashSet<WorkThread> workThreadSet;

    private volatile int workThreadSize;
    /**
     * 保存线程池的状态
     */
    private final SuyeThreadPoolState suyeThreadPoolState;

    private final ThreadRepository threadRepository;

    private final Lock mainLock=new ReentrantLock();

    private final Condition mainCondition=mainLock.newCondition();

    public SuyeThreadPool(){
        this.bestPoolThreadSize=Runtime.getRuntime().availableProcessors();
        this.theMostPoolThreadSize=bestPoolThreadSize;
        this.taskQueue=new LinkedBlockingQueue<Runnable>(theMostPoolThreadSize*2);
        this.workThreadSet=new HashSet<WorkThread>();
        this.suyeThreadPoolState=SuyeThreadPoolState.getInstance();
        this.threadRepository=ThreadRepository.newInstance();
    }

    public SuyeThreadPool(int theMostPoolThreadSize){
        this.bestPoolThreadSize=Runtime.getRuntime().availableProcessors();
        this.theMostPoolThreadSize=theMostPoolThreadSize;
        this.taskQueue=new LinkedBlockingQueue<Runnable>(theMostPoolThreadSize*2);
        this.workThreadSet=new HashSet<WorkThread>();
        this.suyeThreadPoolState=SuyeThreadPoolState.getInstance();
        this.threadRepository=ThreadRepository.newInstance();
    }

    public SuyeThreadPool(int theMostPoolThreadSize,int taskQueueSize){
        this.bestPoolThreadSize=Runtime.getRuntime().availableProcessors();
        this.theMostPoolThreadSize=theMostPoolThreadSize;
        this.taskQueue=new LinkedBlockingQueue<Runnable>(taskQueueSize);
        this.workThreadSet=new HashSet<WorkThread>();
        this.suyeThreadPoolState=SuyeThreadPoolState.getInstance();
        this.threadRepository=ThreadRepository.newInstance();
    }

    public void execute(Runnable command) {
        if (command==null){
            throw new NullPointerException();
        }
        int poolThreadSize=suyeThreadPoolState.getWorkThreadSize();

        if (poolThreadSize<bestPoolThreadSize){
            if (addWorkThread(command)){
                return;
            }
        }else if (taskQueue.offer(command)){
            System.out.println("插入任务队列，不创建新的线程");
            return;
        }else if (!taskQueue.offer(command) && (poolThreadSize<theMostPoolThreadSize)){
            if (addWorkThread(command)){
                System.out.println("任务队列已满，且线程池中工作者线程数量小于最大数量，则创建新的线程");
                return;
            }
        }else {
            //执行拒绝策略
            System.out.println("拒绝该任务");
            return;
        }
    }

    private boolean addWorkThread(Runnable firstTask){
        retry:
        for (;;){
            /**
             * 如果线程池的状态大于Stop或者任务队列为null
             * 又或者线程第一个任务为null，那么就拒绝创建新的线程
             * 又或者工作者线程数量大于最大工作者线程数量
             */
            int poolState=suyeThreadPoolState.getPoolState();
            int poolThreadSize=suyeThreadPoolState.getWorkThreadSize();
            System.out.println("poolThreadSize:"+poolThreadSize);
            System.out.println("poolState:"+poolState);
            if (taskQueue.isEmpty() && firstTask==null){
               return false;
            }else if (poolState>=suyeThreadPoolState.StopState()){
                return false;
            }else if(poolThreadSize>=theMostPoolThreadSize){
                return false;
            }else {
                break retry;
            }
        }
        WorkThread workThread=new WorkThread(firstTask,threadRepository,this);
        if (workThread!=null){
            int poolThreadSize=suyeThreadPoolState.getWorkThreadSize();
            int poolState=suyeThreadPoolState.getPoolState();
            final Thread thread=workThread.getThread();
            this.mainLock.lock();
            if (thread.isAlive()){
                throw new IllegalStateException();
            }
            workThreadSet.add(workThread);
            workThreadSize=suyeThreadPoolState.increasePoolThreadSize();
            this.mainLock.unlock();
            thread.start();
        }
        return true;
    }

    public Runnable getTask(){
        while (true){
            int poolState=suyeThreadPoolState.getPoolState();
            if (poolState>=suyeThreadPoolState.StopState() || taskQueue.isEmpty()){
                //减少线程池的线程数量方法
                return null;
            }
            try {
                Runnable task=taskQueue.take();
                if (task!=null){
                    System.out.println("从任务队列中取得线程");
                    return task;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public int getWorkThreadSize() {
        return workThreadSize;
    }
}
