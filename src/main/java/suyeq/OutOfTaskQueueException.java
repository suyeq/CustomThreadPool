package suyeq;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-10
 * @time: 15:50
 */
public class OutOfTaskQueueException extends Exception {

    private final String excptionMessage="拒绝执行该任务，可能任务队列已满或者线程达到线程池的最大数，又或者线程池终止";

    @Override
    public String toString(){
        return excptionMessage;
    }
}
