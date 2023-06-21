package com.reece.platform.products.model.DTO;

import com.reece.platform.products.helpers.ERPSystem;
import com.reece.platform.products.model.entity.Categories;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Deprecated
public class CategoriesDTODeprecated {

    private String eclipseCategories;
    private String mincronCategories;

    public CategoriesDTODeprecated(ERPSystem erpSystem, String value) {
        if (erpSystem.equals(ERPSystem.ECLIPSE)) {
            this.eclipseCategories = value;
        } else {
            this.mincronCategories = value;
        }
    }

    public CategoriesDTODeprecated(List<Categories> allCategories) {
        for (Categories category : allCategories) {
            if (category.getErp().equals(ERPSystem.ECLIPSE.name())) {
                this.eclipseCategories = category.getValue();
            } else {
                this.mincronCategories = category.getValue();
            }
        }
    }
}
