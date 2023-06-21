package com.reece.platform.products.model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO implements Serializable {

    public String name;
    public List<CategoryDTO> children;

    public CategoryDTO(String name) {
        this.name = name;
    }

    public boolean isVisibleCategory() {
        return (
            !this.getName().equals("TBC") && !this.getName().equals("CATCLEAN") && !this.getName().equals("Nonstock")
        );
    }
}
