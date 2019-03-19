import suyeq.InterruptThreadMessage;
import suyeq.RejectionStrategy;
import suyeq.SuyeThreadPool;
import suyeq.suyeschedule.ScheduledFutureTask;
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
    public static void main(String []args) throws InterruptedException, TimeoutException, ExecutionException {


        /**
         *
         * 中断异常以及基础线程池测试
        SuyeThreadPool pool=new SuyeThreadPool(2,4, RejectionStrategy.ABANDONED);
        pool.execute(new Task());
        pool.execute(new Task1());
        pool.execute(new Task1());
        Thread.sleep(5000);
        List<InterruptThreadMessage> list=pool.getMaybeNeedInterruptThread();
        list.get(0).setThreadInterupte();
        System.out.println("fsddfsdf"+list.get(0).getThreadName());
        System.out.println();
         */

        /**
         * 定时任务测试
        SuyeScheduleThreadPool pool=new SuyeScheduleThreadPool(3);
        pool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World1!");
            }
        },10,TimeUnit.SECONDS);

        pool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World2!");
            }
        },8,TimeUnit.SECONDS);

        pool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World3!");
            }
        },3,TimeUnit.SECONDS);

        Future<String> future=pool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World4!");
            }
        },6,TimeUnit.SECONDS,"Hello");
        System.out.println(future.get());
         */

        SuyeScheduleThreadPool pool=new SuyeScheduleThreadPool(1);
        pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World1");
            }
        },3,TimeUnit.SECONDS);

        pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World2");
            }
        },5,TimeUnit.SECONDS);
    }
}
