package mapper.packagefortesting;

import mapper.Mappable;

import java.util.List;

/**
 * Created by erhan.karakaya on 3/14/2017.
 */
public class TestHasNoSuperClass implements Mappable {
  private Double childField1;
  private List<Integer> childField2;

  public Double getChildField1() {
    return childField1;
  }

  public void setChildField1(Double childField1) {
    this.childField1 = childField1;
  }

  public List<Integer> getChildField2() {
    return childField2;
  }

  public void setChildField2(List<Integer> childField2) {
    this.childField2 = childField2;
  }
}
