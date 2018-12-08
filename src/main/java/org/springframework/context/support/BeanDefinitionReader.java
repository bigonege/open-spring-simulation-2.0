package org.springframework.context.support;

import org.springframework.beans.factory.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Auther: Wang Ky
 * @Date: 2018/12/8 11:22
 * @Description:
 */
public class BeanDefinitionReader {

    Properties config = new Properties();

    private List<String> registyBeanClasses = new ArrayList<String>();
    //读取配置文件（查找，读取，解析）
    public BeanDefinitionReader(String... locations) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:", ""));
        try {
            config.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(resourceAsStream != null){
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        doScanner(config.getProperty("scanPackage"));

    }

    public List<String> loadBeanDefinitions(){
        return  this.registyBeanClasses;
    }

    private void doScanner(String scanPackage) {
         URL url = this.getClass().getClassLoader().getResource("/"+scanPackage.replaceAll("\\.","/"));
         File files = new File(url.getFile());
         for(File file : files.listFiles()){
            if (file.isDirectory()){
                doScanner(scanPackage+"."+file.getName());
            }else {
                registyBeanClasses.add(scanPackage+"."+file.getName().replace(".class",""));
            }
         }
    }

    public Properties getConfig(){
        return this.config;
    }

    public BeanDefinition registerBean(String className){
        if(this.registyBeanClasses.contains(className)){
            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setBeanClassName(className);
            beanDefinition.setFactoryBeanName(lowerFrist(className.substring(className.lastIndexOf(".")+1)));
            return beanDefinition;
        }
        return  null;
    }

    private String lowerFrist(String substring) {
        char[] chars = substring.toCharArray();
        chars[0]+=32;
        return String.valueOf(chars);
    }
}
