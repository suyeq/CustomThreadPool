package suyeq;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-07
 * @time: 19:54
 */
public interface Executor {
    /**
     * 向线程池提交任务
     * @param task
     */
    void execute(Runnable task);
}
