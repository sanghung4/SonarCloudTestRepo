package com.reece.specialpricing.postgres;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "branch", schema="special_pricing")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Branch {

    @Id
    @Column(name = "id")
    private String id;
}
