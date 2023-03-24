package com.cluster9.logDispatcherRestService.service;

import com.cluster9.logDispatcherRestService.dao.AppRoleRepo;
import com.cluster9.logDispatcherRestService.dao.AppUserRepo;
import com.cluster9.logDispatcherRestService.entities.AppRole;
import com.cluster9.logDispatcherRestService.entities.AppUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	@Autowired
	private BCryptPasswordEncoder bcEncoder;
	@Autowired
	private AppUserRepo userRepo;
	@Autowired 
	private AppRoleRepo roleRepo;

	@Override
	public AppUser saveUser(AppUser user) {
		String hashPw = bcEncoder.encode(user.getPassword());
		user.setPassword(hashPw);
		return userRepo.save(user);
	}

	public boolean exists(AppUser user) {
		return userRepo.existsByIdent(user.getUsername());
	}

	public boolean exists(String username) {
		return userRepo.existsByIdent(username);
	}

	@Override
	public AppRole saveRole(AppRole role) {
		return roleRepo.save(role);
	}

	@Override
	public boolean createUser(String userName, String passwd) {
		if (userRepo.existsByIdent(userName)){
			return false;
		} else {
			saveUser(new AppUser(userName, passwd));
			return true;
		}
	}

	@Override
	public boolean createRole(String roleName) {
		AppRole role=roleRepo.findByRoleName(roleName);
		if (role == null) {
			role = new AppRole();
			role.setRoleName(roleName);
			roleRepo.save(role);
		} else {
			return false;
		}
		return  true;
	}

	// should work like this: get a AppUser from the db; add the role to this user and that's it, the appuser is attached
	// so the role should be added when flushing, which is auto with JPA and cascadeAll
	// https://howtodoinjava.com/hibernate/hibernate-many-to-many-mapping/
	// but the role must exists beforehand and be retrieved as an JPA attached instance.
	@Override
	public boolean addRoleToUser(String username, String roleName) throws Exception {
		AppUser user = null;
		AppRole role=roleRepo.findByRoleName(roleName);
		if (role == null) {
			throw new RuntimeException("add role to user failed: already exists.");
		}
		if (userRepo.existsByIdent(username)) {
			user=userRepo.findByIdent(username);
			user.getRoles().add(role);
			return true;
		} else {
			throw new RuntimeException("add role to user; no user with this username");
		}
		//return false;
	}

	// used by security module
	@Override
	public AppUser findUserByUsername(String username) {
		return userRepo.findByIdent(username);
	}

	public void deleteUserByUsername(String username) {
		userRepo.delete(userRepo.findByIdent(username));
	}
}
