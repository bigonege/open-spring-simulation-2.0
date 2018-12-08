package org.springframework.beans.factory;

import sun.net.idn.Punycode;

/**
 * @Auther: Wang Ky
 * @Date: 2018/12/8 14:00
 * @Description:
 */
//用于做事件监听
public class BeanPostProcessor {
    //为在Bean的初始化前提供回调入口
    public Object postProcessBeforeInitialization(Object bean, String beanName){
        return bean;
    }

    //为在Bean的初始化之后提供回调入口
    public Object postProcessAfterInitialization(Object bean, String beanName){
        return bean;
    }
}
