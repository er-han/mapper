package mapper;

import java.io.Serializable;

import mapper.exception.ResultTypeInstantiationException;


/**
 * To use Mapper, both source and target classes
 * must implement Mappable interface.
 */
public interface Mappable extends Serializable {
  default <T extends Mappable> T mapTo(Class<T> resultType)
      throws ResultTypeInstantiationException {
    return Mapper.getMapperFrom(this).mapTo(resultType);
  }
}
