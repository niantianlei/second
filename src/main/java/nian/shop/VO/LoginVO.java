package nian.shop.VO;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import nian.shop.validator.IsMobile;

@Data
public class LoginVO {
	@NotNull
	@IsMobile
	private String mobile;
	@NotNull
	@Length(min=32)
	private String password;
	
	
}
