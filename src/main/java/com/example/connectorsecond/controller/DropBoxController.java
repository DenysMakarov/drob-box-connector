package com.example.connectorsecond.controller;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.CreateFolderResult;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.example.connectorsecond.service.DropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DropBoxController {

    DropService dropService;

    @Autowired
    public DropBoxController(DropService dropService) {
        this.dropService = dropService;
    }

    @GetMapping("/account")
    public String getNameOfAccount() throws DbxException {
        return dropService.getNameOfAccount();
    }

    @PostMapping("/account/folder/{title}")
    public CreateFolderResult createFolder(@PathVariable String title) throws DbxException {
        return dropService.createFolder("/" + title);
    }

    @GetMapping("/account/folder/{title}")
    public FolderMetadata getFolderDetails(@PathVariable String title) throws DbxException {
        return dropService.getFolderDetails("/newFolder/" + title);
    }

    @GetMapping("/account/folders/{start}")
    public ListFolderResult listFolder(@PathVariable String start) throws DbxException {
        return dropService.listFolder("/" + start, true, 10);
    }
}
