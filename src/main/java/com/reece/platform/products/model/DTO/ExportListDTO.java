package com.reece.platform.products.model.DTO;

import com.reece.platform.products.model.UploadListData;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportListDTO {

    private List<UploadListData> listLineItems;
}
