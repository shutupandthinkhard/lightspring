package org.lightspring.aop.config;

import org.lightspring.beans.BeanUtils;
import org.lightspring.beans.factory.BeanFactory;
import org.lightspring.beans.factory.BeanFactoryAware;
import org.lightspring.beans.factory.FactoryBean;
import org.lightspring.util.StringUtils;

import java.lang.reflect.Method;

public class MethodLocatingFactory implements FactoryBean<Method>, BeanFactoryAware {

    private String targetBeanName;

    private String methodName;

    private Method method;

    public void setTargetBeanName(String targetBeanName) {
        this.targetBeanName = targetBeanName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        if (!StringUtils.hasText(this.targetBeanName)) {
            throw new IllegalArgumentException("Property 'targetBeanName' is required");
        }
        if (!StringUtils.hasText(this.methodName)) {
            throw new IllegalArgumentException("Property 'methodName' is required");
        }

        Class<?> beanClass = beanFactory.getType(this.targetBeanName);
        if (beanClass == null) {
            throw new IllegalArgumentException("Can't determine type of bean with name '" + this.targetBeanName + "'");
        }


        this.method = BeanUtils.resolveSignature(this.methodName, beanClass);

        if (this.method == null) {
            throw new IllegalArgumentException("Unable to locate method [" + this.methodName +
                    "] on bean [" + this.targetBeanName + "]");
        }
    }


    @Override
    public Method getObject() throws Exception {
        return this.method;
    }

    @Override
    public Class<?> getObjectType() {
        return Method.class;
    }
}
