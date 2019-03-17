import suyeq.InterruptThreadMessage;
import suyeq.RejectionStrategy;
import suyeq.SuyeThreadPool;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

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
                    System.out.println("线程为："+Thread.currentThread().getName());
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
//                    if (Thread.currentThread().isInterrupted()){
//                        break;
//                    }
                }
        }
    }



    static class Task1 implements Runnable{
        public void run() {
            int n=10;
            while (n>0){
//                System.out.println("线程为："+Thread.currentThread().getName());
//                    try {
//                        Thread.sleep(1000);
//                        n--;
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
            }
        }
    }

//    static class Task3 implements Runnable{
//        Object object;
//        public Task3(Object o){
//            this.object=o;
//        }
//        public void run() {
//            synchronized (object){
//                try {
//                    object.wait();
//                    System.out.println("被解放了");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//    }
//
//    static class Task4 implements Runnable{
//        Object object;
//        public Task4(Object o){
//            this.object=o;
//        }
//
//        public void run() {
//            synchronized (object){
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("来解放你了");
//                object.notify();
//            }
//        }
//    }





    public static void main(String []args) throws InterruptedException {
        SuyeThreadPool pool=new SuyeThreadPool(2,4, RejectionStrategy.ABANDONED);
        pool.execute(new Task());
        pool.execute(new Task1());
        pool.execute(new Task1());
        Thread.sleep(5000);
        List<InterruptThreadMessage> list=pool.getMaybeNeedInterruptThread();
        list.get(0).setThreadInter();
        System.out.println("fsddfsdf"+list.get(0).getThreadName());
//        System.out.println();
//        Thread thread=list.get(0);
//        System.out.println("dasdasda"+thread.getName());
//        thread.interrupt();
//        Object object=new Object();
//        new Thread(new Task3(object)).start();
//        new Thread(new Task4(object)).start();

    }
}
