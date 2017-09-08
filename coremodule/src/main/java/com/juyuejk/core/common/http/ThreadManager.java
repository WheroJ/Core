package com.juyuejk.core.common.http;

import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Pattern;

/**
 * 线程池的管理,单例设计
 *
 * @author shopping
 */
public class ThreadManager {

    private static final String TAG = "core.ThreadManager";

    public ThreadPoolExecutor getLongExecutor() {
        return longExecutor;
    }

    private ThreadPoolExecutor longExecutor; // 耗时比较长的线程   用来请求网络
    private ExecutorService shortExecutor; // 比较短的线程    用来加载本地数据

    private static final ThreadManager instance = new ThreadManager();

    private ThreadManager() {
        int num = Runtime.getRuntime().availableProcessors();

//		开启线程池 执行异步任务
//		corePoolSize： 线程池维护线程的最少数量
//		maximumPoolSize：线程池维护线程的最大数量
//		keepAliveTime： 线程池维护线程所允许的空闲时间
//		unit： 线程池维护线程所允许的空闲时间的单位
//		workQueue： 线程池所使用的缓冲队列
//		handler： 线程池对拒绝任务的处理策略

//      当线程池中的线程数量大于 corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止。这样，线程池可以动态的调整池中的线程数。
        // 最优线程数量：核数*2 + 1
        longExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2*num + 1);
        shortExecutor = Executors.newSingleThreadExecutor();
    }

    public static ThreadManager getInstance() {
        return instance;
    }

    /**
     * 执行异步任务
     *
     * @param runnable
     */
    public void executeLongTask(Runnable runnable) {
        longExecutor.execute(runnable);// 用线程池执行代码
    }

    public void executeShortTask(Runnable runnable) {
        shortExecutor.execute(runnable);// 用线程池执行代码
    }

    /**
     * 从线程池中移除runnable任务
     * @param runnable
     */
    public boolean remove(Runnable runnable) {
//        try {
            if (longExecutor != null && !longExecutor.isShutdown()) {
                boolean remove = longExecutor.remove(runnable);
                return remove;// 停止任务
            } else {
                return false;
            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
    }

    //CPU个数
    private int getNumCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if(Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            Log.d(TAG, "CPU Count: " + files.length);
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch(Exception e) {
            //Print exception
            Log.d(TAG, "CPU Count: Failed.");
            e.printStackTrace();
            //Default to return 1 core
            return 1;
        }
    }
}
