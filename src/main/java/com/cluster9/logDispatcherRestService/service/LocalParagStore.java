package com.cluster9.logDispatcherRestService.service;

import com.cluster9.logDispatcherRestService.dao.TagRepo;
import com.cluster9.logDispatcherRestService.entities.WebLogParagraph;
import com.clustercld.logsmanager.DispatchLogFilesContent;
import com.clustercld.logsmanager.entities.LogParagraph;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.util.ArrayList;

public class LocalParagStore {
    String paragsFolderPath;

    public LocalParagStore(String paragsFolderPath) {
        this.paragsFolderPath = paragsFolderPath;
    }

    public ArrayList<WebLogParagraph> readParags() {

        DispatchLogFilesContent dispatchLogFilesContent = new DispatchLogFilesContent();
        ArrayList<LogParagraph> simpleLogs = null;
        try {
            simpleLogs = (ArrayList<LogParagraph>) dispatchLogFilesContent.getLogParagraphs(paragsFolderPath);
        } catch (Exception e) {
            System.out.println("probliem loading log files in localstore instance");
            throw new RuntimeException(e);
        }
        return convertToWebLogParags(simpleLogs);
    }


    private ArrayList<WebLogParagraph> convertToWebLogParags(ArrayList<LogParagraph> simpleParags) {
        ArrayList<WebLogParagraph> list = new ArrayList<WebLogParagraph>();
        simpleParags.forEach( (parag) -> list.add(new WebLogParagraph(parag)));
        return list;
    }
}
