package mapper;

import mapper.exception.ResultTypeInstantiationException;

/**
 * Created by erhan.karakaya on 3/15/2017.
 */
public interface MapTo {
  <T extends Mappable> T mapTo(Class<T> resultType) throws ResultTypeInstantiationException;
}
