package com.andon.springbootutil.entity;

import com.andon.springbootutil.constant.DataType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Andon
 * 2022/10/26
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "h2_table")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class H2Table implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36) COMMENT 'ID'", updatable = false, nullable = false, unique = true)
    String id;

    @Column(name = "name", columnDefinition = "VARCHAR(255) COMMENT '名称'", nullable = false, unique = true)
    String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(100) COMMENT '类型'", nullable = false)
    DataType dataType;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE COMMENT '是否是调试数据'")
    Boolean isDebug;

    @Column(name = "remark", columnDefinition = "VARCHAR(255) COMMENT '备注'")
    String remark;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    Date createTime;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    Date updateTime;
}
