package com.backup.central_data_storage.backup.bean;

import java.util.List;
import lombok.Data;

@Data
public class BackupPayload {
  private String backupLocation;
  private String backupFileLocation;
  private BackupStorageLocation backupStorageLocation;
  private String clientId;
}
