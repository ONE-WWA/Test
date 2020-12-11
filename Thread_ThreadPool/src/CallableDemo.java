import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/*class Thread_one implements Runnable{

    @Override
    public void run() {

    }
}*/

/*
* 返回值(类型自定义)  通常用来返回 标识发生错误的线程
* */
class Thread_Two implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("COME IN Callable");
        return 10010;
    }
}

//构造注入   传接口
//为什么 有runable  还需要  callable
//面向接口编程
public class CallableDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new Thread_Two());
        Thread thread = new Thread(futureTask, "AA");
        thread.start();

       /* int resultq = 100;
        int resultw = futureTask.get();*/
        //System.out.println("result: "+(resultq + resultw));

        //获得Callable线程的计算结果 如果没有计算完成就会强求 会导致阻塞 直到计算完成
        //如无必要 建议放在最后

        //System.out.println("result: "+futureTask.get());

        int resultq = 100;
        int resultw = 0;
        while (!futureTask.isDone()){
            resultw = futureTask.get();

        }
        System.out.println("result: "+(resultq + resultw));
    }
}
