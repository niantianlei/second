package nian.shop.DTO;

import java.io.Serializable;

import nian.shop.utils.ResultCode;
/**
 * 
 * @author Niantianlei
 * @date 2018年5月18日 下午3:15:20
 * @param <T>
 */
public class ResultDTO<T> implements Serializable {
	
	private static final long serialVersionUID = -3091492566196534172L;
	private int code;
	private String msg;
	private T data;
	
	public static <T> ResultDTO<T> success(T data) {
		return new ResultDTO<T>(data);
	}
	
	public static <T> ResultDTO<T> error(String msg) {
		return error(ResultCode.ERROR.getCode(), msg);
	}
	public static <T> ResultDTO<T> error(Integer code, String msg) {
		ResultDTO<T> resultDTO = new ResultDTO<>();
		resultDTO.setCode(code);
		resultDTO.setMsg(msg);
		return resultDTO;
	}
	private ResultDTO(T data) {
		this.code = 0;
		this.msg = "success";
		this.data = data;
	}
	private ResultDTO() {
		
	}
	
	public ResultDTO(int code, String msg, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	
}
