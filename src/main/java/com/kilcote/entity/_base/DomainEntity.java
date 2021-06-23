package com.kilcote.entity._base;

import org.springframework.data.domain.Persistable;
import java.io.Serializable;

public interface DomainEntity extends Serializable, Persistable<Long> {
}
