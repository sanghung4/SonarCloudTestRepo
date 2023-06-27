package com.reece.specialpricing.postgres;

import com.reece.specialpricing.model.DynamicSortable;
import com.reece.specialpricing.snowflake.SNOWFLAKE_IMPORT_STATUS;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "product", schema="special_pricing")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product implements DynamicSortable {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SNOWFLAKE_IMPORT_STATUS status;

    @Column(name="error_details")
    private String errorDetails;

    @Column(name="created_at",nullable = false)
    private Date createdAt;

    @Column(name="updated_at")
    private Date updatedAt;

    public Product(String id, String name) {
        this.id=id;
        this.displayName=name;
    }

    public String getComparisonField(String fieldName){
        if ("id" .equals(fieldName)) {
            return this.getId();
        }
        return this.getDisplayName();
    }
}
