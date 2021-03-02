package net.yunyi.back.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

// TODO: fix bug here
//完成自动填充功能
@Component//将对象交给spring容器管理
public class MyMetaObjectHandler implements MetaObjectHandler {
	/**
	 * 在pojo中添加了 新增更新的注解,但是必须在数据库的字段中完成赋值的操作
	 * 所以 必须明确 新增更新时操作的是那个字段,以及值是多少
	 *
	 * @param metaObject
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		this.setInsertFieldValByName("create_time", new Date(), metaObject);
		this.setInsertFieldValByName("update_time", new Date(), metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.setUpdateFieldValByName("update_time", new Date(), metaObject);
	}
}