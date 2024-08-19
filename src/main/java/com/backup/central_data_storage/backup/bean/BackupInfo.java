package com.backup.central_data_storage.backup.bean;

import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class BackupInfo {
  @Id
  private String backupId;
//  to check the company/client
  private String clientId;
  private BackupType backupType = BackupType.INCREMENTAL;
  private String backupName;
  private Date backupDate;
//  url of location selected by user
  private String backupLocation;
  private String backupFileLocation;
  private String backupCommand;
  //  to check if backup script is executed
  private boolean backupCreated = false;
  //  populated when backup script executed
  private boolean runRestoreCommand = false;
  private String restoreCommand;
  private String restoreLocation;
  private Date restorationDate;
  private BackupStorageLocation backupStorageLocation = BackupStorageLocation.LOCAL_STORAGE;
}
