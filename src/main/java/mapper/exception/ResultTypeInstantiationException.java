package mapper.exception;

/**
 * Created by erhan.karakaya on 3/14/2017.
 */
public class ResultTypeInstantiationException extends Exception {

  private String resultTypeInstantiationExceptionMessage = "Can not instantiate a new object of given result type: %s. The result type must have a public no-args constructor.";


  public ResultTypeInstantiationException(Class<?> resultType) {
    String message = String.format(resultTypeInstantiationExceptionMessage, resultType.getTypeName());
    new ResultTypeInstantiationException(message);
  }

  public ResultTypeInstantiationException(Class<?> resultType, Throwable cause) {
    String message = String.format(resultTypeInstantiationExceptionMessage, resultType.getTypeName());
    new ResultTypeInstantiationException(message, cause);
  }

  public ResultTypeInstantiationException(String message) {
    super(message);
  }

  public ResultTypeInstantiationException(String message, Throwable cause) {
    super(message, cause);
  }

}
