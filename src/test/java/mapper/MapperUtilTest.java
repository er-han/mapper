package mapper;

import mapper.packagefortesting.TestChildClass;
import mapper.packagefortesting.TestSuperClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


  @Test
  public void getFieldsMap_whenHasSuperClass_returnsCorrectMap() throws IllegalAccessException {
    final String superField1Value = "string";
    final Integer superField2Value = 1;
    final Double childField1Value = 3.0;
    final List<Integer> childField2Value = new ArrayList<>();
    childField2Value.add(30);

    Map<String, Object> expectedMap = new HashMap<>();
    expectedMap.put("superField1", superField1Value);
    expectedMap.put("superField2", superField2Value);
    expectedMap.put("childField1", childField1Value);
    expectedMap.put("childField2", childField2Value);


    TestChildClass testChildObj = new TestChildClass();
    testChildObj.setSuperField1(superField1Value);
    testChildObj.setSuperField2(superField2Value);
    testChildObj.setChildField1(childField1Value);
    testChildObj.setChildField2(childField2Value);

    Map<String, Object> actualMap = MapperUtil.getFieldsMap(testChildObj);

    assertEquals(expectedMap, actualMap);

  }


  //todo: add public, protected and package private fields into test classes and write test methods for each access level

}