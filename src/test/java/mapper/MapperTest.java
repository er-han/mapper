package mapper;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mapper.exception.ResultTypeInstantiationException;
import mapper.packagefortesting.TestChildClass;
import mapper.packagefortesting.TestHasNoSuperClass;

import org.junit.Before;
import org.junit.Test;


/**
 * Created by erhan.karakaya on 3/15/2017.
 */
public class MapperTest {

  private TestChildClass mapFromObj;
  private TestHasNoSuperClass mapToObj;

  private static final String superField1Value = "STR";
  private static final Integer superField2Value = 99;
  private static final Double childField1Value = 120.3;
  private static final List<Integer> childField2Value =
      new ArrayList<>(Arrays.asList(6, 8, 9, 445));

  /**
   * Setting mapFromObj with initial values.
   */
  @Before
  public void setUp() {
    mapFromObj = new TestChildClass();
    mapFromObj.setSuperField1(superField1Value);
    mapFromObj.setSuperField2(superField2Value);
    mapFromObj.setChildField1(childField1Value);
    mapFromObj.setChildField2(childField2Value);
  }

  @Test
  public void shouldReturnMapperFromInstantce_whenGetMapperIsInvoked() {
    try {
      mapToObj = Mapper.getMapperFrom(mapFromObj).mapTo(TestHasNoSuperClass.class);

      assertNotNull(mapFromObj);
    } catch (ResultTypeInstantiationException e) {
      fail();
      e.printStackTrace();
    }
  }
}