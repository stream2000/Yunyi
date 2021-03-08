package net.yunyi.back.persistence.service.trans;

import com.baomidou.mybatisplus.extension.service.IService;
import net.yunyi.back.persistence.entity.ArticleTrans;
import net.yunyi.back.persistence.param.UploadTransParam;

/**
 * <p>
 * 原文切分的翻译表 服务类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
public interface IArticleTransService extends IService<ArticleTrans> {
	int uploadTranslation(int userId, UploadTransParam param);

	boolean deleteTranslation(int userId, int transId);
}
