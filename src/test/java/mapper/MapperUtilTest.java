package mapper;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapper.exception.ResultTypeInstantiationException;
import mapper.packagefortesting.TestChildClass;
import mapper.packagefortesting.TestHasNoSuperClass;
import mapper.packagefortesting.TestHasNotPublicNoArgsConstructorClass;
import mapper.packagefortesting.TestSuperClass;
import mapper.util.MapperUtil;

import org.junit.Test;


/**
 * Created by erhan.karakaya on 3/10/2017.
 */
public class MapperUtilTest {

  @Test
  public void shoulGetAllFields_whenHasSuperClass_returnsAllFields() {
    List<Field> fields = MapperUtil.getAllFields(TestChildClass.class);
    final int expectedTotalFieldsSize = 4;

    assertEquals(expectedTotalFieldsSize, fields.size());
  }

  @Test
  public void shouldGetAllFields_whenHasSuperClass_returnsCorrectFieldNamesAndTypes() {

    final HashMap<String, Type> expectedFieldNames = new HashMap<>();
    expectedFieldNames.put("superField1", String.class);
    expectedFieldNames.put("superField2", Integer.class);
    expectedFieldNames.put("childField1", Double.class);
    expectedFieldNames.put("childField2", List.class);

    List<Field> fields = MapperUtil.getAllFields(TestChildClass.class);

    HashMap<String, Type> actualFieldNames = new HashMap<>();
    fields.forEach(field -> actualFieldNames.put(field.getName(), field.getType()));

    assertEquals(expectedFieldNames, actualFieldNames);
  }

  @Test
  public void shoulGetAllFields_whenHasNotSuperClass_returnsAllFields() {
    List<Field> fields = MapperUtil.getAllFields(TestSuperClass.class);
    final int expectedTotalFieldsSize = 2;

    assertEquals(expectedTotalFieldsSize, fields.size());
  }


  @Test
  public void shoulGetAllFields_whenHasNotSuperClass_returnsCorrectFieldNamesAndTypes() {
    List<Field> fields = MapperUtil.getAllFields(TestSuperClass.class);

    final HashMap<String, Type> expectedFieldNames = new HashMap<>();
    expectedFieldNames.put("superField1", String.class);
    expectedFieldNames.put("superField2", Integer.class);

    HashMap<String, Type> actualFieldNames = new HashMap<>();
    fields.forEach(field -> actualFieldNames.put(field.getName(), field.getType()));

    assertEquals(expectedFieldNames, actualFieldNames);
  }


  @Test
  public void shoulGetFieldsMap_whenHasSuperClass_returnsCorrectMap()
      throws IllegalAccessException {
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

  @Test
  public void shoulGetFieldsMap_whenHasNotSuperClass_returnsCorrectMap()
      throws IllegalAccessException {
    final String superField1Value = "string";
    final Integer superField2Value = 1;

    Map<String, Object> expectedMap = new HashMap<>();
    expectedMap.put("superField1", superField1Value);
    expectedMap.put("superField2", superField2Value);


    TestSuperClass testSuperObj = new TestSuperClass();
    testSuperObj.setSuperField1(superField1Value);
    testSuperObj.setSuperField2(superField2Value);

    Map<String, Object> actualMap = MapperUtil.getFieldsMap(testSuperObj);

    assertEquals(expectedMap, actualMap);
  }


  @Test(expected = ResultTypeInstantiationException.class)
  public void shoulThrowException_whenResultTypeHasNotPublicNoArgConstructor()
      throws ResultTypeInstantiationException {

    TestChildClass testChildObj = new TestChildClass();
    testChildObj.setSuperField1("string");
    testChildObj.setSuperField2(1);

    try {
      TestHasNotPublicNoArgsConstructorClass result =
          MapperUtil.map(testChildObj, TestHasNotPublicNoArgsConstructorClass.class);
      fail("map method should throw exception");
    } catch (ResultTypeInstantiationException e) {
      throw e;
    }
  }

  @Test
  public void shouldMapAllFieldsMatchedByNameAndByType_whenResultTypeHasPublicNoArgConstructor() {
    final Double childField1Value = 3.0;
    final List<Integer> childField2Value = new ArrayList<>();
    childField2Value.add(30);

    TestChildClass testChildObj = new TestChildClass();
    testChildObj.setChildField1(childField1Value);
    testChildObj.setChildField2(childField2Value);

    try {
      TestHasNoSuperClass testHasNoSuperClassObj =
          MapperUtil.map(testChildObj, TestHasNoSuperClass.class);
      assertEquals(childField1Value, testHasNoSuperClassObj.getChildField1());
      assertEquals(childField2Value, testHasNoSuperClassObj.getChildField2());
    } catch (ResultTypeInstantiationException e) {
      fail();
    }

  }

  @Test
  public void shouldNotMapAnyFieldNotMatchedByNameAndByType_whenResultTypeHasPublicNoArgCons() {
    final String superField1Value = "string";

    TestChildClass testChildObj = new TestChildClass();
    testChildObj.setSuperField1(superField1Value);

    try {
      TestHasNoSuperClass testHasNoSuperClassObj =
          MapperUtil.map(testChildObj, TestHasNoSuperClass.class);
      assertNull(testHasNoSuperClassObj.getSuperField1());
    } catch (ResultTypeInstantiationException e) {
      fail();
    }

  }


}