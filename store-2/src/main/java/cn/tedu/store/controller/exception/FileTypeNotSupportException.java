package cn.tedu.store.controller.exception;

public class FileTypeNotSupportException extends FileUploadException{

	private static final long serialVersionUID = -1919934638257089254L;

	public FileTypeNotSupportException() {
		super();
	}

	public FileTypeNotSupportException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FileTypeNotSupportException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileTypeNotSupportException(String message) {
		super(message);
	}

	public FileTypeNotSupportException(Throwable cause) {
		super(cause);
	}

}
