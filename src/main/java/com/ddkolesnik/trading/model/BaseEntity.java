package com.ddkolesnik.trading.model;

import com.ddkolesnik.trading.configuration.security.SecurityUtils;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

/**
 * @author Alexandr Stegnin
 */

@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    private Long id;

    @Column(name = "creation_time")
    private Instant creationTime;

    @Column(name = "modified_time")
    private Instant modifiedTime;

    @Column(name = "modified_by")
    private String modifiedBy;

    @PrePersist
    public void prePersist() {
        this.creationTime = Instant.now();
        this.modifiedBy = SecurityUtils.getUsername();
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedTime = Instant.now();
        this.modifiedBy = SecurityUtils.getUsername();
    }

}
