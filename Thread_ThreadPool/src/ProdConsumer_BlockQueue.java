import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/*
* 多线程   情况下不推荐使用  i++    ++i
* volatile
* */
class MyResource{
    private volatile boolean FLAG = true;  //默认开启  进行生产加消费
    private AtomicInteger atomicInteger = new AtomicInteger();

    BlockingQueue<String> blockingQueue = null;

    public MyResource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        //打印所传对象名   方便问题排查
        System.out.println(blockingQueue.getClass().getName());
    }

    public void myProd() throws Exception {
        String data = null;
        boolean retValue;
        while (FLAG){
            data = atomicInteger.incrementAndGet()+"";
            retValue = blockingQueue.offer(data,2L, TimeUnit.SECONDS);
            if(retValue){
                System.out.println(Thread.currentThread().getName() + "\t插入队列" + data + "成功");
            }else {
                System.out.println(Thread.currentThread().getName() + "\t 插入队列" + data + "失败");
            }
                TimeUnit.MILLISECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName() + "\t 停 flag=false生产结束");
    }

    public void myConsumer() throws Exception {
        String result = null;
        while (FLAG){
            result = blockingQueue.poll(2L,TimeUnit.MILLISECONDS);
            if(null == result || result.equalsIgnoreCase("")){
                FLAG = false;
                System.out.println(Thread.currentThread().getName() + "\t"+"超过2秒未成功消费  消费退出");
                return;
            }
            System.out.println(Thread.currentThread().getName() + "\t  消费队列" + result + "成功");
        }
    }

    public void stop(){
        this.FLAG = false;
    }
}


public class ProdConsumer_BlockQueue {

    public static void main(String[] args) {
        MyResource myResource = new MyResource(new ArrayBlockingQueue<>(10));

        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getName() + "\t 生产线程启动");
                myResource.myProd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"PROD").start();

        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getName() + "\t 消费线程启动");
                myResource.myConsumer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"CONSUMER").start();

        try {
            TimeUnit.MILLISECONDS.sleep(8);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("=======================================");
        System.out.println("5秒时间后main线程终止操作");
        myResource.stop();
    }
}
