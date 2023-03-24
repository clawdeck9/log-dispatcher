package com.cluster9.logDispatcherRestService.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cluster9.logDispatcherRestService.dao.TagRepo;
import com.cluster9.logDispatcherRestService.dao.WebLogParagraphRepo;
import com.cluster9.logDispatcherRestService.entities.Tag;
import com.cluster9.logDispatcherRestService.entities.WebLogParagraph;
import com.cluster9.logDispatcherRestService.service.ErrorBindingService;
import com.cluster9.logDispatcherRestService.service.LogsService;



// @CrossOrigin(origins = "http://10.0.2.2:8080", maxAge = 36000) // this is done in the security configuration
@RestController
@RequestMapping("/")
public class LogController {

    Logger logger = LoggerFactory.getLogger(LoggingController.class);
	@Autowired
	private WebLogParagraphRepo logsRepo;
	@Autowired
	private LogsService logsService;
	@Autowired
	private TagRepo tagRepo;
	@Autowired
	private ErrorBindingService errorService;
	
	int number = (int) (Math.random()*100);

	@GetMapping("/logs")
	public Iterable<WebLogParagraph>  logs(){
		return logsRepo.findAll();
	}

	@GetMapping(value="/logs", params= {"tag"})
	public ResponseEntity<?>  logsByTag(@RequestParam String tag){
		List<WebLogParagraph> foundLogs = logsService.findByTag(tag);
		return new ResponseEntity<List<WebLogParagraph>>(foundLogs, HttpStatus.OK);
	}

//	@GetMapping(value = "/logs-paged", params = { "tag"})
//	public ResponseEntity<?>  logByIdPaged(@RequestParam("tag") String tag, @NotNull final Pageable pageable){
//		return logsRepo.findByTag(tag, pageable );
//	}
	
	@GetMapping(value="/log", params= {"id"})
	public ResponseEntity<?>  logById(@RequestParam Long id){

		Optional<WebLogParagraph> foundLog = logsRepo.findById(id);
		if(foundLog.isPresent()) {
			System.out.println("LogController::foundLog:"+foundLog.get().getTag());
			return new ResponseEntity<WebLogParagraph>(foundLog.get(), HttpStatus.OK);
		} else {
			logger.debug("Optional<Log> {} was not found for the id {}", foundLog, id);
			return new ResponseEntity<WebLogParagraph>(new WebLogParagraph (0, "noLog", new Tag("noTag"), "empty"), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value="/logs/titles", params = {"tag"})
	public ResponseEntity<?> findTitlesByTag(@RequestParam String tag){
		return new ResponseEntity<List<String>>(logsService.findTitlesByTag(tag), HttpStatus.OK);
	}

//	TODO:
//	these methods must check the roles: @Secured({ "ROLE_VIEWER", "ROLE_EDITOR" })
//	https://www.baeldung.com/spring-security-method-security
	@PostMapping("/logs")
    public ResponseEntity<?> createLog( @Valid @RequestBody WebLogParagraph log, BindingResult result){
    	System.out.println("restcontroller::createLog method - restcontroller number= " + number + "log tag: "+log.getTag());
    	
    	ResponseEntity<?> errorMap = errorService.mapErrors(result);
        
        if(errorMap!=null) 
        	return errorMap;

    	return new ResponseEntity<WebLogParagraph>(logsRepo.save(findOrCreateTag(log)), HttpStatus.CREATED);
    }
    
    // to update a log, an id must be sent as a param in the URL to delete the old log version and replace it
    @PostMapping(value="/logmod", params= {"id"})
    public ResponseEntity<?> updateLog(@Valid @RequestBody WebLogParagraph log, @RequestParam Long id, BindingResult result){
    	System.out.println("restcontroller::updateLog method - restcontroller number= " + number + "log Title: "+log.getTitle());
    	System.out.println("id param value in log controller: " + id);
    	ResponseEntity<?> errorMap = errorService.mapErrors(result);
        if(errorMap!=null) 
        	return errorMap;

        if(id !=null) {        	
        	System.out.println("restcontroller::updateLog method; deleting log by id: " + id);
        	if(logsRepo.existsById(id)){
        		logsRepo.deleteById(id);
            }
        }
        
        log = this.findOrCreateTag(log);
        
    	WebLogParagraph createdLog = logsRepo.save(log);
    	return new ResponseEntity<WebLogParagraph>(createdLog, HttpStatus.CREATED);
    }
    
    private WebLogParagraph findOrCreateTag(WebLogParagraph log) {
        // create a tag if tag doesn't exist
        if(log.getTag()  != null) {
        	Tag tag = null;
        	tag = tagRepo.findByName(log.getTagName());
        	if( tag == null ) {
        		// create a new tag
        		tag = new Tag(log.getTagName());
        		tag.setComment("auto created at log creation");
        	}
        	log.setTag(tag);
        } else { 
        	throw new RuntimeException("tag name was not found in new log");
        }
        return log;
    }
    
// 	a PUT exmaple from: https://www.baeldung.com/http-put-patch-difference-spring
//	@PutMapping("/heavyresource/{id}")
//	public ResponseEntity<?> saveResource(@RequestBody HeavyResource heavyResource,
//	  @PathVariable("id") String id) {
//	    heavyResourceRepository.save(heavyResource, id);
//	    return ResponseEntity.ok("resource saved");
//	}
    
    @DeleteMapping
    public ResponseEntity<?> deleteLog(@PathVariable Long id, @NotNull final Pageable pageable){
    	logsRepo.deleteById(id);
    	return new ResponseEntity<String>("deleted", HttpStatus.ACCEPTED);
    }
    

}






