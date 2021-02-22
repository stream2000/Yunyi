package net.yunyi.back.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author stream2000
 * @since 2021-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer uploaderId;

    private String title;

    private String body;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后一次修改时间
     */
    private LocalDateTime updateTime;


}
