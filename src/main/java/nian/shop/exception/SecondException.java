package nian.shop.exception;

import nian.shop.DTO.ResultDTO;

public class SecondException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private ResultDTO<?> resultDTO;
	
	public SecondException(ResultDTO<?> res) {
		super(res.toString());
		resultDTO = res;
	}

	public ResultDTO<?> getResultDTO() {
		return resultDTO;
	}
}
