package com.kilcote.entity._base;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class GenericEntity {

	@Id
	@Column(name="id", unique = true, nullable = false, length=32)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
}
