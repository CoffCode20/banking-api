package co.istad.spring_boot_2.dto.response;

import lombok.Builder;

@Builder
public record MediaResponse (
        String name,
        String extension,
        String uri,
        String mimeTypeFile,
        Long size
) {
}
