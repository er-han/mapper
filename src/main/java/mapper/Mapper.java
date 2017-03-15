package mapper;

import mapper.exception.ResultTypeInstantiationException;
import mapper.util.MapperUtil;

/**
 * Created by erhan.karakaya on 3/10/2017.
 */
public class Mapper implements MapTo {
  private Mappable source;
  private Mapper(Mappable source) {
    this.source = source;
  }


  public static MapTo getMapperFrom(Mappable source) {
    return new Mapper(source);
  }


  @Override
  public <T extends Mappable> T mapTo(Class<T> resultType) throws ResultTypeInstantiationException {
    return MapperUtil.map(source, resultType);
  }
}
