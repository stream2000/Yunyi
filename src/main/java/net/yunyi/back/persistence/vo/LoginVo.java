package net.yunyi.back.persistence.vo;

import lombok.Data;
import net.yunyi.back.persistence.entity.User;

@Data
public class LoginVo {

	private User user;
	private String jwt;
	private boolean newUser;
}
