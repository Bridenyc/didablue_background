package util;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public abstract class BeanUtils extends org.springframework.beans.BeanUtils {
    private static final Map<String, BeanCopier> beanCopierMap = new HashMap();

    public BeanUtils() {
    }

    public static void copyProperties(Object source, Object target, boolean ignoreNullProperties, boolean ignoreCollection, String... ignoreProperties) throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = ignoreProperties != null ? Arrays.asList(ignoreProperties) : null;
        PropertyDescriptor[] var11 = targetPds;
        int var10 = targetPds.length;

        for(int var9 = 0; var9 < var10; ++var9) {
            PropertyDescriptor targetPd = var11[var9];
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreProperties == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }

                            Object value = readMethod.invoke(source);
                            if ((!ignoreCollection || !Collection.class.isAssignableFrom(readMethod.getReturnType())) && (!ignoreNullProperties || value != null)) {
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }

                                writeMethod.invoke(target, value);
                            }
                        } catch (Throwable var16) {
                            throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", var16);
                        }
                    }
                }
            }
        }

    }

    public static void copyProps(Object source, Object target) {
        copyProps(source, (Object)target, (Converter)null);
    }

    public static <T> T copyProps(Object source, Class<T> type) throws IllegalStateException {
        return copyProps(source, (Class<T>) type, (Converter)null);
    }

    public static void copyProps(Object source, Object target, Converter converter) {
        if (source != null && target != null) {
            String key = generateKey(source.getClass(), target.getClass());
            BeanCopier copier = null;
            if (!beanCopierMap.containsKey(key)) {
                copier = BeanCopier.create(source.getClass(), target.getClass(), false);
                beanCopierMap.put(key, copier);
            } else {
                copier = (BeanCopier)beanCopierMap.get(key);
            }

            copier.copy(source, target, converter);
        }
    }

    public static <T> T copyProps(Object source, Class<T> type, Converter converter) throws IllegalStateException {
        if (source != null && type != null) {
            String key = generateKey(source.getClass(), type);
            BeanCopier copier = null;
            if (!beanCopierMap.containsKey(key)) {
                copier = BeanCopier.create(source.getClass(), type, false);
                beanCopierMap.put(key, copier);
            } else {
                copier = (BeanCopier)beanCopierMap.get(key);
            }

            Object t = null;

            try {
                t = type.newInstance();
            } catch (IllegalAccessException | InstantiationException var7) {
                throw new IllegalStateException("无法实例化: " + var7);
            }

            copier.copy(source, t, converter);
            return (T) t;
        } else {
            return null;
        }
    }

    private static String generateKey(Class<?> source, Class<?> target) {
        return source.getName() + target.getName();
    }

    public static <T> List<T> copyList(List<?> sourceList, Class<T> type) throws IllegalStateException {
        List<T> targetList = new ArrayList();
        if (sourceList != null && sourceList.size() > 0 && type != null) {
            Iterator var4 = sourceList.iterator();

            while(var4.hasNext()) {
                Object source = var4.next();

                Object target;
                try {
                    target = type.newInstance();
                } catch (IllegalAccessException | InstantiationException var7) {
                    throw new IllegalStateException("无法实例化: " + var7);
                }

                copyProps(source, target);
                targetList.add((T) target);
            }
        }

        return targetList;
    }

    public static <T> Set<T> copySets(Set<?> sourceSet, Class<T> type) throws IllegalStateException {
        Set<T> targetSet = new HashSet();
        if (sourceSet != null && sourceSet.size() > 0 && type != null) {
            Iterator var4 = sourceSet.iterator();

            while(var4.hasNext()) {
                Object source = var4.next();

                Object target;
                try {
                    target = type.newInstance();
                } catch (IllegalAccessException | InstantiationException var7) {
                    throw new IllegalStateException("无法实例化: " + var7);
                }

                copyProps(source, target);
                targetSet.add((T) target);
            }
        }

        return targetSet;
    }

    public static String toString(Object obj) {
        return obj == null ? Objects.toString(obj) : ReflectionToStringBuilder.toString(obj);
    }
}

