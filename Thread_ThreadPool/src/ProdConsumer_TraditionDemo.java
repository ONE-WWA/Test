
/*
* 一个初始为的变量 两个线程对其交替操作  一个加一 一个减一 循环五次
* 生产者  消费者
*
* 多线程企业级模板口诀:
* 高内聚 低耦合
* 线程     操作(方法)    资源类
* 判断     干活          唤醒通知
* 严防多线程情况下的虚假唤醒
*
* 多线程情况下  循环要用while   不能用if(可能发生虚假唤醒  wait方法官方文档要求)
* */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//资源类
class ShareData {
    private int num = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();


    public void increment() throws Exception {

        lock.lock();
        try {

            //判断
            while (num != 0)
            {
                // 等待 不能生产
                condition.await();
            }
            //干活
            num++;
            System.out.println(Thread.currentThread().getName()+"\t"+num);
            //通知唤醒
            condition.signalAll();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }

    public void decrement() throws Exception {

        lock.lock();
        try {

            //判断
            while (num == 0)
            {
                // 等待 不能生产
                condition.await();
            }
            //干活
            num--;
            System.out.println(Thread.currentThread().getName()+"\t"+num);
            //通知唤醒
            condition.signalAll();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }
}



public class ProdConsumer_TraditionDemo
{
    public static void main(String[] args)
    {
        ShareData shareData = new ShareData();
        new Thread(()->{
            for (int i = 1; i <=5 ; i++) {
                try {
                    shareData.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"AA").start();


        new Thread(()->{
            for (int i = 1; i <=5 ; i++) {
                try {
                    shareData.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"BB").start();
    }


}

