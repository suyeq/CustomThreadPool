import suyeq.SuyeThreadPool;
import suyeq.SuyeThreadPoolState;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-07
 * @time: 21:28
 */
public class Test {

    @org.junit.Test
    public void test(){
        //********...00:RUN
        //********...01:STOP
        //********...10:DESTROY
        //取状态：c&00000.....11
        //取数量：c&11111.....00(MAX-3)
        int run=Integer.MAX_VALUE-3;
        int stop=Integer.MAX_VALUE-2;
        int destroy=Integer.MAX_VALUE-1;
        int poolState=(1<<2)-1;
        int poolThreadSize=Integer.MAX_VALUE-3;
        System.out.println(Integer.toBinaryString(run));
        System.out.println(Integer.toBinaryString(stop));
        System.out.println(Integer.toBinaryString(destroy));
        System.out.println("状态为："+Integer.toBinaryString(run&poolState));
        System.out.println("数量为："+Integer.toBinaryString((run&poolThreadSize)>>2));
        int a=(run&poolThreadSize)>>2;
        System.out.println("合并为："+Integer.toBinaryString(run|(a<<2)));

    }
    @org.junit.Test
    public void test1(){
        SuyeThreadPoolState state=SuyeThreadPoolState.getInstance();
        System.out.println(Integer.toBinaryString(state.getPoolSizeAndState()));
        System.out.println("------------");
        System.out.println(state.setPoolStateToStop());
        System.out.println(Integer.toBinaryString(state.getPoolSizeAndState()));
        System.out.println("------------");
        System.out.println(state.setPoolStateToDestroy());
        System.out.println(Integer.toBinaryString(state.getPoolSizeAndState()));
        System.out.println("------------");
        System.out.println(Integer.toBinaryString(state.getWorkThreadSize()));
        System.out.println("------------");
        System.out.println(Integer.toBinaryString(state.getPoolState()));
        System.out.println("------------");
        System.out.println(state.increasePoolThreadSize());
        System.out.println(Integer.toBinaryString(state.getPoolSizeAndState()));
        System.out.println("------------");
    }
    @org.junit.Test
    public void test3() throws InterruptedException {
        BlockingQueue<String> queue=new LinkedBlockingQueue<String>();
        queue.take();
        System.out.println("alalla");
    }
}
