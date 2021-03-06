package suyeq.suyeschedule;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-18
 * @time: 9:03
 */
public class DelayWorkQueue implements BlockingQueue<Runnable> {

    private volatile int size=0;

    private BlockingQueue<ScheduledFutureTask> taskqueue=new PriorityBlockingQueue<ScheduledFutureTask>();

    private Lock lock=new ReentrantLock();

    private Condition notice=lock.newCondition();


    @Override
    public boolean add(Runnable e) {
        return offer(e);
    }

    @Override
    public boolean offer(Runnable e) {
        if (e==null){
            throw new NullPointerException();
        }
        ScheduledFutureTask task= (ScheduledFutureTask) e;
        taskqueue.offer(task);
        lock.lock();
        size++;
        lock.unlock();
        return true;
    }

    /**
     * 锁必须尽可能的小
     * 否则会影响到定时的精度
     * @return
     * @throws InterruptedException
     */
    @Override
    public Runnable take() throws InterruptedException {
        try{
            while(true){
                lock.lock();
                Runnable task=taskqueue.peek();
                if (task==null){
                    System.out.println("取得任务失败，优先队列为null");
                    return null;
                }
                long delay=((ScheduledFutureTask) task).getDelay(TimeUnit.NANOSECONDS);
                if (delay<=0){
                    return finishTake((ScheduledFutureTask)task);
                }
                lock.unlock();
            }
        }finally {
            lock.unlock();
        }
    }

    /**
     * 判断是否需要周期执行
     * 是，则将定时重置，取出任务再次加入优先队列重新排序
     * 否则直接返回
     * @param task
     * @return
     * @throws InterruptedException
     */
    public Runnable finishTake(ScheduledFutureTask task) throws InterruptedException {
        if (task.isPeriodic()){
            task.calculateNextDelay();
            Runnable newTask=taskqueue.take();
            System.out.println(((ScheduledFutureTask) newTask).getDelay(TimeUnit.SECONDS));
            taskqueue.offer((ScheduledFutureTask)newTask);
            return newTask;
        }
        size--;
        return taskqueue.take();
    }


    @Override
    public Runnable remove() {
        return null;
    }

    @Override
    public Runnable poll() {
        return null;
    }

    @Override
    public Runnable element() {
        return null;
    }

    @Override
    public Runnable peek() {
        return null;
    }

    @Override
    public void put(Runnable e) throws InterruptedException {
        offer(e);
    }

    @Override
    public boolean offer(Runnable e, long timeout, TimeUnit unit) throws InterruptedException {
        return offer(e);
    }


    @Override
    public Runnable poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public int remainingCapacity() {
        return 0;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Runnable> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return taskqueue.size();
    }

    @Override
    public boolean isEmpty() {
        return size==0 ;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Runnable> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public int drainTo(Collection<? super Runnable> c) {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super Runnable> c, int maxElements) {
        return 0;
    }
}
