package nian.shop.entity;

import java.util.Date;

import lombok.Data;

@Data
public class SecondUser {
	private Long id;
	private String nickname;
	private String password;
	private String salt;
	private String head;
	private Date registerDate;
	private Date lastLoginDate;
	private Integer loginCount;
}
