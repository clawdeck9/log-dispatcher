package com.cluster9.logDispatcherRestService.service;

import com.cluster9.logDispatcherRestService.entities.WebLogParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.text.SimpleDateFormat;



@Service
public class ToFolderService {

    Logger logger = LoggerFactory.getLogger("com.cluster9.logDispatcherRestService.service");
    String folderPathStr;

    SimpleDateFormat nameFormatter = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat contentFormatter = new SimpleDateFormat("dd/MM/yyyy");
    {
        String localFolderName = "/generated-logs/";

        folderPathStr = (Paths.get("").toAbsolutePath().toString()) + localFolderName;
        if (!Files.exists(Paths.get(folderPathStr))) {
            try {
                Files.createDirectory(Paths.get(folderPathStr));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



    public void saveParag(WebLogParagraph paragraph) {

        logger.debug("ToFolderService.saveParag(logPar) called");

        String filePath = folderPathStr.concat("claw-"
                + nameFormatter.format(paragraph.getCreatedDate())
                + "-"
                + paragraph.getTitle().trim().toLowerCase());

        try { // todo: throw an error when the title is too long
            Files.write(Paths.get(filePath),
                    (contentFormatter.format(paragraph.getCreatedDate())
                            .concat("\n")
                            .concat(paragraph.getTitle())
                            .concat("\n")
                            .concat(paragraph.getLines()).getBytes(StandardCharsets.UTF_8)),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("error when writing a file to the log folder");
            e.printStackTrace();

        }
    }
}
