package com.cluster9.logDispatcherRestService.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cluster9.logDispatcherRestService.dao.AppUserRepo;
import com.cluster9.logDispatcherRestService.entities.AppRole;
import com.cluster9.logDispatcherRestService.entities.AppUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service // no annots in the interface declaration
public class UserDetailsServiceImpl implements UserDetailsService {
	

	@Autowired
	private AppUserRepo aur;

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);
	
	@Override @Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = aur.findByIdent(username);
//		AppUser appUser = getUser();
		// appUser est null, il n'est pas trouvé dans la base par le service appUserRepo (généré par Spring)
		if (appUser == null) {
			logger.error("Cannot set user authentication ***" + username);
			throw new UsernameNotFoundException("UserDetailsService.loadUserByUsername(): username not found");
		}
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		appUser.getRoles().forEach(u -> authorities.add(new SimpleGrantedAuthority(u.getRoleName())));
		logger.debug("a UserDetails has been created with username: "+username);
		return new User(appUser.getUsername(), appUser.getPassword(), authorities);
	}
	
	// to test with a known user:
	private AppUser getUser() {
		AppUser appUser = new AppUser();
		appUser.setPassword("$2a$10$Bza7isAGxhqH.kIKsi/WeO/rGbKyxcHI02tXrJCgtllvQksnjMjqG");
		appUser.setUsername("user");
		Set<AppRole> roles = new HashSet<>();
		AppRole userRole = new AppRole();
		userRole.setRoleName("USER");
		roles.add(userRole);
		appUser.setRoles(roles);
		return appUser;
	}
}

//from bezKoder:
//		should I use the builder? Are the roles added in the user in the user repo
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//	@Autowired
//	UserRepository userRepository;
//
//	@Override
//	@Transactional
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		User user = userRepository.findByUsername(username)
//				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
//
//		return UserDetailsImpl.build(user);
//	}
//
//}
