package com.backup.central_data_storage.s3;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/s3")
public class S3Controller {

  @Autowired
  private S3Service s3Service;

  @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public String uploadFile (MultipartFile file) throws IOException {
    s3Service.uploadFile(file.getOriginalFilename(), file);
    return "file uploaded";
  }

  @GetMapping("download/{fileName}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
    return ResponseEntity.ok()
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .body(new InputStreamResource(s3Service.fetchFile(fileName).getObjectContent()));
  }

  @GetMapping("view/{fileName}")
  public ResponseEntity<InputStreamResource> viewFile(@PathVariable String fileName) {
    var s3BucketObject = s3Service.fetchFile(fileName);
    var content = s3BucketObject.getObjectContent();
    return ResponseEntity.ok()
        .contentType(MediaType.IMAGE_PNG)
        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\""+fileName+"\"")
        .body(new InputStreamResource(content));
  }
}
