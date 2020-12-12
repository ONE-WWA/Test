
/*
* 死锁两个或多个线程执行过程中
* 因争夺资源造成的一种互相等待的现象
* 若无外力干涉 那他们都无法推进下去
*
* jps命令定额我i进程号 jsp -l
* jstack 找到死锁产看  jstack xx
* */

import java.util.concurrent.TimeUnit;

class HoldLockThread implements Runnable{
private String lockA;
private String lockB;

    public HoldLockThread(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;

    }

    @Override
    public void run() {
        synchronized (lockA){
            System.out.println(Thread.currentThread().getName() + "\t 持有：" + lockA + "\t 尝试获得" + lockB);
            try { TimeUnit.MILLISECONDS.sleep(2); }catch (Exception e){ e.printStackTrace(); }
            synchronized (lockB){
                System.out.println(Thread.currentThread().getName() + "\t 持有：" + lockB + "\t 尝试获得" + lockA);

            }
        }
    }
}

public class DeadLock {
    public static void main(String[] args) {
        String lockA = "lockA";
        String lockB = "lockB";

       new Thread(new HoldLockThread(lockA,lockB),"AAA").start();
       new Thread(new HoldLockThread(lockB,lockA),"BBB").start();
    }
}
