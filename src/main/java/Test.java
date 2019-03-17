import suyeq.RejectionStrategy;
import suyeq.SuyeThreadPool;

import java.util.Iterator;
import java.util.List;
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



    static class Task1 implements Runnable{
        public void run() {
            int n=10;
            while (n>0){
                System.out.println("线程为："+Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000);
                        n--;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    static class Task3 implements Runnable{
        public void run() {
            synchronized (Object.class){
                try {
                    wait();
                    System.out.println("被解放了");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    static class Task4 implements Runnable{


        public void run() {
            synchronized (Object.class){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("来解放你了");
                notify();
            }
        }
    }





    public static void main(String []args){
//        SuyeThreadPool pool=new SuyeThreadPool(2,4, RejectionStrategy.ABANDONED);
//        pool.execute(new Task());
//        pool.execute(new Task1());
//        pool.execute(new Task1());
//        List<Thread> list=pool.get();
//        Thread thread=list.get(1);
//        System.out.println("dasdasda"+thread.getName());
//        thread.interrupt();
        new Thread(new Task3()).start();
        new Thread(new Task4()).start();
    }
}
