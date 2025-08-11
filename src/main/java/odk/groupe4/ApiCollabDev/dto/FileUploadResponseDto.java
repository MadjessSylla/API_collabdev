package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class FileUploadResponseDto {
    private String fileName;
    private String url;
    private long size;
    private String contentType;
}
