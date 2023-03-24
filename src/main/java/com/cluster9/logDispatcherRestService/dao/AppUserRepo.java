package com.cluster9.logDispatcherRestService.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cluster9.logDispatcherRestService.entities.AppUser;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {
	
	public AppUser findByIdent(String ident);
    public void delete(AppUser appUser);
    public  boolean existsByIdent(String ident);
}
