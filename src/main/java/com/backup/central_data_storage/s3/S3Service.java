package com.backup.central_data_storage.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class S3Service  {

  @Autowired
  private AmazonS3 amazonS3;

  @Value("${aws.s3.bucket}")
  private String s3BucketName;

  public void uploadFile(String keyName, MultipartFile file) throws IOException {
    var uploadResult = amazonS3.putObject(s3BucketName, keyName, file.getInputStream(), null);
    log.info("file upload result: ", uploadResult.getMetadata());
  }

  public S3Object fetchFile(String keyName) {
    return amazonS3.getObject(s3BucketName, keyName);
  }
}
