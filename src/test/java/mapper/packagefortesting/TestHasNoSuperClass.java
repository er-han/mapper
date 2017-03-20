package mapper.packagefortesting;

import java.util.List;

import mapper.Mappable;

/**
 * Created by erhan.karakaya on 3/14/2017.
 */
public class TestHasNoSuperClass implements Mappable {
  private Double childField1;
  private List<Integer> childField2;
  private Integer superField1;

  public TestHasNoSuperClass(){
    //no-args constructor
  }


  public TestHasNoSuperClass(Double childField1){
    this.childField1 = childField1;
  }


  public Integer getSuperField1() {
    return superField1;
  }

  public void setSuperField1(Integer superField1) {
    this.superField1 = superField1;
  }

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
