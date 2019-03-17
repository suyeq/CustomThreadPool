package suyeq;


import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
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
public class SuyeThreadPool implements ExecutorService {

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
    /**
     * 工作者线程的数量
     */
    private volatile int workThreadSize;
    /**
     * 保存线程池的状态
     */
    private final SuyeThreadPoolState suyeThreadPoolState;

    private final ThreadRepository threadRepository;

    private final Lock mainLock=new ReentrantLock();

    private final Condition mainCondition=mainLock.newCondition();

    private int rejectStrategy;

    public SuyeThreadPool(){
        this.bestPoolThreadSize=Runtime.getRuntime().availableProcessors();
        this.theMostPoolThreadSize=bestPoolThreadSize;
        rejectStrategy=RejectionStrategy.ABANDONED;
        this.taskQueue=new LinkedBlockingQueue<Runnable>(theMostPoolThreadSize*2);
        this.workThreadSet=new HashSet<WorkThread>();
        this.suyeThreadPoolState=SuyeThreadPoolState.getInstance();
        this.threadRepository=ThreadRepository.newInstance();
    }

    public SuyeThreadPool(int theMostPoolThreadSize){
        this.bestPoolThreadSize=Runtime.getRuntime().availableProcessors();
        this.theMostPoolThreadSize=theMostPoolThreadSize;
        rejectStrategy=RejectionStrategy.ABANDONED;
        this.taskQueue=new LinkedBlockingQueue<Runnable>(theMostPoolThreadSize*2);
        this.workThreadSet=new HashSet<WorkThread>();
        this.suyeThreadPoolState=SuyeThreadPoolState.getInstance();
        this.threadRepository=ThreadRepository.newInstance();
    }

    public SuyeThreadPool(int theMostPoolThreadSize,int taskQueueSize,int rejectStrategy){
        this.bestPoolThreadSize=Runtime.getRuntime().availableProcessors();
        this.theMostPoolThreadSize=theMostPoolThreadSize;
        this.rejectStrategy=rejectStrategy;
        this.taskQueue=new LinkedBlockingQueue<Runnable>(taskQueueSize);
        this.workThreadSet=new HashSet<WorkThread>();
        this.suyeThreadPoolState=SuyeThreadPoolState.getInstance();
        this.threadRepository=ThreadRepository.newInstance();
    }

    /**
     * 提交任务
     * @param command
     */
    public void execute(Runnable command) {
        if (command==null){
            throw new NullPointerException();
        }
        int poolThreadSize=suyeThreadPoolState.getWorkThreadSize();
        int poolState=suyeThreadPoolState.getPoolState();
        int RunState=suyeThreadPoolState.RunState();
        if (poolThreadSize<bestPoolThreadSize){
            if (addWorkThread(command)){
                return;
            }
            poolState=suyeThreadPoolState.getPoolState();
        }
        if (poolState==RunState && taskQueue.offer(command)){
            System.out.println("插入任务队列，不创建新的线程");
            return;
        }
        poolState=suyeThreadPoolState.getPoolState();
        if (poolState==RunState && !taskQueue.offer(command) && (poolThreadSize<theMostPoolThreadSize)){
            if (addWorkThread(command)){
                System.out.println("任务队列已满，且线程池中工作者线程数量小于最大数量，则创建新的线程");
                return;
            }
        }
        //执行拒绝策略
        System.out.println("执行拒绝策略");
        RejectionStrategy.rejectStrategy(command,rejectStrategy);
        return;
    }

