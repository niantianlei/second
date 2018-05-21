package nian.shop.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import nian.shop.DTO.ResultDTO;
import nian.shop.utils.ResultCode;



@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
	//拦截所有exception
	@ExceptionHandler(value=Exception.class)
	public ResultDTO<String> exceptionHandler(HttpServletRequest request, Exception e){
		e.printStackTrace();
		if(e instanceof SecondException) {
			SecondException ex = (SecondException)e;
			return ResultDTO.error(ex.getResultDTO().getCode(), ex.getResultDTO().getMsg());
		} else if(e instanceof BindException) {
			BindException ex = (BindException)e;
			List<ObjectError> errors = ex.getAllErrors();
			ObjectError error = errors.get(0);
			String msg = error.getDefaultMessage();
			return ResultDTO.error(ResultCode.REQUEST_ERROR.getCode(), msg);
		} else {
			return ResultDTO.error(ResultCode.SERVER_ERROR.getCode(), "");
		}
	}
}