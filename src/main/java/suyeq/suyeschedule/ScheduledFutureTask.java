package suyeq.suyeschedule;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-17
 * @time: 16:26
 */
public class ScheduledFutureTask<V> extends FutureTask<V> implements RunnableScheduledFuture<V> {

    private long delayTimes;

    private final long periodic;

    private final long cycleTimes;

    private final TimeUnit unit;

    //private final int sortIndex;

    public ScheduledFutureTask(Runnable task,long ns,TimeUnit unit) {
        super(task,null);
        this.periodic=0;
        this.cycleTimes=0;
        this.unit=unit;
        this.delayTimes=TimeUnit.NANOSECONDS.convert(ns,unit)+now();
        //this.sortIndex=sortIndex;
    }

    public ScheduledFutureTask(Runnable task,long ns,long cycleTimes,TimeUnit unit) {
        super(task,null);
        this.periodic=1;
        this.cycleTimes=cycleTimes;
        this.unit=unit;
        this.delayTimes=TimeUnit.NANOSECONDS.convert(cycleTimes,unit)+now();
        //this.sortIndex=sortIndex;
    }

    public ScheduledFutureTask(Callable task,long ns,TimeUnit unit) {
        super(task);
        this.periodic=0;
        this.cycleTimes=0;
        this.unit=unit;
        this.delayTimes=TimeUnit.NANOSECONDS.convert(ns,unit)+now();
        //this.sortIndex=sortIndex;
    }

    @Override
    public void run(){
        boolean isPeriodic=isPeriodic();
        if (!isPeriodic){
            super.run();
        }else if (isPeriodic){
            //执行周期性任务
            super.runAndReset();
        }
    }

    /**
     * 判断有无周期性
     * @return
     */
    @Override
    public boolean isPeriodic() {
        return this.periodic!=0;
    }

    /**
     * 计算周期任务下次执行时间
     */
    public void calculateNextDelay(){
       this.delayTimes=now()+TimeUnit.NANOSECONDS.convert(cycleTimes,this.unit);
    }

    /**
     * 按纳秒的时间距离到达还剩余的时间
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delayTimes-now()+1000*1000,TimeUnit.NANOSECONDS);
    }

    /**
     * 返回0表示延迟相等
     * -1表示该延迟大于当前Future延迟
     * 1表示延迟小于
     * -2表示该延迟是自身或者
     * 传入的不是ScheduledFutureTask对象
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        if (o==this){
            return 0;
        }
        if (o instanceof ScheduledFuture){
            ScheduledFuture scheduledFuture=(ScheduledFuture)o;
            long timeSub=scheduledFuture.getDelay(TimeUnit.NANOSECONDS)-this.getDelay(TimeUnit.NANOSECONDS);
            if (timeSub>0){
                return -1;
            }else if (timeSub==0){
                return 0;
            }else {
                return 1;
            }
        }
        return -2;
    }

    /**
     * 以纳秒的方式返回当前时间
     * @return
     */
    public long now(){
        return System.nanoTime();
    }

    @Override
    public int hashCode(){
        int hash=5381;
        Random rand =new Random(Integer.MAX_VALUE);
        for (int i = 0; i < rand.nextInt(Integer.MAX_VALUE); ++i) {
            hash=((hash<<5)+hash)+ rand.nextInt(Integer.MAX_VALUE);
        }
        return hash;
    }

}
