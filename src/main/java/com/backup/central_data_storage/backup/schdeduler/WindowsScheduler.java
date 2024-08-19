package com.backup.central_data_storage.backup.schdeduler;

import com.amazonaws.util.CollectionUtils;
import com.backup.central_data_storage.backup.bean.BackupInfo;
import com.backup.central_data_storage.backup.bean.BackupStorageLocation;
import com.backup.central_data_storage.backup.respository.BackupRepository;
import com.backup.central_data_storage.s3.S3Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class WindowsScheduler {

  @Autowired
  private BackupRepository backupRepository;

  @Autowired
  private S3Service s3Service;

  @Async("asyncExecutor")
  @Scheduled(cron = "0 0 0 * * ?")
  public void scheduleBackup() throws IOException {
    List<BackupInfo> backupInfoList = backupRepository.findByBackupCreated(true);
    if(!CollectionUtils.isNullOrEmpty(backupInfoList)) {
      for(BackupInfo backupInfo : backupInfoList) {
        ProcessBuilder processBuilder = new ProcessBuilder(backupInfo.getBackupCommand());
        Process process = processBuilder.start();

      /*
        after script exeution, the zip folder should be stored in cloud storage, if user has opted CLOUD storage
       */
        if(backupInfo.getBackupStorageLocation() == BackupStorageLocation.CLOUD) {
          File file = new File(backupInfo.getBackupFileLocation()+".zip");
          MultipartFile multipartFile = null;
          try {
            multipartFile = new MockMultipartFile(backupInfo.getBackupFileLocation()+".zip", new FileInputStream(new File("C:\\Users\\NEHA\\Desktop\\poc\\backup.zip")));
            s3Service.uploadFile("backupName", multipartFile);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
  }

  /**
   * the job runs 12pm daily and script processing happens only if user has opted for restoration
   */
  @Async("asyncExecutor")
  @Scheduled(cron = "0 12 ? * * *")
  public void recoveryJob() {
    /*
    the restoration script is similar to backup script
    incremental backups have been implemented in the system for faster backup use case

   Order of restoration: A full backup is restored first and then the decremental backups one by one in order of last to first
   In order to retrieve backup till a certain date, full backup to be restored first and then decremental backups till the specified date

    In order to restore in windows script to be run: 7z x -y C:\Users\NEHA\Desktop\poc\incremental-7.7z -oC:\Users\NEHA\Desktop\poc\restore
    Linux and Mac script: tar --force-local -xvf "C:/Users/NEHA/Desktop/poc/backup.tar.gz" -C "C:/Users/NEHA/Desktop/poc/restore"
     */
  }

}
