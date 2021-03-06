package org.runcity.secure;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.runcity.db.entity.Volunteer;
import org.runcity.db.entity.enumeration.SecureUserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public class SecureUserDetails implements UserDetails {
	public static final SimpleGrantedAuthority ADMIN_ROLE = new SimpleGrantedAuthority("ROLE_ADMIN");
	public static final SimpleGrantedAuthority VOLUNTEER_ROLE = new SimpleGrantedAuthority("ROLE_VOLUNTEER");
	
	private Long id;
	private boolean active;
	private String username;
	private String password;
	private String credentials;
	private String email;
	private String locale;
	private Set<GrantedAuthority> roles;
	
	private Volunteer current;

	public SecureUserDetails(Long id, String username, boolean active, String password, String credentials,
			String email, String locale, List<SecureUserRole> roles, Volunteer current) {
		update(id, username, active, password, credentials, email, locale, roles);
		this.current = current;
	}

	public static SecureUserDetails getCurrentUser() {
		Authentication a = SecurityContextHolder.getContext().getAuthentication();
		Object o = a == null ? null : a.getPrincipal();
		return o instanceof SecureUserDetails ? (SecureUserDetails) o : null;
	}

	public static String getCurrentUserLocale() {
		SecureUserDetails currentUser = getCurrentUser();
		return currentUser == null ? null : currentUser.getLocale();
	}
	
	public void update(Long id, String username, boolean active, String password, String credentials,
			String email, String locale, List<SecureUserRole> roles) {
		this.id = id;
		this.username = username;
		this.active = active;
		this.password = password;
		this.credentials = credentials;
		this.email = email;
		this.locale = locale;

		if (roles != null) {
			Set<GrantedAuthority> userRoles = new HashSet<GrantedAuthority>();
			
			for (SecureUserRole r : roles) {
				userRoles.add(new SimpleGrantedAuthority("ROLE_" + SecureUserRole.getStoredValue(r)));
			}
			this.roles = userRoles;
		}
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return active;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getId() {
		return id;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocale() {
		return locale;
	}
	
	public void setLocale(String locale) {
		this.locale = locale;
	}

	public void setCurrent(Volunteer current) {
		this.current = current;
	}
	
	public Volunteer getCurrent() {
		return current;
	}
	
	public boolean isAdmin() {
		return roles.contains(ADMIN_ROLE);
	}
	
	public boolean isVolunteer() {
		return roles.contains(VOLUNTEER_ROLE);
	}
}
