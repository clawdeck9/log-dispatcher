package com.cluster9.logDispatcherRestService.service;

import java.util.List;
import java.util.stream.Collectors;

import com.cluster9.logDispatcherRestService.controllers.LoggingController;
import com.cluster9.logDispatcherRestService.dao.TagRepo;
import com.cluster9.logDispatcherRestService.entities.Tag;
import com.cluster9.logDispatcherRestService.entities.WebLogParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cluster9.logDispatcherRestService.dao.WebLogParagraphRepo;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogsService {

	Logger logger = LoggerFactory.getLogger(LoggingController.class);
	@Autowired
	WebLogParagraphRepo logRepo;
	@Autowired
	TagRepo tagRepo;
	// Title list by tag
	public List<String> findTitlesByTag(String tagName){
		try {
			return logRepo.findByTag(tagRepo.findByName(tagName)).stream().map( p -> p.getTitle()).collect(Collectors.toList());
		} catch (Exception e) {
			logger.debug("TagEntity not found in TagRepo", logRepo);
			throw new RuntimeException(e);
		}
	}

	public  List<WebLogParagraph> findLogsByTitle(String title) {
		return logRepo.findByTitle(title).stream().collect(Collectors.toList());
	}

	public List<WebLogParagraph> findByTag(String tagName) {
		try {
			return logRepo.findByTag(tagRepo.findByName(tagName));
		} catch (Exception e) {
			logger.debug("TagEntity not found in TagRepo", logRepo);
			throw new RuntimeException(e);
		}
	}

	// saves a webParag if it contains a temp Tag entity by checking the unique Tag in TagRepo
	// fails if no Tag found in DB
	@Transactional
	public WebLogParagraph saveWithExistingTag(WebLogParagraph parag) {
		if (tagRepo.existsByName(parag.getTagName())) {
			parag.setTag(tagRepo.findByName(parag.getTagName()));
			return parag;
		} else {
			throw new RuntimeException("LogService tries to save a webparag without Tag");
		}
	}

	// saves a webParag
	@Transactional
	public WebLogParagraph saveParagAndTagIfNone(int index, String fileName, String tagName, String title) {
		Tag tag = null;
		tag = tagRepo.findByName(tagName);
		if (tag != null) {
			return new WebLogParagraph(index,fileName, tag, title);
		} else {
			return new WebLogParagraph(index, fileName, new Tag(tagName), title);
		}
	}
	// saves a webParag
	@Transactional
	public WebLogParagraph saveParagAndCheckUniqueTag(WebLogParagraph parag) {
		if (tagRepo.existsByName(parag.getTagName())) {
			parag.setTag(tagRepo.findByName(parag.getTagName()));
			logRepo.save(parag);
		} else {
			parag.setTag(tagRepo.save(new Tag(parag.getTagName())));
		}
		return logRepo.save(parag);
	}

	public boolean existsByTitle(String title) {
		return logRepo.existsByTitle(title);
	}

}
