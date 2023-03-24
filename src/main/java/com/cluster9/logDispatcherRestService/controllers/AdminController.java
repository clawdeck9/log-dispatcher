package com.cluster9.logDispatcherRestService.controllers;


import com.cluster9.logDispatcherRestService.entities.AppUser;
import com.cluster9.logDispatcherRestService.service.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RefreshScope
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/admin")
public class AdminController {

    Logger logger = LoggerFactory.getLogger(LoggingController.class);
    @Autowired
    AccountServiceImpl accountService;

    @GetMapping(value="/post", params= {"name"})
    public ResponseEntity<Boolean> createUser(@RequestParam String name) {
        return new ResponseEntity<Boolean>((Boolean) accountService.createUser(name, "passwd"), HttpStatus.CREATED);
    }

    @GetMapping(value="/delete", params= {"name"})
    public ResponseEntity<Boolean> deleteUser(String name) {
        if (accountService.exists(name)) {
            return new ResponseEntity<Boolean>(HttpStatus.OK);
        }
        logger.debug("Delete user: user not found");
        return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.NOT_FOUND);
    }
}
