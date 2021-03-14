package net.yunyi.back.persistence.service.trans.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.persistence.entity.TransSegStats;
import net.yunyi.back.persistence.mapper.TransSegStatsMapper;
import net.yunyi.back.persistence.service.trans.ITransSegStatsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 翻译统计表 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-14
 */
@Service
public class TransSegStatsServiceImpl extends ServiceImpl<TransSegStatsMapper, TransSegStats> implements ITransSegStatsService {

}
