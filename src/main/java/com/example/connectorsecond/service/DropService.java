package com.example.connectorsecond.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.users.FullAccount;
import com.example.connectorsecond.exceptions.DropBoxException;
import org.springframework.stereotype.Service;

import java.io.InputStream;


@Service
public class DropService {

    private final DbxClientV2 client;

    public DropService(DbxClientV2 client) {
        this.client = client;
    }


    public String getNameOfAccount() throws DbxException {
//    private String ACCESS_TOKEN = "sl.BGB2y-SUHl7qg8ZeikJ8gY2E84kRLKCLNBRUCFwaAurSbMqeK6nNEKG3P8k7VoWlbzOa1Tw_nJ32CPf9T_7Pa2nKHAgcxud--fQT6ecsg5LzyCL5cbd06-S6zhPLZpShVL_zD0OzRkXM";
//        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/").build();
//
//        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
//        System.out.println(client.account());
        FullAccount account = client.users().getCurrentAccount();
        System.out.println(account.getName().getDisplayName());
//        ListFolderResult result = client.files().listFolder("");
//        while (true) {
//
//            for (Metadata metadata : result.getEntries()) {
//                System.out.println(metadata.getPathLower());
////                System.out.println(metadata.getName());
//            }
//
//            if (!result.getHasMore()) {
//                break;
//            }
//            result = client.files().listFolderContinue(result.getCursor());
//        }
        return "Account : " + account.getName().getDisplayName();
    }


    public CreateFolderResult createFolder(String folderPath){
        return handleDropboxAction(() -> client.files().createFolderV2(folderPath), "Error creating folder");
    }

    public FolderMetadata getFolderDetails(String folderPath){
        return getMetadata(folderPath, FolderMetadata.class, String.format("Error getting folder metadata: ", folderPath));
    }

    public ListFolderResult listFolder(String folderPath, boolean recursiveListing, long limit){
        ListFolderBuilder listFolderBuilder = client.files().listFolderBuilder(folderPath);
        listFolderBuilder.withRecursive(recursiveListing);
        listFolderBuilder.withLimit(limit);
        return handleDropboxAction(listFolderBuilder::start, String.format("Error listening folder", folderPath));
    }



    // NEED TO FIGURE OUT
    private <T> T handleDropboxAction(DropboxActionResolver<T> action, String exceptionMessage) {
        try {
            return action.perform();
        } catch (Exception e) {
            String messageWithCause = String.format("%s with cause: %s", exceptionMessage, e.getMessage());
            throw new DropBoxException(messageWithCause, e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getMetadata(String path, Class<T> type, String message){
        Metadata metadata = handleDropboxAction(() -> client.files().getMetadata(path), String.format("Error accessing details of:"));
        checkIfMetadataIsInstanceOfGivenType(metadata, type, message);
        return (T) metadata;
    }

    private <T> void checkIfMetadataIsInstanceOfGivenType(Metadata metadata, Class<T> validType, String exceptionMessage) {
        boolean isValidType = validType.isInstance(metadata);
        if (!isValidType) {
            throw new DropBoxException(exceptionMessage);
        }
    }


}

