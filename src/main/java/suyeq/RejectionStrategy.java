package suyeq;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-10
 * @time: 15:39
 */
public class RejectionStrategy {

    /**
     * 拒绝掉任务，并抛出异常(默认)
     */
    public final static int ABANDONED=1;

    /**
     * 由调用者线程执行该任务
     */
    public final static int CALLER=2;

    private static void abandoned() {
        try {
            throw new OutOfTaskQueueException();
        } catch (OutOfTaskQueueException e) {
            e.printStackTrace();
        }
    }

    private static void caller(Runnable task){
        task.run();
    }

    public static void rejectStrategy(Runnable task,int rejectChoose){
        if (rejectChoose==ABANDONED){
            abandoned();
        }else if (rejectChoose==CALLER){
            caller(task);
        }
    }





}
