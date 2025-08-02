package co.istad.spring_boot_2.controller;

import co.istad.spring_boot_2.dto.response.MediaResponse;
import co.istad.spring_boot_2.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/medias/file")
    public MediaResponse upload(@RequestPart MultipartFile file) {
        return mediaService.fileUpload(file);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/medias/files")
    public List<MediaResponse> uploads(@RequestPart List<MultipartFile> files) {
        return mediaService.filesUpload(files);
    }


    @GetMapping("/media/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        return mediaService.downloadMedia(filename);
    }

    @DeleteMapping("/media/remove/{filename:.+}")
    public ResponseEntity<?> deleteFile(@PathVariable String filename) {
        mediaService.deleteMedia(filename);
        return ResponseEntity.noContent().build();
    }


}
