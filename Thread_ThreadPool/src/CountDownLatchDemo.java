import java.util.concurrent.CountDownLatch;

/*
* CountDownLatch 倒计数 结束后执行
*
* 让一些线程阻塞直到另一个线程完成一系列操作后才会被唤醒
*
* CountDownLatch主要有两个方法，当一个或多个线程调用await方法时，调用线程会被阻塞。
* 其他线程调用countDown方法会将计数器 减一 （调用countDown方法的线程不会阻塞）
* 当计数器的值变为零时 因调用await方法被阻塞的线程会被唤醒  继续执行
 * */
class countDownLatchDemo
{

    public static void main(String[] args) throws InterruptedException
    {


        CountDownLatch countDownLatch = new CountDownLatch(6);

        //保证循环中的线程执行完成后   main主线程最后执行
        for (int i = 1; i <= 6; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"\t---国被灭");
                //每次执行完   -1
                countDownLatch.countDown();
            },CountryEnum.forEach_CountryEnum(i).getRetMessage()).start();
        }
        //为 0时向下执行
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName()+"\t---秦帝国一统华夏");
    }


    /*public static void main(String[] args) throws InterruptedException {


        CountDownLatch countDownLatch = new CountDownLatch(5);

        //保证循环中的线程执行完成后   main主线程最后执行
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"\t上完自习离开教室");
                //每次执行完   -1
                countDownLatch.countDown();
            },String.valueOf(i)).start();
        }
        //为 0时向下执行
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName()+"\t___________班长最后走人");
    }*/
}
