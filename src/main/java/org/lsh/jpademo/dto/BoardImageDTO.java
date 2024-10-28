package org.lsh.jpademo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lsh.jpademo.dto.upload.UploadResultDTO;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardImageDTO {
    private String uuid;
    private List<UploadResultDTO> fileName;
    private int ord;
    private boolean isImage;
}
