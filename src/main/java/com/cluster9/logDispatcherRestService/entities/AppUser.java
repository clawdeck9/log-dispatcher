package com.cluster9.logDispatcherRestService.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
public class AppUser {

	public AppUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AppUser(String name, String password) {
		super();
		this.setUsername(name);
		this.setPassword(password);
	}

	@Id @GeneratedValue
	private Long id;
	
	private String ident;

	// @JsonIgnore you should forbid the password to be send	
	private String password;

	// try with a Set instead of a Collection (doubles pb)
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER)
	@JoinTable(
			name = "app_user_roles",
			joinColumns = @JoinColumn(name = "app_user"),
			inverseJoinColumns = @JoinColumn(name = "app_role"))
	private Set<AppRole> roles;

	//getters and setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return ident;
	}

	public void setUsername(String ident) {
		this.ident = ident;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<AppRole> getRoles() {
		if (roles == null)
			return new HashSet<>();
		else
			return roles;
	}

	public void setRoles(Set<AppRole> roles) {
		this.roles = roles;
	}
	public boolean addRole(AppRole role) {
		for (AppRole r: this.getRoles()) {
			if (r.getRoleName().equals(role.getRoleName()))
				return true;
		}
		return this.getRoles().add(role);
	}
/*	public boolean addRole(AppRole role) {
		if (roles == null)
			roles = new HashSet<AppRole>();
		for (AppRole r: roles) {
			if (r.getRoleName().equals(role.getRoleName()))
				return true;
		}
		return roles.add(role);
	}*/
}
