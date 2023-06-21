package com.reece.platform.products.model.DTO;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoriesDTO implements Serializable {

    public List<CategoryDTO> categories;
}
