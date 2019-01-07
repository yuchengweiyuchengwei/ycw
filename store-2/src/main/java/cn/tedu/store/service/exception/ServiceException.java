package cn.tedu.store.service.exception;
/**
 * 业务异常，是当前项目中生成所有异常的基类
 * @author UID-ECD
 *
 */
public class ServiceException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5631578415492734367L;

	public ServiceException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ServiceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
