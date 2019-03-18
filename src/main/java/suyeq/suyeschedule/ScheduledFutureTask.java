package suyeq.suyeschedule;

import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-17
 * @time: 16:26
 */
public class ScheduledFutureTask<V> extends FutureTask<V> implements RunnableScheduledFuture<V> {




    public ScheduledFutureTask(Callable<V> callable) {
        super(callable);
    }


    @Override
    public boolean isPeriodic() {
        return false;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return 0;
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}
