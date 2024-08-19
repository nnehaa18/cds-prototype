package com.backup.central_data_storage.backup.service;

import com.amazonaws.util.CollectionUtils;
import com.backup.central_data_storage.backup.bean.BackupDTO;
import com.backup.central_data_storage.backup.bean.BackupInfo;
import com.backup.central_data_storage.backup.bean.BackupPayload;
import com.backup.central_data_storage.backup.bean.BackupType;
import com.backup.central_data_storage.backup.respository.BackupRepository;
import com.backup.central_data_storage.s3.S3Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Service
@Slf4j
public class BackupService {
  @Autowired
  private BackupRepository backupRepository;

  @Autowired
  private S3Service s3Service;

//  public void saveBackupInfo(BackupPayload backupPayload) {
//    ProcessBuilder processBuilder = new ProcessBuilder("tar --force-local -czvg \"C:/Users/NEHA/Desktop/poc/snapshot-file\" -f \"C:/Users/NEHA/Desktop/poc/backup-1.tar.gz\" \"C:/Users/NEHA/Desktop/poc/back\n"
//        + "up\" \"C:/Users/NEHA/Desktop/file1\"");
//    try {
//      Process process = processBuilder.start();
//    }
//    catch (IOException ex){
//      System.out.println("Exception occurred: " +ex);
//    }
//
//  }


//public void saveBackupInfo() {
//  File file = new File("C:\\Users\\NEHA\\Desktop\\poc\\backup.zip");
//  MultipartFile multipartFile = null;
//  try {
//    multipartFile = new MockMultipartFile("backup.zip", new FileInputStream(new File("C:\\Users\\NEHA\\Desktop\\poc\\backup.zip")));
//    s3Service.uploadFile("backup.zip", multipartFile);
//  } catch (IOException e) {
//    throw new RuntimeException(e);
//  }
//}

  public BackupInfo saveBackupInfo(BackupPayload backupPayload) throws IOException, ParseException {
    List<BackupInfo> backupInfoList =  backupRepository.findByBackupLocation(backupPayload.getBackupLocation());
    BackupInfo backupInfo = new BackupInfo();
    backupInfo.setBackupLocation(backupPayload.getBackupLocation());
    backupInfo.setBackupFileLocation(backupPayload.getBackupFileLocation());
    if(CollectionUtils.isNullOrEmpty(backupInfoList)) {
      backupInfo.setBackupType(BackupType.COMPLETE);
    }
    backupInfo.setClientId(backupPayload.getClientId());
    backupInfo.setBackupStorageLocation(backupInfo.getBackupStorageLocation());

    String script = null;
    if(backupInfo.getBackupType() == BackupType.COMPLETE) {
      script = retrieveFullBackupScript(backupInfo, backupPayload.getBackupFileLocation());
    }
    else {
      script = retrieveIncrementalBackupScript(backupInfo, backupPayload.getBackupFileLocation());
    }
    backupInfo.setBackupCommand(script);
    backupInfo = backupRepository.save(backupInfo);

    return backupInfo;
  }

  public String retrieveFullBackupScript(BackupInfo backupInfo, String backupFileLocation) {
    String osName = System.getProperty("os.name");
    osName = osName.toLowerCase();
    String script = null;

      if(osName.contains("windows")) {
        script = "7z a zipped.7z backup\\* -x!*/";
        script = script.replaceAll("backup", backupInfo.getBackupLocation());
        script = script.replaceAll("zipped", backupFileLocation);
      }
      if(osName.contains("linux") || osName.contains("mac")) {
        script = "tar --force-local -czvg \"backup/snapshot-file\" -f \"zipped/full.tar.gz\" \"backup\" ";
        script = script.replaceAll("backup", backupInfo.getBackupLocation());
        script = script.replaceAll("zipped", backupFileLocation);
      }
    return script;
  }

  public String retrieveIncrementalBackupScript(BackupInfo backupInfo, String backupFileLocation)
      throws ParseException {
    String osName = System.getProperty("os.name");
    osName = osName.toLowerCase();
    String script = null;

      if(osName.contains("windows")) {
        script = "7z u backup.7z -u- -up1q1r3x1y1z0w1!zipped/incremental.7z C:\\Users\\NEHA\\Desktop\\poc\\backup\\* -x!*/";
      }
      if(osName.contains("linux") || osName.contains("mac")) {
        script = "tar --force-local -czvg \"zipped/snapshot-file\" -f \"zipped/incremental.tar.gz\" \"backup\"";

      }
    script = script.replaceAll("backup", backupInfo.getBackupLocation());
    script = script.replaceAll("zipped", backupFileLocation);

    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    Date today = new Date();
    Date todayWithZeroTime = formatter.parse(formatter.format(today));
    script = script.replaceAll("incremental", todayWithZeroTime.toString());
    return script;
  }

  public void restoreBackup () {
    /*
    this method marks the runRestoreCommand field in BackupInfo as true.
    this results in all the restore job to be executed for which the runRestoreCommand field is true
     */
  }

}
