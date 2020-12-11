
/*
* 线程池的优势
* 线程池的主要工作是控制线程的数量 处理过程中将任务放入队列  然后在线程创建后启动这些任务
* 如果线程数量超过了最大数量 超出线程的数量排队等候 等其它线程执行完毕 再从对队列中取出任务来执行
*
* 特点
* 线程复用  控制最大并发数  管理线程
* 降低资源消耗 通过重复利用已经创建的线程降低线程的创建和销毁造成的消耗
* 提高相应速度 当任务到达时 任务可以不需要等到线程创建就能立即执行
* 提高线程的可管理性 线程是稀缺资源 如果无限制的创建 不仅会消耗系统资源 还会降低系统的稳定性
* 使用线程池可以进行统一的分配 调优和监控
*
*
* 线程池七大参数
*
* int corePoolSize                      线程池常驻核心线程数
* int maximumPoolSize                   线程池能够容纳同时执行的最大线程数 此值必须大于一
* long keepAliveTime                    多余的空闲线程存活时间  当线程超过corePoolSize时 当空闲时间达到keepAliveTime值时
*                                       多余的空闲线程会被销毁知道剩下 corePoolSize 个线程数为止
* TimeUnit unit                         keepAliveTime的单位
* BlockingQueue<Runnable> workQueue     任务队列 被提交但尚未执行的任务
* ThreadFactory threadFactory           表示生成线程池中工作线程的线程工厂 用于创建线程  一般用默认即可
* RejectedExecutionHandler handler      拒绝策略 表示当队列满了并且工作线程大于等于线程池的最大线程数 maximumPoolSize 时
*                                       如何来拒绝请求执行的runable的策略
* 拒绝策略：
*       AbortPolicy 超出最大线程数加等待队列数 直接报错(java.util.concurrent.RejectedExecutionException)
        CallerRunsPolicy 回退给调用者线程
        DiscardOldestPolicy  抛弃等待时间最久的 然后把当前任务加入队列尝试再次提交
        DiscardPolicy  直接丢弃多出的任务

* 推荐创建线程方式
* 【强制】线程池不循序使用Executors创建 通过ThreadPoolExecutor的方式这样的方式会明确线程的运行规则 规避资源耗尽风险
* Executor返回的线程池对象弊端如下
* FixedThreadPool和SingleThreadPool：
* 允许请求长度为Integer.MAX_VALUE 可能会堆积大量的请求 导致 oom
* CachedThreadPool和ScheduledThreadPool
* 允许创建线程数为Integer.MAX_VALUE 可能会创建大量的线程 导致 oom
*
* 合理配置线程池  cpu密集型
*               io密集型
*                   cpu密集型  任务需要大量运算 cpu一直全速运行   指 多核cpu
*                   参考公式    线程数量 cpu核数 +1
*                   io密集型   密集型任务线程并不是一直执行  cpu核数*2
*
*                             io密集型  即任务需要大量的io 即大量的阻塞
*                             在单线程上运行io密集型的任务会导致浪费大量的cpu运算能力在等待
*                             所以在io密集型任务中使用多线程可以大大的加速程序的运行
*                             即使在单核cpu上这种加速主要就是利用了被浪费的阻塞时间
*                               io密集型大部分时间都阻塞 故需要多配置线程数
*                     参考公式  cpu核数/1-阻塞系数     阻塞系数 0.8 - 0.9 之间
*                               如8核数cpu   8/1-0.9 = 80 个线程数
*
* * */

import java.util.concurrent.*;

public class ThreadPool {
    public static void main(String[] args) {

        //System.out.println(Runtime.getRuntime().availableProcessors());

        /*创建定长的线程池 可控制线程最大并发数 超出的线程会 在队列中等待
        newFixedThreadPool创建的线程池corePoolSize和maximumPoolSize值是相等的  它使用LinkedBlockingQueue
        ExecutorService threadPool = Executors.newFixedThreadPool(5);*/

       /* 创建一个单线程的线程池 它只会用唯一的工作线程来执行任务 保证所有任务按照指定顺序执行
        newSingleThreadExecutor将corePoolSize和maximumPoolSize都设置为1 它使用LinkedBlockingQueue
        ExecutorService threadPool = Executors.newSingleThreadExecutor();*/

       /* 创建一个可缓存线程池 如果线程池长度超过处理需要 可灵活回收空闲线程 若无可回收 则新建线程
        newCacheThreadPool将corePoolSize设置为0 将maximumPoolSize设置为integer.MAX_VALUE 使用的
        SynchronousQueue 也就是说来了任务就创建线程运行 当线程空闲超过60秒 就销毁线程*/
        //ExecutorService threadPool = Executors.newCachedThreadPool();
        //模拟10个用户来办理业务 每一个用户就是一个来自外部的请求线程




        //自定义线程池
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2,
                                                                5,
                                                                1L,
                                                                TimeUnit.MILLISECONDS,
                                                                new LinkedBlockingQueue<>(3),
                                                                Executors.defaultThreadFactory(),
                                                                new ThreadPoolExecutor.AbortPolicy());

        try {

            for (int i = 1; i <=9 ; i++) {
                poolExecutor.execute(()->{
                    System.out.println(Thread.currentThread().getName() + "\t begin");
                });
               /* try {
                    TimeUnit.MILLISECONDS.sleep(100);
                }catch (Exception e){
                    e.printStackTrace();
                }*/
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            poolExecutor.shutdown();
        }

    }

    private static void ThreadPoolinit(ExecutorService threadPool) {
        try {

            for (int i = 1; i <=10 ; i++) {
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName() + "\t begin");
                });
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPool.shutdown();
        }
    }
}
