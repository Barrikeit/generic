package org.barrikeit.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.barrikeit.model.domain.GenericEntity;
import org.barrikeit.service.dto.GenericDto;
import org.barrikeit.util.constants.ExceptionConstants;
import org.barrikeit.util.exceptions.FieldValueException;
import org.barrikeit.util.exceptions.NotFoundException;
import org.barrikeit.util.exceptions.UnExpectedException;
import org.springframework.util.ReflectionUtils;

import static org.barrikeit.util.TimeUtil.convertLocalDate;
import static org.barrikeit.util.TimeUtil.convertLocalDateTime;

public class ReflectionUtil extends ReflectionUtils {
  private ReflectionUtil() {
    throw new IllegalStateException("ReflectionUtil class");
  }

  /**
   * Crea una nueva instancia de una clase utilizando su constructor sin argumentos.
   *
   * @param clazz La clase de la cual se desea crear una nueva instancia.
   * @return Una nueva instancia de la clase especificada o `null` si ocurre un error al crearla.
   */
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

  /**
   * Obtiene el valor de un campo especificado de una instancia utilizando su método getter.
   *
   * @param instance La instancia de la cual se desea obtener el valor del campo.
   * @param fieldName El nombre del campo cuyo valor se desea obtener.
   * @return El valor del campo especificado o `null` si no se puede acceder al método getter.
   * @throws FieldValueException Si ocurre un error al intentar acceder al valor del campo.
   */
  public static Object getFieldValue(final Object instance, String fieldName) {
    Object value = null;
    try {
      String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      Method getterMethod = findMethod(instance.getClass(), getterName);
      if (getterMethod != null) {
        value = getterMethod.invoke(instance);
      }
    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
      throw new FieldValueException(ExceptionConstants.ERROR_FIELD_GET_VALUE, fieldName, instance);
    }
    return value;
  }

  /**
   * Establece un valor en un campo de una instancia utilizando su método setter.
   *
   * @param instance La instancia en la cual se desea establecer el valor del campo.
   * @param fieldName El nombre del campo al cual se le asignará el valor.
   * @param value El valor a establecer en el campo.
   * @throws FieldValueException Si ocurre un error al intentar establecer el valor del campo.
   */
  public static void setFieldValue(final Object instance, String fieldName, Object value) {
    try {
      String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      Method setterMethod = findMethod(instance.getClass(), setterName, value.getClass());
      if (setterMethod != null) {
        setterMethod.invoke(instance, value);
      }
    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
      throw new FieldValueException(ExceptionConstants.ERROR_FIELD_SET_VALUE, fieldName, instance);
    }
  }

  /**
   * Obtiene la clase del tipo genérico parametrizado en el índice especificado.
   *
   * @param clazz Clase de la cual se extraerá el tipo parametrizado.
   * @param index Índice del parámetro genérico dentro de la clase.
   * @return La clase correspondiente al tipo genérico parametrizado.
   */
  public static <E> Class<E> getParameterizedTypeClass(Class<E> clazz, int index) {
    ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
    Type[] typeArguments = parameterizedType.getActualTypeArguments();
    @SuppressWarnings("unchecked")
    Class<E> tClass = (Class<E>) typeArguments[index];
    return tClass;
  }

