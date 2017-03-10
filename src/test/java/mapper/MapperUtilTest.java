package mapper;

import mapper.packagefortesting.TestChildClass;
import mapper.packagefortesting.TestSuperClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by erhan.karakaya on 3/10/2017.
 */
public class MapperUtilTest {

  @Test
  public void getAllFields_whenHasSuperClass_getsSuperFields_returnsAllFields() {
    List<Field> fields = MapperUtil.getAllFields(TestChildClass.class);
    final int expectedTotalFieldsSize = 4;

    assertEquals(expectedTotalFieldsSize, fields.size());
  }

  @Test
  public void getAllFields_whenHasSuperClass_getsSuperFields_returnsCorrectFieldNamesAndTypes() {
    List<Field> fields = MapperUtil.getAllFields(TestChildClass.class);

    final HashMap<String, Type> expectedFieldNames = new HashMap<>();
    expectedFieldNames.put("superField1", String.class);
    expectedFieldNames.put("superField2", Integer.class);
    expectedFieldNames.put("childField1", Double.class);
    expectedFieldNames.put("childField2", List.class);

    HashMap<String, Type> actualFieldNames = new HashMap<>();
    fields.forEach(field -> actualFieldNames.put(field.getName(), field.getType()));

    assertEquals(expectedFieldNames, actualFieldNames);
  }

  @Test
  public void getAllFields_whenHasNotSuperClass_returnsAllFields() {
    List<Field> fields = MapperUtil.getAllFields(TestSuperClass.class);
    final int expectedTotalFieldsSize = 2;

    assertEquals(expectedTotalFieldsSize, fields.size());
  }


  @Test
  public void getAllFields_whenHasNotSuperClass_returnsCorrectFieldNamesAndTypes() {
    List<Field> fields = MapperUtil.getAllFields(TestSuperClass.class);

    final HashMap<String, Type> expectedFieldNames = new HashMap<>();
    expectedFieldNames.put("superField1", String.class);
    expectedFieldNames.put("superField2", Integer.class);

    HashMap<String, Type> actualFieldNames = new HashMap<>();
    fields.forEach(field -> actualFieldNames.put(field.getName(), field.getType()));

    assertEquals(expectedFieldNames, actualFieldNames);
  }


  //todo: add public, protected and package private fields into test classes and write test methods for each access level

}