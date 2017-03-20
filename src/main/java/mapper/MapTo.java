package mapper;

import java.util.function.Supplier;

import mapper.exception.ResultTypeInstantiationException;


/**
 * This interface is used in Mapper class to
 * supply more readible code.
 */
public interface MapTo {
  <T extends Mappable> T mapTo(Class<T> resultType) throws ResultTypeInstantiationException;

  <T extends Mappable> T mapTo(Supplier<T> supplier);
}
