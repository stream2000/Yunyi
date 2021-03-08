package net.yunyi.back.persistence.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.yunyi.back.persistence.entity.TransStats;
import net.yunyi.back.persistence.mapper.TransStatsMapper;
import net.yunyi.back.persistence.service.ITransStatsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 翻译统计表 服务实现类
 * </p>
 *
 * @author stream2000
 * @since 2021-03-07
 */
@Service
public class TransStatsServiceImpl extends ServiceImpl<TransStatsMapper, TransStats> implements ITransStatsService {

}
