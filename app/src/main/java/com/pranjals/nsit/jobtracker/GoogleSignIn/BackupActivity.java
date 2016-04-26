package com.pranjals.nsit.jobtracker.GoogleSignIn;

import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by Pranjal Verma on 4/26/2016.
 */
public class BackupActivity extends DriveBaseActivity {

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        Drive.DriveApi.newDriveContents(getGoogleApiClient())
                .setResultCallback(driveContentsCallback);
    }

    final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback = new
            ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult driveContentsResult) {
                    DriveContents cont = driveContentsResult != null && driveContentsResult.getStatus().isSuccess() ?
                            driveContentsResult.getDriveContents() : null;

                    // write file to content, chunk by chunk
                    if (cont != null) try {

                        OutputStream oos = cont.getOutputStream();
                        if (oos != null) try {
                            String dbPath = "/data/data/" + "com.pranjals.nsit.jobtracker" + "/databases/" + "DB.db";
                            InputStream is = new FileInputStream(new java.io.File(dbPath));

                            byte[] buf = new byte[4096];
                            int c;
                            while ((c = is.read(buf, 0, buf.length)) > 0) {
                                oos.write(buf, 0, c);
                               // Log.e("sdsdsdsds", "ooooooooooooooooooooooooooo");
                                oos.flush();
                            }
                        } finally {
                            oos.close();
                        }
                        String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType("db");
                        MetadataChangeSet meta = new MetadataChangeSet.Builder().setTitle("DB.db").setMimeType(mimeType).build();
                        Drive.DriveApi.getRootFolder(getGoogleApiClient()).createFile(getGoogleApiClient(), meta, cont).setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                            @Override
                            public void onResult(DriveFolder.DriveFileResult driveFileResult) {
                                if (driveFileResult != null && driveFileResult.getStatus().isSuccess()) {
                                    DriveFile dFil = driveFileResult != null && driveFileResult.getStatus().isSuccess() ?
                                            driveFileResult.getDriveFile() : null;
                                    if (dFil != null) {
                                        // BINGO , file uploaded
                                        dFil.getMetadata(getGoogleApiClient()).setResultCallback(new ResultCallback<DriveResource.MetadataResult>() {
                                            @Override
                                            public void onResult(DriveResource.MetadataResult metadataResult) {
                                                if (metadataResult != null && metadataResult.getStatus().isSuccess()) {
                                                    DriveId mDriveId = metadataResult.getMetadata().getDriveId();

                                                }
                                            }
                                        });
                                    }
                                } else { /* report error */ }
                            }
                        });
                    }
                    catch (Exception e) { e.printStackTrace(); }

                }
            };






}
