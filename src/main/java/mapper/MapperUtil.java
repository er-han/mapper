package mapper;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by erhan.karakaya on 3/10/2017.
 */
public class MapperUtil {

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


}
