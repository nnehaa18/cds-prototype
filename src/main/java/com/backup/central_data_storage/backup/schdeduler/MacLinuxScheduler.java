package com.backup.central_data_storage.backup.schdeduler;

public class MacLinuxScheduler {
  /*
  the logic and implementation is same as Windows.
  The backup job will run everyday at 12AM. IT will create backup compressed folders and save it in S3 if the user
  has opted for CLOUD storage. Else it will store the same in local storage

  the restoration will run everyday at 12PM and will execute the restoration scripts when the user has opted for restoration -
  this is determined by the runRestoreCommand field in BackupInfo.

  the restoration script is similar to backup script
    incremental backups have been implemented in the system for faster backup use case

   Order of restoration: A full backup is restored first and then the decremental backups one by one in order of last to first
   In order to retrieve backup till a certain date, full backup to be restored first and then decremental backups till the specified date

    In order to restore in windows script to be run: 7z x -y C:\Users\NEHA\Desktop\poc\incremental-7.7z -oC:\Users\NEHA\Desktop\poc\restore
    Linux and Mac script: tar --force-local -xvf "C:/Users/NEHA/Desktop/poc/backup.tar.gz" -C "C:/Users/NEHA/Desktop/poc/restore"
   */
}
