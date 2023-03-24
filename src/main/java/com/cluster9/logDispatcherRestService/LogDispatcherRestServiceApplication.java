package com.cluster9.logDispatcherRestService;

import com.cluster9.logDispatcherRestService.entities.WebLogParagraph;
import com.cluster9.logDispatcherRestService.service.LocalParagStore;
import com.cluster9.logDispatcherRestService.service.LogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cluster9.logDispatcherRestService.dao.WebLogParagraphRepo;
import com.cluster9.logDispatcherRestService.entities.AppRole;
import com.cluster9.logDispatcherRestService.entities.AppUser;
import com.cluster9.logDispatcherRestService.service.AccountServiceImpl;

import java.util.ArrayList;

@SpringBootApplication
public class LogDispatcherRestServiceApplication implements CommandLineRunner {
	@Value("${cluster9.log-directory.input}")
	private String paragsFolder;
	@Autowired
	private LogsService logsService;
	@Autowired
	private AccountServiceImpl accountService;
	
	public static void main(String[] args) {
		ConfigurableApplicationContext app = SpringApplication.run(LogDispatcherRestServiceApplication.class, args);
		// System.out.println("bean definitions : " + Arrays.toString( app.getBeanDefinitionNames()));
		
	}
	
	@Bean
	BCryptPasswordEncoder getBCEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void run(String... arg0) throws Exception {

		//testLoadParagsFromLocalStore(paragsFolder);
		createUsers();
		//loadParagsFromLocalStore(paragsFolder);
	}

	void testLoadParagsFromLocalStore(String pathToFolder) {
		LocalParagStore store = new LocalParagStore(pathToFolder);
		ArrayList<WebLogParagraph> list = store.readParags();
		list.forEach(parag -> System.out.println(parag.getTitle()));
	}

	// TODO: exceptions for: double secondary key (title + creation date + tag); must test if exists before saving?
	void loadParagsFromLocalStore(String pathToFolder){
		LocalParagStore store = new LocalParagStore(pathToFolder);
		store.readParags().forEach( parag -> logsService.saveParagAndCheckUniqueTag(parag) );
	}

	private void createUsers() throws Exception {
		// creates some user with their roles
		//roles before, a new instance can't be added if already exists
		accountService.createRole("ADMIN");
		accountService.createRole("USER");

		String username = "testuser";
		String password = "testpwd";

		// 		{"username":"testuser", "passwd": "testpwd"}

		if ( !accountService.exists(username)) {
			accountService.createUser(username, password);
		}
		// add ADMIN to user:
		accountService.addRoleToUser(username, "USER");
		accountService.addRoleToUser(username, "ADMIN");
	}


}
