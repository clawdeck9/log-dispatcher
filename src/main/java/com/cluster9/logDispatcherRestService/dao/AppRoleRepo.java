package com.cluster9.logDispatcherRestService.dao;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;

import com.cluster9.logDispatcherRestService.entities.AppRole;

public interface AppRoleRepo extends JpaRepository<AppRole, Long> {
	
	public AppRole findByRoleName(String ident);

}


