package net.yunyi.back.persistence.param;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;

@Data
public class ModifyUserParam {

	int age;
	@Nullable
	private String nickName;
	@Email
	@Nullable
	private String email;
}
