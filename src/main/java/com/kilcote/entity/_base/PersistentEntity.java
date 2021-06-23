package com.kilcote.entity._base;

import javax.persistence.MappedSuperclass;

import org.springframework.data.jpa.domain.AbstractPersistable;

@MappedSuperclass
public class PersistentEntity  extends AbstractPersistable<Long> implements DomainEntity {
	
	private static final long serialVersionUID = -6205897787347868691L;

	public void setId(Long id) {
		super.setId(id);
	}
	
	public Long getId() {
		return super.getId();
	}
}
