package net.yunyi.back.persistence.param;

import lombok.Data;

@Data
public class ModifyUserParam {

	String nickName;
	String email;
	int age;
}
