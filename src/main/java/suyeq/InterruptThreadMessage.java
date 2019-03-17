package suyeq;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: Suyeq
 * @date: 2019-03-17
 * @time: 13:51
 * 储存需要被中断的线程的信息
 * 以及中断线程的方法
 */
public class InterruptThreadMessage {

    private Thread thread;

    private Thread.State state;

    private String threadName;

    public InterruptThreadMessage(Thread thread, Thread.State state,String threadName){
        this.thread=thread;
        this.state=state;
        this.threadName=threadName;
    }

    /**
     * 设置线程中断
     */
    public void setThreadInterupte(){
        thread.interrupt();
    }

    public String getThreadName() {
        return threadName;
    }

    public Thread.State getState() {
        return state;
    }
}
