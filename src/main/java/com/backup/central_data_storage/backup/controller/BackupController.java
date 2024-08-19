package com.backup.central_data_storage.backup.controller;

import com.backup.central_data_storage.backup.bean.BackupDTO;
import com.backup.central_data_storage.backup.bean.BackupInfo;
import com.backup.central_data_storage.backup.bean.BackupPayload;
import com.backup.central_data_storage.backup.service.BackupService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
public class BackupController {

  @Autowired
  private BackupService backupService;

  @PostMapping("backup")
  public ResponseEntity<BackupInfo> createBackup (@RequestBody BackupPayload backupPayload)
      throws IOException, ParseException {
      BackupInfo backupDTO = new BackupInfo();
      ResponseEntity<BackupInfo> response = ResponseEntity.ok().body(backupDTO);
      backupService.saveBackupInfo(backupPayload);
    return response;
  }

/*
  dummy api for restoration plain logic
  this marks the field true for restoring backup in system from
 */
  @GetMapping("restore")
  public void restoreBackup () {
    backupService.restoreBackup();
  }

}
