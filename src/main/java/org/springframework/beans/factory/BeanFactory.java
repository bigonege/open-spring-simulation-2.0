package org.springframework.beans.factory;

/**
 * @Auther: Wang Ky
 * @Date: 2018/12/8 11:04
 * @Description:
 */
public interface BeanFactory {
    //根据bean的名字，获取在IOC容器中得到bean实例
    Object getBean(String name);
}
