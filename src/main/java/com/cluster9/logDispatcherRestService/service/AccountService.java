package com.cluster9.logDispatcherRestService.service;

import com.cluster9.logDispatcherRestService.entities.AppRole;
import com.cluster9.logDispatcherRestService.entities.AppUser;

public interface AccountService {
	//TODO: remove the User return type, which must stay in this service
	// these methods are not called by Spring
	public AppUser saveUser(AppUser user);
	public AppRole saveRole(AppRole role);

	// should work like this: get a AppUser from the db; add the role to this user and that's it, the appuser is attached
	// so the role should be added when flushing, which is auto with JPA and cascadeAll
	// https://howtodoinjava.com/hibernate/hibernate-many-to-many-mapping/
	boolean addRoleToUser(String username, String roleName) throws Exception;

	boolean createRole(String roleName);
	boolean createUser(String userName, String passwd);

	// this method is called by Spring security through the userDetailsService obj
	public AppUser findUserByUsername(String username);
}
