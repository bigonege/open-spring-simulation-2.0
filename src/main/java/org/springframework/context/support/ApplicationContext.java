package org.springframework.context.support;

import com.sun.xml.internal.ws.util.StringUtils;
import org.springframework.beans.factory.BeanDefinition;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanPostProcessor;
import org.springframework.beans.factory.BeanWrapper;
import org.springframework.context.stereotype.Autowired;
import org.springframework.context.stereotype.Controller;
import org.springframework.context.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: Wang Ky
 * @Date: 2018/12/8 11:03
 * @Description:
 */
public class ApplicationContext implements BeanFactory {

    String[] configLocations;
    BeanDefinitionReader beanDefinitionReader;
    Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String,BeanDefinition>();
    Map<String,Object> beanCacheMap = new ConcurrentHashMap<String,Object>();
    Map<String,BeanWrapper> beanWrapperMap = new ConcurrentHashMap<String,BeanWrapper>();


    public ApplicationContext(String ... configLocatios) {
        this.configLocations = configLocatios;
        refresh();
    }
    public void refresh(){
        //1,定位
        beanDefinitionReader = new BeanDefinitionReader(configLocations);
        //2,加载
        List<String> beanDefinitions = beanDefinitionReader.loadBeanDefinitions();
        //3,注册
        doRegister(beanDefinitions);
        //4,依赖注入（lazy-init = false）,通过自动调用getBean()方法
        doAutowired();
    }

    private void doAutowired() {
        for(Map.Entry<String,BeanDefinition> entry : this.beanDefinitionMap.entrySet()){
            String beanName = entry.getKey();
            if(!entry.getValue().isLazyInit()){
                Object bean = getBean(beanName);
            }
        }
        for(Map.Entry<String,BeanWrapper> beanWrapperEntry : this.beanWrapperMap.entrySet()){
            pupulateBean(beanWrapperEntry.getKey(),beanWrapperEntry.getValue().getOriginalInstance());
        }
    }
    //自动注入
    private void pupulateBean(String beanName, Object originalInstance) {
        Class clazz = originalInstance.getClass();
        if(clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class)){
                Field[] declaredFields = clazz.getDeclaredFields();
                for(Field field : declaredFields){
                    if(!field.isAnnotationPresent(Autowired.class)) continue;
                    Autowired annotation = field.getAnnotation(Autowired.class);
                    String value = annotation.value().trim();
                    if("".equals(value)){
                        value = field.getType().getName();
                    }
                    field.setAccessible(true);

                    try {
                        field.set(originalInstance,this.beanWrapperMap.get(value).getWrappedInstance());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private void doRegister(List<String> beanDefinitions) {
        for(String className : beanDefinitions){
            try {
                Class<?> beanClass = Class.forName(className);
                if(beanClass.isInterface()) continue;
                BeanDefinition beanDefinition = beanDefinitionReader.registerBean(className);
                if(beanDefinition !=null){
                    this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
                }
                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    this.beanDefinitionMap.put(i.getName(),beanDefinition);
                }
                //到这里为止，容器初始化完毕
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    //通过反射new 一个实例
    @Override
    public Object getBean(String name) {
        try {
            BeanDefinition beanDefinition = this.beanDefinitionMap.get(name);
            Object instantion = instantion(beanDefinition);
            if(instantion == null) return null;

            BeanPostProcessor beanPostProcessor = new BeanPostProcessor();
            beanPostProcessor.postProcessBeforeInitialization(instantion,name);
            BeanWrapper beanWrapper = new BeanWrapper(instantion);
            this.beanWrapperMap.put(name,beanWrapper);
            beanPostProcessor.postProcessAfterInitialization(instantion,name);
            return this.beanWrapperMap.get(name).getWrappedInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private Object instantion(BeanDefinition beanDefinition) {
        String className = beanDefinition.getBeanClassName();
        Object instance = null;
        try {

            if(beanDefinitionMap.containsKey(className)){
                instance = beanCacheMap.get(className);
            }else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                beanCacheMap.put(className,instance);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return instance;

    }

}
