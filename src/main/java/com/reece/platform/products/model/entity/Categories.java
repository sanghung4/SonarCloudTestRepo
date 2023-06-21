package com.reece.platform.products.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Deprecated(since = "0.94", forRemoval = true)
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Categories {

    @Id
    @Column(name = "erp")
    private String erp;

    @Column(name = "value")
    private String value;
}
