package org.springframework.beans.factory;

/**
 * @Auther: Wang Ky
 * @Date: 2018/12/8 11:29
 * @Description:
 */
public class BeanWrapper {
    private Object wrapperInstance;
    private Object originalInstance;



    public BeanWrapper(Object instance) {
        this.originalInstance = instance;
        this.wrapperInstance = instance;
    }

    public Object getOriginalInstance() {
        return originalInstance;
    }

    public void setOriginalInstance(Object originalInstance) {
        this.originalInstance = originalInstance;
    }
    public Object getWrappedInstance(){
        return this.wrapperInstance;
    }
    public Class<?> getWrappedClass(){
        return this.wrapperInstance.getClass();
    }

}
