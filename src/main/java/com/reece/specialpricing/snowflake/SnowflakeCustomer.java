package com.reece.specialpricing.snowflake;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "customer", schema = "consolidation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SnowflakeCustomer {
    @Id
    @Column(name = "CUSTOMERID")
    private String id;

    @Column(name ="CUSTOMERNAME")
    private String name;
}
