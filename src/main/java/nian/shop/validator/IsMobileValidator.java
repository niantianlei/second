package nian.shop.validator;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import nian.shop.utils.ValidatorUtil;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

	private boolean required = false;
	
	public void initialize(IsMobile constraintAnnotation) {
		required = constraintAnnotation.required();
	}

	//校验逻辑
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(required) {
			return ValidatorUtil.isMobile(value);
		}else {
			if(value == null || value.length() == 0) {
				return true;
			}else {
				return ValidatorUtil.isMobile(value);
			}
		}
	}

}
