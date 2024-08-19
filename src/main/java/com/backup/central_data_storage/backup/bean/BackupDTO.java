package com.backup.central_data_storage.backup.bean;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class BackupDTO {
  private String backupId;
  private BackupType backupType = BackupType.INCREMENTAL;
  private String backupName;
  private Date backupDate;
  private List<String> backupLocations;
  private String backupCommand;
  //  to check if backup script is executed
  private boolean isBackupCreated = false;
  //  populated when backup script executed
  private String restoreCommand;
  private String restoreLocation;
  private Date restorationDate;
  private BackupStorageLocation backupStorageLocation;
}
