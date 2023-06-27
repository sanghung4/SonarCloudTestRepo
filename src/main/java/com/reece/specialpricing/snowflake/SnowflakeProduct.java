package com.reece.specialpricing.snowflake;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "product", schema = "consolidation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SnowflakeProduct {
    @Id
    @Column(name = "PRODUCTID")
    private String id;

    @Column(name ="PRODDESC")
    private String name;
}
