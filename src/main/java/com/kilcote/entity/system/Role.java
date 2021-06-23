package com.kilcote.entity.system;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kilcote.entity._base.GenericEntity;
import com.kilcote.utils.DateUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "t_role")
public class Role extends GenericEntity {

    @Column(name="role_name", nullable=false, length=64)
    private String roleName;

    @Column(name="remark", length=128)
    private String remark;

    @Column(name="create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern=DateUtil.FULL_TIME_SPLIT_PATTERN)
    private LocalDateTime createTime;

    @Column(name="modify_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern=DateUtil.FULL_TIME_SPLIT_PATTERN)
    private LocalDateTime modifyTime;

    @Transient
    private transient List<Long> menuIds;
    
}