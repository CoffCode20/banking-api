package co.istad.spring_boot_2.service;

import co.istad.spring_boot_2.domain.Media;
import co.istad.spring_boot_2.dto.response.MediaResponse;
import co.istad.spring_boot_2.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaService {

    private final MediaRepository mediaRepository;

    @Value("${media.server-path}")
    private String serverPath;

    @Value("${media.base-uri}")
    private String baseUri;

    // upload one file
    public MediaResponse fileUpload(MultipartFile file) {

        // 1. Save file to file system path
        String fileName = UUID.randomUUID().toString();
        int lastIndex = Objects.requireNonNull(file.getOriginalFilename())
                .lastIndexOf(".");
        String extension = file.getOriginalFilename()
                .substring(lastIndex + 1);
        // Create path object
        Path path = Paths.get(serverPath + String.format("%s.%s",
                fileName, extension));

        // try catch for copy binary code file image
        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "File upload failed");
        }

        // 2. Save metadata of media into database
        Media media = new Media();
        media.setName(fileName);
        media.setExtension(extension);
        media.setMimeTypeFile(file.getContentType());
        media.setDeleted(false);

        media = mediaRepository.save(media);
        return MediaResponse.builder()
                .name(media.getName())
                .extension(media.getExtension())
                .mimeTypeFile(media.getMimeTypeFile())
                .uri(baseUri + String.format("%s.%s", fileName, extension))
                .size(file.getSize())
                .build();
    }

    // upload many files
    public List<MediaResponse> filesUpload(List<MultipartFile> files) {
        return files.stream()
                .map(this::fileUpload)
                .toList();
    }


    // download file with (name + ext)
    public ResponseEntity<Resource> downloadMedia (String fileName) {
        Path filePath = Paths.get(serverPath).resolve(fileName);

        if (!Files.exists(filePath)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }

        try {
            Resource resource = new UrlResource(filePath.toUri());
            String contentType = Files.probeContentType(filePath);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .header("Content-Type", contentType != null ? contentType : "application/octet-stream")
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid file URL");
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not determine file type");
        }
    }


    // deleted file
    public void deleteMedia(String fileName) {
        Path filePath = Paths.get(serverPath).resolve(fileName);

        // Delete file from filesystem
        try {
            if (!Files.exists(filePath)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
            }
            Files.delete(filePath);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file");
        }

        // Delete record from DB
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0) {
            String name = fileName.substring(0, dotIndex);
            mediaRepository.findMediaByName(name)
                    .ifPresent(mediaRepository::delete);

        }
    }

}
