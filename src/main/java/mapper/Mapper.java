package mapper;

import mapper.exception.ResultTypeInstantiationException;
import mapper.util.MapperUtil;

/**
 * Mapper is used for mapping from a Mappable source object
 * to another Mappable object in an easy and readible way.
 */
public class Mapper implements MapTo {
  private Mappable source;
  private Mapper(Mappable source) {
    this.source = source;
  }


  /**
   * Creates a new Mapper instance.
   * @param source Mappable object which will be used
   *               as source when mapping.
   * @return a new Mapper instance.
   */
  public static MapTo getMapperFrom(Mappable source) {
    return new Mapper(source);
  }


  /**
   * Maps the given source Mappable object
   * to a newly instantiated object of the given resultType.
   * @param resultType Target object's class.
   *                   Result type must have a public no-arg
   *                   constructor. Otherwise a ResultTypeInstantiationException
   *                   will be thrown.
   * @param <T> Target type which implements Mappable
   * @return a newly instantiated object of type T,
   * mapped from source object.
   * @throws ResultTypeInstantiationException
   */
  @Override
  public <T extends Mappable> T mapTo(Class<T> resultType) throws ResultTypeInstantiationException {
    return MapperUtil.map(source, resultType);
  }
}
