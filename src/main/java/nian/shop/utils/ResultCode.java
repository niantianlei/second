package nian.shop.utils;

public enum ResultCode {
	SUCCESS(0, "success"), 
	FAIL(1, "fail"), 
	REQUEST_ERROR(400, "客户端错误"), 
	SERVER_ERROR(500, "服务端错误"),;
	
	private int code;
	private String msg;
	
	private ResultCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
}
