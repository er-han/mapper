package mapper.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mapper.Mappable;
import mapper.exception.ResultTypeInstantiationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by erhan.karakaya on 3/10/2017.
 */
public class MapperUtil {

  public static final Logger logger = LoggerFactory.getLogger(MapperUtil.class);

  /**
   * Gets all public, private, protected fields of the given type.
   * If the given type has superclass, gets it's fields too using recursive call of self.
   *
   * @param type The type which's fields will be returned.
   * @return List of the fields of the given type.
   */
  public static List<Field> getAllFields(Class<?> type) {
    List<Field> fields = new ArrayList<>();
    if (type.getSuperclass() != null) {
      List<Field> fieldsOfSuper = getAllFields(type.getSuperclass());
      fields.addAll(fieldsOfSuper);
    }

    List<Field> fieldsOfCurrent = Arrays.stream(type.getDeclaredFields())
        .map(field -> {
          Iterator<Field> iterator = fields.iterator();
          while (iterator.hasNext()) {
            Field tempField = iterator.next();
            if (tempField.getName().equals(field.getName())) {
              iterator.remove();
              break;
            }
          }
          return field;
        }).collect(Collectors.toList());

    fields.addAll(fieldsOfCurrent);

    return fields;
  }


  /**
   * Extracts a map of fields in given Mappable object.
   * If given object type has a super class,
   * the fields derived from the super class is extracted too.
   *
   * @param mappable The object which's fields are wanted to be extracted.
   * @return a Map object which contains extracted fields names and values from the given object
   * @throws IllegalAccessException throws this when can't get one of the field's value of mappable.
   */
  public static Map<String, Object> getFieldsMap(final Mappable mappable)
      throws IllegalAccessException {
    final Map<String, Object> map = new HashMap<>();
    final List<Field> fields = getAllFields(mappable.getClass());
    for (final Field field : fields) {
      final boolean isFieldAccessible = field.isAccessible();
      field.setAccessible(true);
      map.put(field.getName(), field.get(mappable));
      field.setAccessible(isFieldAccessible);
    }
    return map;
  }

  /**
   * Maps given source object's suitable fields
   * to a newly instantiated object of type resultType.
   *
   * @param source     the object which's fields will be taken as map source.
   * @param resultType the type which will be used to instantiate a target object.
   * @param <SourceT>  source object' type.
   * @param <ResultT>  result object's type.
   * @return an object of type resultType.
   * @throws ResultTypeInstantiationException throws this when can't instantiate a new object.
   */
  public static <SourceT extends Mappable, ResultT extends Mappable>
      ResultT map(SourceT source, Class<ResultT> resultType)
      throws ResultTypeInstantiationException {


    Class<?> sourceType = source.getClass();
    logger.debug("Started mapping from source type '"
        + sourceType.getName() + "' to result type '" + resultType.getName() + "'.");

    ResultT result;

    try {
      result = (ResultT) resultType.newInstance();
    } catch (Exception e) {
      ResultTypeInstantiationException exception =
          new ResultTypeInstantiationException(resultType, e);
      logger.debug(exception.getMessage());
      throw exception;
    }

    result = map(source, result);

    return result;
  }


