package cn.tedu.store.util;

import java.io.Serializable;
//服务器端对客户端响应的类型
public class ResponseResult<T> implements Serializable{
	
	private static final long serialVersionUID = -7057704594570040634L;
		private Integer state;
		private String message;
		//<T>泛型占位符,占位符可以是任意的名称，但最好不要是已识别的类型
		private T data;
		public ResponseResult() {
			super();
		}
		
		public ResponseResult(Integer state) {
			super();
			this.state = state;
			setState(state);
		}
		
		public ResponseResult(Integer state, String message) {
			super();
			this.state = state;
			setMessage(message);
		}
		
		public ResponseResult(Integer state,Exception e) {
			this(state,e.getMessage());
		}
		
		public ResponseResult(Integer state, T data) {
			this(state);
			setData(data);
		}

		public Integer getState() {
			return state;
		}
		public void setState(Integer state) {
			this.state = state;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public T getData() {
			return data;
		}
		public void setData(T data) {
			this.data = data;
		}
		@Override
		public String toString() {
			return "ResponseResult [state=" + state + ", message=" + message + ", data=" + data + "]";
		}
		
}
