package com.cluster9.logDispatcherRestService.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class AppRole {
	
	public AppRole() {
		super();
	}
	public AppRole(String roleName) {
		super();
		this.setRoleName(roleName);
	}


	@Id @GeneratedValue
	private Long id;

	@Column(unique = true, nullable = false, length = 100)
	private String roleName;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
