package com.reece.platform.eclipse.model.DTO;

import com.reece.platform.eclipse.model.generated.DocumentImagingFile;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DocumentImagingFileDTO {
    private String updateKey;
    private String id;
    private String filename;

    public DocumentImagingFileDTO(DocumentImagingFile documentImagingFile) {
        this.updateKey = documentImagingFile.getUpdateKey();
        this.id = documentImagingFile.getId();
        this.filename = documentImagingFile.getFileInformation().getOriginalFilename();
    }
}
