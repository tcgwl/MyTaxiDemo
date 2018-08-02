package com.thunderhou.mytaxi.common.databus;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxBus {
    private static final String TAG = "RxBus";
    private static volatile RxBus instance;
    // 订阅者集合
    private Set<Object> subscribers;

    /**
     * 注册 DataBusSubscriber
     */
    public synchronized void register(Object subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * 注销 DataBusSubscriber
     */
    public synchronized void unRegister(Object subscriber) {
        subscribers.remove(subscriber);
    }

    private RxBus() {
        subscribers = new CopyOnWriteArraySet<>();
    }

    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }

        return instance;
    }

    /**
     * 包装处理过程
     */
    public void chainProcess(Func1 func) {
        Observable.just("")
                .subscribeOn(Schedulers.io()) // 指定处理过程在 IO 线程
                .map(func)   // 包装处理过程
                .observeOn(AndroidSchedulers.mainThread())  // 指定事件消费在 Main 线程
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object data) {
                        Log.d(TAG, "chainProcess start");
                        if (data == null)
                            return;

                        send(data);
                    }
                });
    }

    public void send(Object data) {
        for (Object subscriber : subscribers) {
            // 扫描注解，将数据发送到注册的对象的标记方法
            callMethodByAnnotiation(subscriber, data);
        }
    }

    /**
     * 反射获取对象方法列表，判断：
     * 1 是否被注解修饰
     * 2 参数类型是否和 data 类型一致
     */
    private void callMethodByAnnotiation(Object target, Object data) {
        Method[] methodArray = target.getClass().getDeclaredMethods();
        for (int i = 0; i < methodArray.length; i++) {
            try {
                Method method = methodArray[i];
                if (method.isAnnotationPresent(RegisterBus.class)) {
                    // 被 @RegisterBus 修饰的方法
                    Class paramType = method.getParameterTypes()[0];
                    if (data.getClass().getName().equals(paramType.getName())) {
                        // 参数类型和 data 一样，调用此方法
                        method.invoke(target, new Object[]{data});
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
