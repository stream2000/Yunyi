package net.yunyi.back.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 翻译统计表
 * </p>
 *
 * @author stream2000
 * @since 2021-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TransSegStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "trans_seg_id", type = IdType.INPUT)
    private Integer transSegId;

    private Integer likeNum;

    private Integer commentNum;


}
