import suyeq.InterruptThreadMessage;
import suyeq.RejectionStrategy;
import suyeq.SuyeThreadPool;
import suyeq.suyeschedule.SuyeScheduleThreadPool;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-09
 * @time: 13:25
 */
public class Test {
    static class Task implements Runnable  {
        @Override
        public void run() {
                int n=10;
                while (n>0){
                    System.out.println("线程为："+Thread.currentThread().getName());

                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        break;
                        //e.printStackTrace();
                    }



//                    if (Thread.currentThread().isInterrupted()){
//                        break;
//                    }
                }
        }
    }



    static class Task1 implements Runnable{
        @Override
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





    public static void main(String []args) throws InterruptedException, TimeoutException, ExecutionException {
//        SuyeThreadPool pool=new SuyeThreadPool(2,4, RejectionStrategy.ABANDONED);
//        pool.execute(new Task());
//        pool.execute(new Task1());
//        pool.execute(new Task1());
//        Thread.sleep(5000);
//        List<InterruptThreadMessage> list=pool.getMaybeNeedInterruptThread();
//        list.get(0).setThreadInterupte();
//        System.out.println("fsddfsdf"+list.get(0).getThreadName());
//        System.out.println();
//        Thread thread=list.get(0);
//        System.out.println("dasdasda"+thread.getName());
//        thread.interrupt();
//        Object object=new Object();
//        new Thread(new Task3(object)).start();
//        new Thread(new Task4(object)).start();

//        FutureTask<String> futureTask=new FutureTask<String>(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                System.out.println("啦啦啦啦啦");
//                //Thread.sleep(5000);
//                return "执行了";
//            }
//        });
//        //new Thread(futureTask).start();
//        futureTask.get(6,TimeUnit.SECONDS);
//        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor=new ScheduledThreadPoolExecutor(1);
//        scheduledThreadPoolExecutor.schedule(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(Thread.currentThread().getName());
//                System.out.println("这里是1");
//            }
//        },10,TimeUnit.SECONDS);
//
//        scheduledThreadPoolExecutor.schedule(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(Thread.currentThread().getName());
//                System.out.println("这里是2");
//            }
//        },10,TimeUnit.SECONDS);
//        scheduledThreadPoolExecutor.shutdown();

        SuyeScheduleThreadPool pool=new SuyeScheduleThreadPool(1);
        pool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World!");
            }
        },3,TimeUnit.SECONDS);
       // ScheduledThreadPoolExecutor

    }
}
