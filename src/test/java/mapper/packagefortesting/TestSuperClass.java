package mapper.packagefortesting;

import mapper.Mappable;

/**
 * Created by erhan.karakaya on 3/10/2017.
 */
public class TestSuperClass implements Mappable {
  private String superField1;
  private Integer superField2;

  public String getSuperField1() {
    return superField1;
  }

  public void setSuperField1(String superField1) {
    this.superField1 = superField1;
  }

  public Integer getSuperField2() {
    return superField2;
  }

  public void setSuperField2(Integer superField2) {
    this.superField2 = superField2;
  }
}
