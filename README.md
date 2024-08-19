# cds-prototype
This is a backend project. The FE 

The system is designed to encompass the following functionalities: 
1. it backs up files from user-specified locations, with the stored backups being either in LOCAL or CLOUD storage according to the user's choice.
2. Additionally, the recovery software allows users to download the compressed backup from either local or cloud storage.

The backup and restore software utilizes incremental backups, providing an efficient method for addressing the use case. This approach allows for quick backups, enabling users to restore data completely or to a specified date as desired.
The implementation of functionality relies on commands that are specific to the operating system in use. To optimize space, backups are compressed prior to being stored on either local or cloud storage.

# Technologies used: Java 11, Spring boot, mongodb, amazon S3
Note: This project is focused on the backend, as the frontend could not be developed within the available timeframe. Nevertheless, frontend development can be scheduled for a future release.

# Flow of program: 

1. Each client holds a unique id(clientId), to differentiate from the rest, if multi-tenant architecture implemented.
2. The user selects a location, which is supposed to be backed up.
3. The API is hit with the location to be backed up
     API: POST: api/v1/backup
     RequestBody: {
                   backupLocation: location to be backed up
                   backupFileLocation: location where backup folder is to be stored
                   backupStorageLocation: CLOUD or LOCAL
                   clientId: company id
                   }
     This API logic prepares the backup script with user selected values
4. Similarly, the restore API marks the flag true in POJO, which results in the restore scheduler to execute restoration scripts
5. the schedulers are created to process and execute scripts, returning the backup compressed folders, which are then stored on to the local or cloud storage as per user's selection.



