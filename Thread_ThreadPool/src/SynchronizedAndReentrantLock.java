
/*
 * 原始构成
 * synchronized是关键字属于JVM层面
 * monitorenter(底层是通过monitor对象来完成的 其实wait/notify等方法也依赖于montitor对象只有在同步块或者方法中才能够调
 * wait/notify等方法)
 * Lock是具体类(java.util.concurrent.locks.lock)是api层面的锁
 *
 * synchronized是可重入锁  不会发生死锁问题  底层实现了 正常 和非正常 退出
 *
 * 使用方法
 * synchronized 不需要用户手动释放锁 当synchronized代码执行完后系统会自动让线程释放对锁的占用
 * ReentrantLock 则需要用户手动释放锁 若没有主动释放锁 就可能出现死锁现象
 * 需要lock() / unlock() 方法配合try/finally语句来完成
 *
 * 等待是否可中断
 * synchronized  不可中断  除非抛出异常或者正确执行完成
 * ReentrantLock 可中断   设置超时方法  tryLock(long timeout, TimeUnit unit)
 *                       lockInterruptibly()放入代码块中 调用interrupt()方法可中断
 *
 * 加锁是否公平
 * synchronized     非公平锁
 * ReentrantLock    两者都可以  默认非公平锁  构造方法传入 boolean值  true公平锁   false非公平锁
 *
 * 锁绑定多个条件Condition
 * synchronized     没有
 * ReentrantLock    用来实现分组唤醒需要唤醒的线程们    可以精确唤醒  而不是像synchronized 要么随机唤醒一个/全部唤醒
 * */

/*
*多线程之间按顺序调用 实现A->B->C三个线程启动
* AA打印5次 BB打印10次  CC打印15次
* then
* AA打印5次 BB打印10次  CC打印15次
* 。。。
* 循环10次
* */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareResource{
    private int num = 1;    // A:1  B:2  C:3
    private Lock lock = new ReentrantLock();
    private Condition c = lock.newCondition();
    private Condition cc = lock.newCondition();
    private Condition ccc = lock.newCondition();

    public void prints(){
        lock.lock();
        try {
            //判断
            while (num != 1) {
                c.await();

            }
                //干活
                for (int i = 1; i <=5 ; i++) {
                    System.out.println(Thread.currentThread().getName() + "\t" + i);
                }
                //通知
                num = 2;
                cc.signal();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void printss(){
        lock.lock();
        try {
            //判断
            while (num != 2) {
                cc.await();
            }
                //干活
                for (int i = 1; i <=10 ; i++) {
                    System.out.println(Thread.currentThread().getName() + "\t" + i);
                }
                //通知
                num = 3;
                ccc.signal();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void printsss(){
        lock.lock();
        try {
            //判断
            while (num != 3) {
                ccc.await();
            }
                //干活
                for (int i = 1; i <=15 ; i++) {
                    System.out.println(Thread.currentThread().getName() + "\t" + i);
                }
                //通知
                num = 1;
                c.signal();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    //
}

public class SynchronizedAndReentrantLock {
    public static void main(String[] args) {

        ShareResource shareResource = new ShareResource();
        new Thread(()->{
            for (int i = 1; i <=10 ; i++) {
                shareResource.prints();
            }
        },"AA").start();

        new Thread(()->{
            for (int i = 1; i <=10 ; i++) {
                shareResource.printss();
            }
        },"BB").start();

        new Thread(()->{
            for (int i = 1; i <=10 ; i++) {
                shareResource.printsss();
            }
        },"CC").start();
    }
}
