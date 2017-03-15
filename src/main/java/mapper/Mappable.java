package mapper;

import mapper.exception.ResultTypeInstantiationException;

import java.io.Serializable;

/**
 * Created by erhan.karakaya on 3/10/2017.
 */
public interface Mappable extends Serializable {
  default <T extends Mappable> T mapTo(Class<T> resultType) throws ResultTypeInstantiationException {
    return Mapper.getMapperFrom(this).mapTo(resultType);
  }
}
