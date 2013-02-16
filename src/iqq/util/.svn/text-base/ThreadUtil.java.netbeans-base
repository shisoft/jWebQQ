package iqq.util;

/*
 * 创建日期 2006-4-22 项目名称 Library.Core 所在包名 jp.kernel.newage.lib.util.concurrent
 * 版权所有 (C) 1996-2006 困了
 */
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/**
 * 线程管理模块
 *
 * @author chen
 * @version 1.0.0
 */
public abstract class ThreadUtil implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 服务是否已经开始
     */
    protected static boolean blnStarted;
    /**
     * 默认的线程执行服务
     */
    protected static ExecutorService etsContainer;
    /**
     * 默认的线程工厂
     */
    protected static ThreadFactory tdfContainer;

    /**
     * 获取默认的线程执行服务
     *
     * @return 默认的线程执行服务 - Executors.newCachedThreadPool(getThreadFactory())
     */
    protected static synchronized ExecutorService getExecutorService() {
        return null != etsContainer ? etsContainer : (etsContainer = Executors.newFixedThreadPool(50,getThreadFactory()));
    }

    /**
     * 获取默认的线程工厂
     *
     * @return 默认的线程工厂 - Executors.defaultThreadFactory()
     */
    protected static synchronized ThreadFactory getThreadFactory() {
        return null != tdfContainer ? tdfContainer : (tdfContainer = Executors.defaultThreadFactory());
    }

    /**
     * 获取一个新的线程
     *
     * @param r 要新建的线程 线程执行类
     * @return 新的线程-newThread
     */
    public static Thread newThread(Runnable r) {
        return getThreadFactory().newThread(r);
    }

    /**
     * 停止服务
     */
    public static synchronized void shutdown() {
        if (blnStarted) {
            try {
                getExecutorService().shutdown();
            } finally {
                etsContainer = null;
                blnStarted = false;
            }
        }
    }

    /**
     * 执行并提交一个线程任务
     *
     * @param r 要运行的线程 线程执行类
     * @return f 该线程的Future返回
     */
    public static synchronized Future<?> submit(Runnable r) {
        //getThreadFactory().newThread(r).start();
        Future<?> f = null;
        try {
            f = getExecutorService().submit(r);
        } finally {
            blnStarted = true;
        }
        return f;
    }
}