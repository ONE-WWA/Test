import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/*
* 集齐七颗龙珠便可召唤神龙
* */
class CyclicBarrierDemo
{

    public static void main(String[] args)
    {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,()->{ System.out.println("---召唤神龙"); });

        for (int i = 1; i <= 7; i++) {
            final int tempint = i;
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"\t已收集："+tempint+"龙珠");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },String.valueOf(i)).start();
        }
    }
}