  /**
   * Obtiene todos los campos declarados de una clase, incluyendo los campos de sus superclases.
   *
   * @param clazz Clase de la cual se desean obtener los campos.
   * @return Una lista con todos los campos declarados de la clase y sus superclases.
   */
  public static List<Field> getAllFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>();
    while (clazz != null) {
      fields.addAll(List.of(clazz.getDeclaredFields()));
      clazz = clazz.getSuperclass();
    }
    return fields;
  }

  /**
   * Obtiene todos los campos de una clase, incluyendo los campos de sus superclases, que estén
   * anotados con una anotación específica.
   *
   * @param clazz Clase de la cual se desean obtener los campos anotados.
   * @param annotation La clase de la anotación que se busca en los campos.
   * @return Una lista con todos los campos anotados de la clase y sus superclases.
   * @throws NotFoundException Si no se encuentra ningún campo con la anotación especificada.
   */
  public static List<Field> getAllAnnotatedFields(
      Class<?> clazz, Class<? extends Annotation> annotation) {
    List<Field> annotatedFields = new ArrayList<>();
    for (Field field : ReflectionUtil.getAllFields(clazz)) {
      if (field.isAnnotationPresent(annotation)) {
        annotatedFields.add(field);
      }
    }
    if (annotatedFields.isEmpty())
      throw new NotFoundException(ExceptionConstants.ERROR_MISSING_ANNOTATION, annotation, clazz);
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

  public static List<String> getFieldNamesWithAnnotation(
      Object instance, Class<? extends Annotation> annotation, String entityName) {
    List<String> annotatedFieldsMap = new ArrayList<>();

    for (Field field : instance.getClass().getDeclaredFields()) {
      String fieldName = buildFullFieldName(entityName, field.getName());
      if (field.isAnnotationPresent(annotation)) {
        annotatedFieldsMap.add(fieldName);
      } else if (field.getType().isAssignableFrom(GenericDto.class)
          || field.getType().isAssignableFrom(GenericEntity.class)) {
        annotatedFieldsMap.addAll(
            getFieldNamesWithAnnotation(field.getClass(), annotation, fieldName));
      }
    }
    return annotatedFieldsMap;
  }

  public static List<String> getFieldNamesWithOutAnnotation(
      Object instance, Class<? extends Annotation> annotation, String entityName) {
    List<String> annotatedFieldsMap = new ArrayList<>();

    for (Field field : instance.getClass().getDeclaredFields()) {
      String fieldName = buildFullFieldName(entityName, field.getName());
      if (!Modifier.isStatic(field.getModifiers()) && !field.isAnnotationPresent(annotation)) {
        annotatedFieldsMap.add(fieldName);
      }
    }

    return annotatedFieldsMap;
  }

  /**
   * Obtiene los campos anotados con una anotación específica desde una instancia de clase,
   * incluyendo campos anidados.
   *
   * @param instance El objeto del cual se extraen los campos y valores anotados.
   * @param annotation La clase de la anotación a buscar.
   * @param fieldName Nombre del campo padre, usado en recursividad para construir nombres completos
   *     en formato "padre.hijo".
   * @return Un `Map` que asocia el nombre completo de cada campo anotado con su valor. Los nombres
   *     reflejan la estructura anidada como "campo1.campo2".
   */
  public static Map<String, Object> getFieldValuesWithAnnotation(
      Object instance, Class<? extends Annotation> annotation, String fieldName) {
    Map<String, Object> annotatedFieldsMap = new HashMap<>();

    for (Field field : instance.getClass().getDeclaredFields()) {
      Object value = getFieldValue(instance, field.getName());
      String fullFieldName =
          (fieldName == null ? field.getName() : fieldName + "." + field.getName());
      if (field.isAnnotationPresent(annotation)) {
        annotatedFieldsMap.put(fullFieldName, value);
      } else if (value instanceof GenericDto || value instanceof GenericEntity) {
        annotatedFieldsMap.putAll(getFieldValuesWithAnnotation(value, annotation, fullFieldName));
      }
    }
    return annotatedFieldsMap;
  }

  private static String buildFullFieldName(String parent, String child) {
    return (parent == null) ? child : parent + "." + child;
  }

  @SuppressWarnings("unchecked")
  private <M> M castFieldToType(Object value, Class<M> targetType) {
    try {
      if (value == null) {
        return null;
      } else if (targetType.isInstance(value)) {
        return (M) value;
      } else if (targetType == String.class) {
        return (M) value.toString();
      } else if (targetType == Integer.class || targetType == int.class) {
        return (M) Integer.valueOf(value.toString());
      } else if (targetType == Long.class || targetType == long.class) {
        return (M) Long.valueOf(value.toString());
      } else if (targetType == Float.class || targetType == float.class) {
        return (M) Float.valueOf(value.toString());
      } else if (targetType == Double.class || targetType == double.class) {
        return (M) Double.valueOf(value.toString());
      } else if (targetType == BigDecimal.class) {
        return (M) new BigDecimal(value.toString());
      } else if (targetType == LocalDate.class) {
        if (value instanceof String) return (M) convertLocalDate((String) value);
        if (value instanceof LocalDateTime) return (M) ((LocalDateTime) value).toLocalDate();
      } else if (targetType == LocalDateTime.class) {
        if (value instanceof String) return (M) convertLocalDateTime((String) value);
        if (value instanceof LocalDate) return (M) ((LocalDate) value).atStartOfDay();
      }
    } catch (Exception e) {
      throw new UnExpectedException(
          "Failed to cast value: {} to type: {}", value, targetType.getName());
    }
    throw new UnExpectedException("Unsupported cast type: {}", targetType.getName());
  }
}
