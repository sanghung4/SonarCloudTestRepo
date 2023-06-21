package com.reece.platform.accounts.model.entity;

import lombok.*;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@RequiredArgsConstructor
@Entity(name = "CustomRevisionEntity")
@Table(name = "custom_revinfo")
@RevisionEntity( CustomRevisionEntityListener.class )
public class CustomRevisionEntity extends DefaultRevisionEntity {
    private String email;
}
