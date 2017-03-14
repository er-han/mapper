package mapper.util;

import mapper.Mappable;
import mapper.exception.ResultTypeInstantiationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

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
    ResultT result;

    try {
      result = (ResultT) resultType.newInstance();
    } catch (Exception e) {
      ResultTypeInstantiationException exception = new ResultTypeInstantiationException(resultType, e);
      logger.debug(exception.getMessage());
      throw exception;
    }

    return result;
  }
}
