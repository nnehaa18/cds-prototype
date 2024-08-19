package com.backup.central_data_storage.backup.respository;

import com.backup.central_data_storage.backup.bean.BackupInfo;
import com.backup.central_data_storage.backup.bean.BackupType;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackupRepository extends MongoRepository<BackupInfo, String> {
  BackupInfo findByBackupNameAndClientId(String backupName, String clientId);
  List<BackupInfo> findByBackupLocation(String backupLocation);
  List<BackupInfo> findByBackupCreated(boolean backupCreated);
}
