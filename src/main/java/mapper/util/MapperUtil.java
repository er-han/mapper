package mapper.util;

import mapper.Mappable;
import mapper.exception.ResultTypeInstantiationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by erhan.karakaya on 3/10/2017.
 */
public class MapperUtil {

  private static final Logger logger = LoggerFactory.getLogger(MapperUtil.class);

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
   * @return a Map object which contains extracted fields names and values
   * from the given object
   * @throws IllegalAccessException
   */
  public static Map<String, Object> getFieldsMap(final Mappable mappable) throws IllegalAccessException {
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


  public static <SourceT extends Mappable, ResultT extends Mappable> ResultT map(SourceT source, Class<ResultT> resultType) throws ResultTypeInstantiationException {


    Class<?> sourceType = source.getClass();
    logger.debug("Started mapping from source type '" + sourceType.getName() + "' to result type '" + resultType.getName() + "'.");

    ResultT result;

    try {
      result = (ResultT) resultType.newInstance();
    } catch (Exception e) {
      ResultTypeInstantiationException exception = new ResultTypeInstantiationException(resultType, e);
      logger.debug(exception.getMessage());
      throw exception;
    }

    List<Field> sourceFields = getAllFields(sourceType);

    logger.debug("Found fields in the source type: " + sourceFields);

    List<Field> resultFields = getAllFields(resultType);

    logger.debug("Found fields in the result type: " + resultFields);

    for (Field sourceField :
        sourceFields) {
      String sourceFieldName = sourceField.getName();
      Class<?> sourceFieldType = sourceField.getType();

      Stream<Field> resultFieldsStream = resultFields.stream();
      Optional<Field> resultFieldOptional = resultFieldsStream.
          filter(field ->
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
          logger.debug("Field '" + sourceType.getName() + " " + sourceFieldName + "' does not have a getter method.");
          continue;
        }

        Object sourceFieldVal;
        try {
          sourceFieldVal = sourceGetMethod.invoke(source, null);
        } catch (InvocationTargetException e) {
          logger.debug("Invokation of  '" + sourceType.getName() + " " + sourceGetMethodName + "' failed. " +
              "Probably it requires at least 1 arg.");
          continue;
        } catch (IllegalAccessException e) {
          logger.debug("Invokation of  '" + sourceType.getName() + " " + sourceGetMethodName + "' failed. " +
              "It has restricted access.");
          continue;
        }catch (IllegalArgumentException e) {
          logger.debug("Invokation of  '" + sourceType.getName() + " " + sourceGetMethodName + "' failed. " +
              "Illegal argument.");
          continue;
        }

        Method resultSetMethod;
        String resultSetMethodName = "set" + fieldNameCapitalized;
        try {
          resultSetMethod = resultType.getMethod(resultSetMethodName, sourceFieldType);
        } catch (NoSuchMethodException e) {
          logger.debug("Field '" + resultType.getName() + " " + sourceFieldName + "' does not have a setter method.");
          continue;
        }


        try {
          resultSetMethod.invoke(result, sourceFieldVal);
        } catch (InvocationTargetException e) {
          logger.debug("Invokation of  '" + resultType.getName() + " " + resultSetMethodName + "' failed. " +
              "Probably it requires at least 1 arg.");
          continue;
        } catch (IllegalAccessException e) {
          logger.debug("Invokation of  '" + resultType.getName() + " " + resultSetMethodName + "' failed. " +
              "It has restricted access.");
          continue;
        } catch (IllegalArgumentException e) {
          logger.debug("Invokation of  '" + resultType.getName() + " " + resultSetMethodName + "' failed. " +
              "Illegal argument.");
          continue;
        }

      }
    }

    return result;
  }
}
