package mapper;

import java.util.List;
import java.util.function.Supplier;

import mapper.exception.ResultTypeInstantiationException;


/**
 * Created by erhan.karakaya on 3/20/2017.
 */
public interface MapToList {
  <T extends Mappable> Iterable<T> mapToList(Class<T> resultType)
      throws ResultTypeInstantiationException;

  <T extends Mappable> Iterable<T> mapToList(Supplier<T> supplier);
}