    /**
     * 增加工作者线程
     * @param firstTask
     * @return
     */
    private boolean addWorkThread(Runnable firstTask){
//        retry:
//        for (;;){
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
            }
//            else {
//                break retry;
//            }
//        }
        WorkThread workThread=new WorkThread(firstTask);
        if (workThread!=null){
            final Thread thread=workThread.thread;
            /**
             * 防止在其他线程中提交任务
             * 因而需要加锁
             */
            this.mainLock.lock();
            if (thread.isAlive()){
                throw new IllegalStateException();
            }
            workThreadSet.add(workThread);
            workThreadSize=suyeThreadPoolState.increasePoolThreadSize();
            this.mainLock.unlock();
            thread.start();
            if (!thread.isAlive()){
                return addWorkThread(firstTask);
            }
        }
        return true;
    }

    /**
     * 将已完成的任务的工作者线程
     * 从集合中去掉
     * @param workThread
     */
    private void reduceWorkThread(WorkThread workThread){
        mainLock.lock();
        workThreadSet.remove(workThread);
        workThreadSize--;
        mainLock.unlock();
    }

    /**
     * 从任务队列中获取任务
     * @return
     */
    private Runnable getTask(WorkThread workThread) throws InterruptedException{
        while (true){
            if (taskQueue.isEmpty()){
                reduceWorkThread(workThread);
                return null;
            }
            Runnable task=taskQueue.take();
            if (task!=null){
                System.out.println("从任务队列中取得线程");
                return task;
            }
        }
    }

    /**
     * 工作者线程运行
     * @param workThread
     * @throws InterruptedException
     */
    private void runWork(WorkThread workThread) throws InterruptedException {
        Thread thread=Thread.currentThread();
        Runnable task=workThread.firstTask;
        workThread.firstTask=null;
        while (task!=null || (task=getTask(workThread))!=null){
            workThread.lock.lock();
            System.out.println("任务执行中");
            task.run();
            System.out.println("任务执行完成");
            workThread.completeTask++;
            task=null;
            workThread.lock.unlock();
            if (thread.isInterrupted()){
                throw new InterruptedException();
            }
        }
    }

    /**
     * 终止线程池，让线程池不再接受新的任务
     * 但可以处理任务队列中的任务
     */
    public void shutdown() {
        suyeThreadPoolState.setPoolStateToStop();
    }

    /**
     * 判断线程池是否被终止
     * @return
     */
    public boolean isShutdown() {
        return suyeThreadPoolState.getPoolState() >= suyeThreadPoolState.StopState();
    }

    /**
     * 指定返回结果的提交方法
     * @param task
     * @param result
     * @param <T>
     * @return
     */
    public <T>Future<T> submit(final Runnable task, final T result) {
        FutureTask<T> future=new FutureTask<T>(new Callable<T>() {
            public T call() throws Exception {
                task.run();
                return result;
            }
        });
        execute(future);
        return future;
    }

    /**
     * 返回工作者线程数量
     * @return
     */
    public int getWorkThreadSize() {
        return workThreadSize;
    }

    /**
     * 封装的工作者线程
     */
    class WorkThread implements Runnable {

        private Lock lock;

        private Runnable firstTask;

        private Thread thread;

        private volatile int completeTask=0;

        public WorkThread(Runnable firstTask){
            this.firstTask=firstTask;
            thread=threadRepository.newThread(this);
            lock=new ReentrantLock();
        }

        public void run() {
            try {
                runWork(this);
            } catch (InterruptedException e) {
                System.out.println("中断线程了");
                e.printStackTrace();
            }finally {
                reduceWorkThread(this);
                System.out.println("处理中断啦");
                System.out.println("重连操作开始");
                return;
            }
        }

    }

    /**
     * 获取线程阻塞、限时等待、等待中的线程信息
     * 是个线程不安全的方法，建议少用主动中断
     * 但是如果你能确保线程在很长一段时间中会被阻塞或者等待的话
     * 那么就可以使用主动中断去让阻塞中的线程
     * 放弃当前任务，而进行下次任务
     * @return
     */
    public List<InterruptThreadMessage> getMaybeNeedInterruptThread(){
        List<InterruptThreadMessage> list=new LinkedList<InterruptThreadMessage>();
        mainLock.lock();
        Iterator iterator=workThreadSet.iterator();
        while (iterator.hasNext()){
            WorkThread workThread=(WorkThread)iterator.next();
            Thread.State state=workThread.thread.getState();
            if (state== Thread.State.TIMED_WAITING || state== Thread.State.WAITING || state== Thread.State.BLOCKED){
                list.add(new InterruptThreadMessage(workThread.thread,state,workThread.thread.getName()));
            }
        }
        mainLock.unlock();
        return list;
    }

}
