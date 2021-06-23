package com.kilcote.entity.system;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kilcote.entity._base.GenericEntity;
import com.kilcote.utils.DateUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author Shun fu
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name="t_log")
public class Log extends GenericEntity {

	/**
	 * Operating user
	 */
	@Column(name="email", nullable=false, length=64)
	private String email;

	/**
	 * Operation content
	 */
	@Column(name="operation", nullable=false, length=256)
	private String operation;

	/**
	 * Time consuming
	 */
	@Column(name="time", nullable=true)
	private Long time;

	/**
	 * Operation method
	 */
	@Column(name="method", nullable=false, length=256)
	private String method;

	/**
	 * Method parameters
	 */
	@Column(name="params", nullable=true, length=1024)
	private String params;

	/**
	 * Operator IP
	 */
	@Column(name="ip", nullable=true, length=64)
	private String ip;

	/**
	 * CreateTime
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern=DateUtil.FULL_TIME_SPLIT_PATTERN)
	@Column(name="create_time")
	private LocalDateTime createTime;

	/**
	 * Operating location
	 */
	@Column(name="location", nullable=true, length=256)
	private String location;
}
