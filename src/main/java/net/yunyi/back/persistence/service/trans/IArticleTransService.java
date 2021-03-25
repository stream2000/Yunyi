package net.yunyi.back.persistence.service.trans;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.yunyi.back.persistence.entity.ArticleTrans;
import net.yunyi.back.persistence.param.UploadTransParam;
import net.yunyi.back.persistence.vo.SimpleTranslationVo;
import net.yunyi.back.persistence.vo.TranslationDetailVo;

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

	int modifyTranslation(int transId, UploadTransParam param);

	boolean deleteTranslation(int userId, int transId);

	SimpleTranslationVo getTranslation(int transId);

	SimpleTranslationVo getBestTranslationForArticle(int articleID);

	IPage<SimpleTranslationVo> getTranslations(int articleId, final Page<SimpleTranslationVo> page);

	boolean likeTrans(int transId, final int userId);

	boolean cancelLikeTrans(int transId, final int userId);

	TranslationDetailVo getTranslationDetail(int transId);

	boolean likeTransSeg(int userId, int transSegId);

	boolean cancelLikeTransSeg(int userId, int transSegId);

}