  /**
   * Maps from source to result.
   * Both source and result must be Mappable.
   *
   * @param source    the object which's fields will be taken as map source.
   * @param result    the object which's fields will be taken as map target.
   * @param <SourceT> source object' type.
   * @param <ResultT> result object's type.
   * @return an object of type resultType.
   */
  public static <SourceT extends Mappable, ResultT extends Mappable> ResultT
      map(SourceT source, ResultT result) {
    if (source == null || result == null) {
      return null;
    }
    Class<?> sourceType = source.getClass();
    List<Field> sourceFields = getAllFields(sourceType);

    logger.debug("Found fields in the source type: " + sourceFields);

    Class<?> resultType = result.getClass();
    List<Field> resultFields = getAllFields(resultType);

    logger.debug("Found fields in the result type: " + resultFields);

    for (Field sourceField :
        sourceFields) {
      String sourceFieldName = sourceField.getName();
      Class<?> sourceFieldType = sourceField.getType();

      Stream<Field> resultFieldsStream = resultFields.stream();
      Optional<Field> resultFieldOptional = resultFieldsStream
          .filter(field ->
              field.getName() == sourceFieldName
                  && field.getType() == sourceFieldType)
          .findFirst();

      if (resultFieldOptional.isPresent()) {

        String fieldNameCapitalized = StringUtil.capitalizeFirstLetter(sourceFieldName);


        Method sourceGetMethod;
        String sourceGetMethodName = "get" + fieldNameCapitalized;
        try {
          sourceGetMethod = sourceType.getMethod(sourceGetMethodName);
        } catch (NoSuchMethodException e) {
          logger.debug("Field '" + sourceType.getName() + " "
              + sourceFieldName + "' does not have a getter method.");
          continue;
        }

        Object sourceFieldVal;
        try {
          sourceFieldVal = sourceGetMethod.invoke(source, null);
        } catch (InvocationTargetException e) {
          logger.debug("Invokation of  '" + sourceType.getName()
              + " " + sourceGetMethodName + "' failed. "
              + "Probably it requires at least 1 arg.");
          continue;
        } catch (IllegalAccessException e) {
          logger.debug("Invokation of  '"
              + sourceType.getName() + " " + sourceGetMethodName + "' failed. "
              + "It has restricted access.");
          continue;
        } catch (IllegalArgumentException e) {
          logger.debug("Invokation of  '"
              + sourceType.getName() + " " + sourceGetMethodName + "' failed. "
              + "Illegal argument.");
          continue;
        }

        Method resultSetMethod;
        String resultSetMethodName = "set" + fieldNameCapitalized;
        try {
          resultSetMethod = resultType.getMethod(resultSetMethodName, sourceFieldType);
        } catch (NoSuchMethodException e) {
          logger.debug("Field '" + resultType.getName() + " "
              + sourceFieldName + "' does not have a setter method.");
          continue;
        }


        try {
          resultSetMethod.invoke(result, sourceFieldVal);
        } catch (InvocationTargetException e) {
          logger.debug("Invokation of  '" + resultType.getName()
              + " " + resultSetMethodName + "' failed. "
              + "Probably it requires at least 1 arg.");
          continue;
        } catch (IllegalAccessException e) {
          logger.debug("Invokation of  '" + resultType.getName()
              + " " + resultSetMethodName + "' failed. "
              + "It has restricted access.");
          continue;
        } catch (IllegalArgumentException e) {
          logger.debug("Invokation of  '" + resultType.getName()
              + " " + resultSetMethodName + "' failed. "
              + "Illegal argument.");
          continue;
        }

      }
    }
    return result;
  }

  public static <SourceT extends Mappable, ResultT extends Mappable> ResultT
      map(SourceT source, Supplier<ResultT> supplier) {
    return map(source, supplier.get());
  }

  /**
   *
   * @param sources A list of object's to be mapped from.
   * @param resultType the type which will be used to instantiate a target object.
   * @param <SourceT> source object' type.
   * @param <ResultT> result object's type.
   * @return A List&lt;ResultT&gt; object of type resultType.
   * @throws ResultTypeInstantiationException throws this when can't instantiate a new object.
   */
  public static <SourceT extends Mappable, ResultT extends Mappable> List<ResultT>
      map(List<SourceT> sources, Class<ResultT> resultType)
      throws ResultTypeInstantiationException {

    try {
      ResultT resultT = resultType.newInstance();
    } catch (Exception e) {
      ResultTypeInstantiationException exception =
          new ResultTypeInstantiationException(resultType, e);
      logger.debug(exception.getMessage());
      throw exception;
    }

    return map(sources, () -> {
      try {
        return resultType.newInstance();
      } catch (Exception e) {
        return null;
      }
    });

  }


  /**
   *
   * @param sources A list of object's to be mapped from.
   * @param supplier Target type's supplier function.
   * @param <SourceT> source object' type.
   * @param <ResultT> result object's type.
   * @return A List&lt;ResultT&gt; object of type resultType.
   */
  public static <SourceT extends Mappable, ResultT extends Mappable> List<ResultT>
      map(List<SourceT> sources, Supplier<ResultT> supplier) {
    if (sources == null || supplier == null) {
      return null;
    }

    List<ResultT> results = new ArrayList<ResultT>();
    sources.forEach(source -> results.add(map(source, supplier)));

    return results;
  }

}
