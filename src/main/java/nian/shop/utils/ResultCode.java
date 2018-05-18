package nian.shop.utils;

public enum ResultCode {
	SUCCESS(0), FAIL(1), ERROR(8000);
	private int code;
	private ResultCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}
}
