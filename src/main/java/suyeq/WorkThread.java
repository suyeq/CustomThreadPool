package suyeq;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-08
 * @time: 10:30
 */
public class WorkThread implements Runnable {

    private Lock lock;

    private Runnable firstTask;

    private ThreadRepository threadRepository;

    private Thread thread;

    private SuyeThreadPool pool;

    public WorkThread(Runnable firstTask,ThreadRepository repository,SuyeThreadPool pool){
        this.firstTask=firstTask;
        this.threadRepository=repository;
        thread=threadRepository.newThread(this);
        this.pool=pool;
        lock=new ReentrantLock();
    }

    public void run() {
        runWork(this);
    }


    public void runWork(WorkThread workThread){
        Thread thread=Thread.currentThread();
        Runnable task=firstTask;
        firstTask=null;
    }

}
