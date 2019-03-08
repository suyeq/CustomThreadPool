package suyeq;

import java.util.concurrent.ThreadFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-08
 * @time: 10:35
 */
public class ThreadRepository implements ThreadFactory {

    private static ThreadRepository repository=null;

    public static ThreadRepository newInstance(){
        synchronized (ThreadRepository.class){
            if (repository==null){
                repository=new ThreadRepository();
                return repository;
            }
            return repository;
        }
    }

    public Thread newThread(Runnable r) {
        return new Thread(r);
    }

    public Thread newThread(Runnable r,String name){
        return new Thread(r,name);
    }

}
