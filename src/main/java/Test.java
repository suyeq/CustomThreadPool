import suyeq.RejectionStrategy;
import suyeq.SuyeThreadPool;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-09
 * @time: 13:25
 */
public class Test {
    static class Task implements Runnable{
        public void run() {
                int n=10;
                while (n>0){
                    //System.out.println("线程为："+Thread.currentThread().getName());
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    if (Thread.currentThread().isInterrupted()){
                        break;
                    }
                }
        }
    }



    public static void main(String []args){
        SuyeThreadPool pool=new SuyeThreadPool(1,4, RejectionStrategy.ABANDONED);
        pool.execute(new Task());
        pool.execute(new Task());
        Thread thread=pool.get();
        thread.interrupt();

    }
}
