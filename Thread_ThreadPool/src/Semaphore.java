import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/*
* 信号量主要用于两个目的，一个是用于多个共享资源互斥使用，另一个用于并发线程数的控制
*   走一个 进一个
* */
 class SemaphoreDemo
{
    public static void main(String[] args)
    {
        Semaphore semaphore = new Semaphore(5);

        for (int i = 1; i <=10 ; i++)
        {
            new Thread(()->{
                try
                {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"\t 抢到车位");
                    try
                    {
                        TimeUnit.MILLISECONDS.sleep(3);
                        System.out.println(Thread.currentThread().getName()+"\t 停车三秒离开车位");
                    }catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }finally
                {
                    semaphore.release();
                }
            },String.valueOf(i)).start();
        }
    }
}
