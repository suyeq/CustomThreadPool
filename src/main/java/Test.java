import suyeq.RejectionStrategy;
import suyeq.SuyeThreadPool;

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
                while ((n--)>0){
                    System.out.println("线程为："+Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }
    }



    public static void main(String []args){
        SuyeThreadPool pool=new SuyeThreadPool(6,4, RejectionStrategy.ABANDONED);
        pool.execute(new Task());
//        pool.shutdown();
//        System.out.println(pool.isShutdown());
//        Future<String> future=pool.submit(new Task(),"成功返回啦");
//        try {
//            System.out.println("返回结果为："+future.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        pool.shutdown();
        pool.execute(new Task());
//        pool.execute(new Task());
//        pool.execute(new Task());
//        pool.execute(new Task());
//        pool.execute(new Task());
//        pool.execute(new Task());
//        pool.execute(new Task());
//        pool.execute(new Task());
//        pool.execute(new Task());
//        pool.execute(new Task());
//        pool.execute(new Task());
//        pool.execute(new Task());
//        pool.execute(new Task());
//        pool.execute(new Task());
        System.out.println(pool.getWorkThreadSize());
    }
}
