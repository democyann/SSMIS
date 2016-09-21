package com.systop.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * refect helper class
 * 
 * @author Sam Lee
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public final class ReflectUtil {
  /**
   * log for this class
   */
  private static Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

  /**
   * 空的Method对象数组
   */
  public static final Method[] EMPTY_METHODS_ARRAY = new Method[] {};

  /**
   * Prevent from initializing.
   */
  private ReflectUtil() {
  }

  /**
   * Load the special class.If failed return <code>null</code>
   * 
   */
  public static Class classForName(String name) {
    try {
      return ClassUtils.forName(name);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Class not found." + name, e);
    }
  }


  /**
   * instantiating an object by special name,if failed,returns <tt>null</tt>
   * 
   */
  public static Object newInstance(String name) {
    Class clazz = classForName(name);
    if (clazz == null) {
      return null;
    }
    return newInstance(clazz);
  }

  /**
   * 执行class.newInstance方法，并处理异常.
   * 
   * @param type Type of the object to be instantialized.
   * @return object to be instantialized, 如果失败，返回null
   */
  public static <T> T newInstance(Class<T> type) {
    Assert.notNull(type);
    return (T) BeanUtils.instantiateClass(type);
  }

  /**
   * 根据属性的名称返回对应getter方法的Method对象
   */
  public static Method findGetterByProperty(Class clazz, String propertyName) {
    String getterName = PropertyUtil.parseGetter(propertyName);
    return ReflectionUtils.findMethod(clazz, getterName);
  }

  /**
   * 根据属性的名称返回对应getter方法的Method对象
   * @param argumentType setter方法的参数类型,如果为null,则返回null
   */
  public static Method findSetterByProperty(Class clazz, String propertyName,
      Class argumentType) {
    String setterName = PropertyUtil.parseSetter(propertyName);

    if (argumentType != null) {
      return ReflectionUtils.findMethod(clazz, setterName,
          new Class[] { argumentType });
    }
    return null;
  }

  /**
   * Invoke the java bean's getter method.
   * 
   * @param targetObject the target object to be invoked
   * @param propertyName property name of the setter, it can be a Database's
   *          fieldname like first_name, the name like that will be trained as
   *          firstName,eg.
   * 
   * <pre>
   *          FIRSTNAME -&gt;FIRSTNAME
   *          fristName -&gt;fristName
   *          FIRST_NAME -&gt;fristName
   *          &lt;pre&gt;
   * &#064;return
   * 
   */
  public static Object get(Object targetObject, String propertyName) {
    Assert.notNull(targetObject);
    Class clazz = targetObject.getClass();
    Method potentialGetter = findGetterByProperty(clazz, propertyName);
    if (potentialGetter != null) {
      return ReflectionUtils.invokeMethod(potentialGetter, targetObject);
    } else {
      reportNoSuchMethodException(clazz, propertyName);
      return null;
    }
  }

  /**
   * Invoke the standard java bean's setter method.
   * 
   * @param targetObject the target object to be invoked
   * @param propertyName property name of the setter, it can be a Database's
   *          fieldname like first_name, the name like that will be trained as
   *          firstName and invoke setFirstName
   * @param value
   */
  public static void set(Object targetObject, String propertyName, Object value) {
    Assert.notNull(targetObject);
    Class clazz = targetObject.getClass();
    Method potentialSetter = findSetterByProperty(clazz, propertyName, value
        .getClass());
    if (potentialSetter != null) {
      ReflectionUtils.invokeMethod(potentialSetter, targetObject,
          new Object[] { value });
    } else {
      reportNoSuchMethodException(clazz, propertyName);
    }
  }

  /**
   * Rethrow a IllegalStateException when catch NoSuchMethodException.
   */
  public static void reportNoSuchMethodException(Class clazz, String methodName) {
    throw new IllegalStateException(new StringBuffer("Method not found ")
        .append(clazz.getName()).append("#").append(methodName).toString());
  }

  /**
   * 反射调用某个对象的方法，返回调用结果，并处理异常。
   * @deprecated use
   *             {@link org.springframework.util.ReflectionUtils#invokeMethod(Method, Object, Object[])}
   *             instead
   */
  public static Object invoke(Object targetObject, Method method, Object[] args)
      throws Exception {
    try {
      return method.invoke(targetObject, args);
    } catch (NullPointerException npe) {
      throw new NullPointerException(
          "NullPointerException occurred while calling");
    } catch (InvocationTargetException ite) {
      logger.error("Exception occurred inside '{}'", ite.getMessage());
      throw new Exception(ite);
    } catch (IllegalAccessException iae) {
      logger.error("IllegalAccessException occurred while calling '{}'", iae.getMessage());
      throw new Exception(iae);
    } catch (IllegalArgumentException iae) {
      logger.error("IllegalArgumentException in class: {}, method of name: {}", 
          targetObject.getClass().getName(), method.getName());
      logger.error("IllegalArgumentException occurred while calling '{}", iae.getMessage());
      throw new Exception(iae);
    }

  }

  /**
   * 根据对象的名字，反射调用某个对象的方法，返回调用结果，并处理异常。
   */
  public static Object invoke(Object targetObject, String methodName,
      Object[] args) {
    Assert.notNull(targetObject);
    Method method = null;
    try {
      if (args != null) {
        Class[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
          if(args[i] != null) {
            parameterTypes[i] = args[i].getClass();
          }
        }
        method = ReflectionUtils.findMethod(targetObject.getClass(),
            methodName, parameterTypes);
      } else {
        method = targetObject.getClass().getMethod(methodName);
      }
    } catch (SecurityException e) {
      ReflectionUtils.handleReflectionException(e);
    } catch (NoSuchMethodException e) {
      reportNoSuchMethodException(targetObject.getClass(), methodName);
    }

    return ReflectionUtils.invokeMethod(method, targetObject, args);
  }

  /**
   * 调用多个get方法
   */
  public static Object nestedGet(Object bean, String property) {
    Assert.notNull(bean);
    Assert.hasText(property);

    String[] propertyNames = property.split("\\.");
    Object value = bean;
    for (int i = 0; i < propertyNames.length; i++) {
      value = get(value, propertyNames[i]);
    }

    return value;
  }

  /**
   * 获取当前类声明的private/protected变量
   * 
   */
  public static <T> T getPrivateProperty(Object target, String propertyName) {
    Assert.notNull(target);
    Assert.hasText(propertyName);
    Field field = null;
    try {
      field = target.getClass().getDeclaredField(propertyName);
      Assert.notNull(field);
      ReflectionUtils.makeAccessible(field);
      return (T) field.get(target);
    } catch (Exception e) {
      ReflectionUtils.handleReflectionException(e);
    }

    return null;
  }

  /**
   * 设置当前类声明的private/protected变量
   */
  static public <T> void setPrivateProperty(Object target, String propertyName,
      T newValue) {
    Assert.notNull(target);
    Assert.hasText(propertyName);

    Field field = null;
    try {
      field = target.getClass().getDeclaredField(propertyName);
      ReflectionUtils.makeAccessible(field);
      Assert.notNull(field);
      field.set(target, newValue);
    } catch (Exception e) {
      ReflectionUtils.handleReflectionException(e);
    }
  }

  /**
   * 调用当前类声明的private/protected函数
   */
  static public Object invokePrivateMethod(Object object, String methodName,
      Object[] params) {
    Assert.notNull(object);
    Assert.hasText(methodName);
    Class[] types = new Class[params.length];
    for (int i = 0; i < params.length; i++) {
      types[i] = params[i].getClass();
    }
    try {
      Method method = object.getClass().getDeclaredMethod(methodName, types);
      method.setAccessible(true);
      return method.invoke(object, params);
    } catch (Exception e) {
      ReflectionUtils.handleReflectionException(e);
    }

    return null;
  }

  /**
   * Find all decleared methods by given name, ignore cases.
   * @param clazz given class
   * @param methodName ginven name.
   * @return Array of Method or empty array.
   */
  public static Method[] findDeclaredMethods(Class clazz, String methodName) {
    List methods = new ArrayList();
    Method[] declearedMethods = clazz.getDeclaredMethods();
    for (int i = 0; i < declearedMethods.length; i++) {
      if (methodName.equalsIgnoreCase(declearedMethods[i].getName())) {
        methods.add(declearedMethods[i]);
      }
    }

    return (Method[]) methods.toArray(EMPTY_METHODS_ARRAY);
  }

  /**
   * Find decleared method by given name, ignore cases.
   * @param clazz given class
   * @param methodName ginven name.
   * @return Mehtod or null.
   */
  public static Method findDeclaredMethod(Class clazz, String methodName) {
    Method[] methods = findDeclaredMethods(clazz, methodName);
    if (methods == null || methods.length == 0) {
      return null;
    }
    return methods[0];
  }

  /**
   * Attempt to find a {@link Field field} on the supplied {@link Class} with
   * the supplied <code>name</code>. Searches all superclasses up to
   * {@link Object}.
   * 
   */
  public static Field findField(Class clazz, String name) {
    Assert.notNull(clazz,
        "The 'class to introspect' supplied to findField() can not be null.");
    Assert.hasText(name,
        "The field name supplied to findField() can not be empty.");

    Class searchType = clazz;
    while (!Object.class.equals(searchType) && searchType != null) {
      final Field[] fields = searchType.getDeclaredFields();
      for (int i = 0; i < fields.length; i++) {
        Field field = fields[i];
        if (name.equals(field.getName())) {
          return field;
        }
      }
      searchType = searchType.getSuperclass();
    }
    return null;
  }

  /**
   * if Field is Modified by <code>transient</code>
   * @param clazz target class
   * @param fieldName name of the field
   */
  public static boolean isTransientField(Class clazz, String fieldName) {
    Assert.notNull(clazz);
    Field field = findField(clazz, fieldName);
    return Modifier.isTransient(field.getModifiers());
  }

  /**
   * 判断给定的方法是否被某个annotation注解
   * @param m 给定方法
   * @param annotationName annotation的完整类名
   * 
   */
  public static boolean isAnnotatedMethod(Method m, String annotationName) {
    Assert.notNull(m);

    Annotation[] anns = m.getAnnotations();
    for (int i = 0; i < anns.length; i++) {
      if (anns[i].annotationType().getName().equals(annotationName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 将一个Bean转换为Map，Map的"key"是属性名称，value是属性值.只包含当前对象的字段， 父类的字段不包括。
   * @param bean 指定的bean
   * @param propertyNames 被转换的属性的名称列表 如果为<code>null</code> 或长度为<code>0</code>，返回所有字段.
   * @param containsNull 如果为false，那么如果某个属性为null，则不被转换。
   * 
   */
  static public Map toMap(Object bean, String[] propertyNames,
      boolean containsNull) {
    Map map = Collections.synchronizedMap(new HashMap());

    // 如果没有指定属性名称，返回所有属性组成的map
    if (propertyNames == null || propertyNames.length == 0) {
      Method[] methods = bean.getClass().getMethods();
      List getters = new ArrayList();
      for (Method method : methods) {
        if (method.getParameterTypes().length == 0
            && PropertyUtil.isGetter(method.getName())) {
          String property = PropertyUtil.accessor2Property(method.getName());
          if(!StringUtils.equals(property, "class")) {
            getters.add(PropertyUtil.accessor2Property(method.getName()));
          }
        }
      }
      propertyNames = (String[]) getters.toArray(new String[] {});
      if (propertyNames != null) {
        logger.debug("Get all {} properties", propertyNames.length);
      }
    }

    for (String fieldName : propertyNames) {
      // 获取属性值
      Object object = get(bean, fieldName);
      if (containsNull || object != null) {
        map.put(fieldName, object);
      }
    }

    return map;
  }
  
  
  /**
   * 调用<code>org.apache.commons.beanutils.BeanUitls.copyProperties</code>
   * 方法，复制bean的属性。同时，处理异常。
   * @param dest 目标bean
   * @param src 源bean
   * @deprecated use
   *             {@link org.springframework.beans.BeanUtils#copyProperties(Object, Object)}
   */
  public static void copyProperties(Object dest, Object src) {
    BeanUtils.copyProperties(src, dest);
  }

  /**
   * 按照指定的字段复制两个Bean的属性
   * @param dest 目标bean
   * @param src 源bean
   * @param propNames 给定字段名称，如果为null， 则相当于调用
   *          {@link #copyProperties(Object, Object)}
   * @deprecated use
   *             {@link org.springframework.beans.BeanUtils#copyProperties(Object, Object, String[])}
   */
  public static void copyProperties(Object dest, Object src, String[] propNames) {
    if (propNames == null) {
      copyProperties(dest, src);
      return;
    }

    for (int i = 0; i < propNames.length; i++) {
      Object val = get(src, propNames[i]);
      set(dest, propNames[i], val);
    }
  }

  /**
   * 复制一个List中的所有element对象到另一个List，相当于反复调用
   * {@link BeanUtils#copyProperties(Object, Object, String[])}
   * @param dest 目标list
   * @param src 源list
   * @param fields 指定忽略的字段
   */
  public static <T> void copyList(List<T> dest, List<T> src, String... ignoreProperties) {
    if (src == null) {
      return;
    }

    for (T obj : src) {
      if (obj != null) {
        if (dest == null) {
          dest = new ArrayList<T>(src.size());
        }

        T destObj = (T) newInstance(obj.getClass());
        BeanUtils.copyProperties(obj, destObj, ignoreProperties);
        dest.add(destObj);
      }
    }
  }

  /**
   * 根据指定的字段，创建一个新的list，与原有list的内容相同
   * @see {@link ReflectUtil#copyList(List, List, String[])
   */
  public static <T> List<T> copyList(List<T> src, String... ignoreProperties) {
    if (src == null) {
      return ListUtils.EMPTY_LIST;
    }

    List dest = new ArrayList(src.size());
    ReflectUtil.copyList(dest, src, ignoreProperties);
    return dest;
  }

  /**
   * find all getter's ,confirm to JavaBean spec.Igore getClass() method.
   */
  public static List<Method> getters(Class clazz, boolean declaredOnly) {
    Assert.notNull(clazz);

    Method[] all = (declaredOnly) ? clazz.getDeclaredMethods() : clazz
        .getMethods();
    if (all == null) {
      return Collections.EMPTY_LIST;
    }
    List getters = new ArrayList(all.length);
    for (int i = 0; i < all.length; i++) {
      if (!PropertyUtil.isGetter(all[i].getName())
          || all[i].getParameterTypes().length != 0) {
        continue;
      }
      if (!all[i].getDeclaringClass().equals(Object.class)) {
        getters.add(all[i]);
      }
    }

    return getters;
  }

  /**
   * find all setter's ,confirm to JavaBean spec.
   */
  public static List<Method> setters(Class clazz, boolean declaredOnly) {
    Assert.notNull(clazz);

    Method[] all = (declaredOnly) ? clazz.getDeclaredMethods() : clazz
        .getMethods();
    if (all == null) {
      return Collections.EMPTY_LIST;
    }
    List setters = new ArrayList(all.length);
    for (int i = 0; i < all.length; i++) {
      if (!PropertyUtil.isSetter(all[i].getName())
          || all[i].getParameterTypes().length != 1) {
        continue;
      }
      setters.add(all[i]);
    }

    return setters;
  }
}
