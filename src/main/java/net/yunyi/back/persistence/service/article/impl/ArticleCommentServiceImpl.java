package net.yunyi.back.persistence.service.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.persistence.entity.ArticleComment;
import net.yunyi.back.persistence.entity.ArticleStats;
import net.yunyi.back.persistence.mapper.ArticleCommentMapper;
import net.yunyi.back.persistence.service.article.IArticleCommentService;
import net.yunyi.back.persistence.service.article.IArticleStatsService;
import net.yunyi.back.persistence.vo.ArticleCommentVo;
import net.yunyi.back.persistence.vo.RefArticleComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-06
 */
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment> implements IArticleCommentService {
	@Autowired
	IArticleStatsService articleStatsService;

	private int getArticleCommentFloor(int articleId) {
		return count(new QueryWrapper<ArticleComment>().eq("article_id", articleId));
	}

	private RefArticleComment mapArticleCommentToRef(ArticleCommentVo vo) {
		if (vo == null) {
			return null;
		}
		return new RefArticleComment(vo.getId(), vo.getSender_id(), vo.getSenderName(), vo.getContent(), vo.getSendTime(), vo.getFloor(), vo.isHasRefComment(), vo.getRefCommentId());
	}

	@Override
	@Transactional
	public ArticleCommentVo addArticleComment(final int senderId, final int articleId, final String content, final boolean hasRefComment, final int refCommentId) {
		ArticleComment comment = new ArticleComment();
		comment.setArticleId(articleId);
		comment.setHasRefComment(hasRefComment);
		comment.setRefCommentId(refCommentId);
		comment.setContent(content);
		comment.setSenderId(senderId);
		comment.setFloor(getArticleCommentFloor(articleId));
		save(comment);

		ArticleStats stats = articleStatsService.getByArticleId(articleId);
		stats.setCommentNum(stats.getCommentNum() + 1);
		articleStatsService.updateById(stats);
		ArticleCommentVo result = baseMapper.getArticleComment(comment.getId().intValue());
		if (hasRefComment) {
			result.setRefComment(mapArticleCommentToRef(baseMapper.getArticleComment(refCommentId)));
		}
		return result;
	}

	@Override
	@Transactional
	public boolean deleteArticleComment(final int commentId) {
		ArticleComment comment = getById(commentId);
		boolean result = removeById(commentId);
		if (!result) {
			return false;
		}
		ArticleStats stats = articleStatsService.getByArticleId(comment.getArticleId());
		stats.setCommentNum(stats.getCommentNum() - 1);
		articleStatsService.updateById(stats);
		return true;
	}

	@Override
	public IPage<ArticleCommentVo> getArticleComment(final Page<ArticleCommentVo> page, final int articleId) {
		IPage<ArticleCommentVo> result = baseMapper.getArticleComments(page, articleId);
		for (ArticleCommentVo commentVo : result.getRecords()) {
			if (commentVo.isHasRefComment()) {
				ArticleCommentVo refComment = baseMapper.getArticleComment(commentVo.getRefCommentId());
				commentVo.setRefComment(mapArticleCommentToRef(refComment));
			}
		}
		return result;
	}
}
