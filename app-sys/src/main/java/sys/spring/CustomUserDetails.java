package sys.spring;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;

import sys.beans.User;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;

	public User getUser() {
		return user;
	}

	public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	@SuppressWarnings("unchecked")
	public CustomUserDetails(User user, String password) {
		super(user.getEmail(), password, Collections.EMPTY_LIST);
		this.user = user;
	}
}
