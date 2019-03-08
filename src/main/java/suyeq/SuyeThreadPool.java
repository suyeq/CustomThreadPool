package suyeq;


import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
    private final HashSet<Thread> workThreadSet;
    /**
     * 保存线程池的状态
     */
    private final SuyeThreadPoolState suyeThreadPoolState;

    private final ThreadRepository threadRepository;

    public SuyeThreadPool(){
        this.bestPoolThreadSize=Runtime.getRuntime().availableProcessors();
        this.theMostPoolThreadSize=bestPoolThreadSize;
        this.taskQueue=new LinkedBlockingQueue<Runnable>(theMostPoolThreadSize*2);
        this.workThreadSet=new HashSet<Thread>();
        this.suyeThreadPoolState=SuyeThreadPoolState.getInstance();
        this.threadRepository=ThreadRepository.newInstance();
    }

    public SuyeThreadPool(int theMostPoolThreadSize){
        this.bestPoolThreadSize=Runtime.getRuntime().availableProcessors();
        this.theMostPoolThreadSize=theMostPoolThreadSize;
        this.taskQueue=new LinkedBlockingQueue<Runnable>(theMostPoolThreadSize*2);
        this.workThreadSet=new HashSet<Thread>();
        this.suyeThreadPoolState=SuyeThreadPoolState.getInstance();
        this.threadRepository=ThreadRepository.newInstance();
    }

    public void execute(Runnable runnable) {

    }
}
