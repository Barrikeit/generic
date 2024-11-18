package org.barrikeit.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.barrikeit.model.domain.GenericEntity;
import org.barrikeit.service.dto.GenericDto;
import org.barrikeit.util.constants.ExceptionConstants;
import org.barrikeit.util.exceptions.FieldValueException;
import org.barrikeit.util.exceptions.NotFoundException;

public class ReflectionUtil {
  private ReflectionUtil() {
    throw new IllegalStateException("ReflectionUtil class");
  }

  public static Object newInstance(Class<?> clazz) {
    try {
      return clazz.getDeclaredConstructor().newInstance();
    } catch (InstantiationException
        | IllegalAccessException
        | NoSuchMethodException
        | InvocationTargetException e) {
      return null;
    }
  }

  public static Object getFieldValue(final Object instance, String fieldName) {
    Object value = null;
    try {
      String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      Method getterMethod = MethodUtils.getAccessibleMethod(instance.getClass(), getterName);
      if (getterMethod != null) {
        value = getterMethod.invoke(instance);
      }
    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
      throw new FieldValueException(
          ExceptionConstants.ERROR_FIELD_GET_VALUE, fieldName, instance);
    }
    return value;
  }

  public static void setFieldValue(final Object instance, String fieldName, Object value) {
    try {
      String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      Method setterMethod = MethodUtils.getAccessibleMethod(instance.getClass(), setterName);
      if (setterMethod != null) {
        setterMethod.invoke(instance, value);
      }
    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
      throw new FieldValueException(
          ExceptionConstants.ERROR_FIELD_SET_VALUE, fieldName, instance);
    }
  }

  public static <E> Class<E> getParameterizedTypeClass(Class<E> clazz, int index) {
    ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
    Type[] typeArguments = parameterizedType.getActualTypeArguments();
    @SuppressWarnings("unchecked")
    Class<E> tClass = (Class<E>) typeArguments[index];
    return tClass;
  }

  public static List<Field> getAllFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>();
    while (clazz != null) {
      fields.addAll(List.of(clazz.getDeclaredFields()));
      clazz = clazz.getSuperclass();
    }
    return fields;
  }

  public static List<Field> getAllAnnotatedFields(
          Class<?> clazz, Class<? extends Annotation> annotation) {
    List<Field> annotatedFields = new ArrayList<>();
    for (Field field : ReflectionUtil.getAllFields(clazz)) {
      if (field.isAnnotationPresent(annotation)) {
        annotatedFields.add(field);
      }
    }
    if (annotatedFields.isEmpty())
      throw new NotFoundException(
              ExceptionConstants.ERROR_MISSING_ANNOTATION, annotation, clazz);
    return annotatedFields;
  }

  public static Map<String, Field> getAllNestedFields(Class<?> clazz, String fieldName) {
    Map<String, Field> fields = new HashMap<>();
    for (Field field : clazz.getDeclaredFields()) {
      String fullFieldName = buildFullFieldName(fieldName, field.getName());
      if (GenericEntity.class.isAssignableFrom(field.getDeclaringClass())
          || GenericDto.class.isAssignableFrom(field.getDeclaringClass())) {
        fields.putAll(getAllNestedFields(field.getDeclaringClass(), fullFieldName));
      } else {
        fields.put(fullFieldName, field);
      }
    }
    Class<?> superClass = clazz.getSuperclass();
    if (superClass != null) {
      fields.putAll(getAllNestedFields(superClass, fieldName));
    }
    return fields;
  }

  private static String buildFullFieldName(String parent, String child) {
    return (parent == null) ? child : parent + "." + child;
  }
}
