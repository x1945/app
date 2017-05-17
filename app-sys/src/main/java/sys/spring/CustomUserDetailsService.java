package sys.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import sys.beans.User;
import sys.service.UserService;
import sys.system.ControllerContext;
import sys.util.Util;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger LOG = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {

//		Org_member_data data = userService.findByEmail(arg0);
		
//		LOG.debug("login data: {}", data);

//		if (data != null) {
//			User user = new User();
//			user.setId(data.getMember_uuid());
//			user.setUsername(data.getFirstname() + " " + data.getLastname());
//			user.setEmail(data.getEmail());
//
//			// set Series
//			String series = ControllerContext.getSeries();
//			LOG.debug("ControllerContext.getSeries[{}]", series);
//			if (Util.isNotEmpty(series)) {
//				user.setSeries(series);
//			}
//			UserDetails ud = new CustomUserDetails(user, data.getPassword());
//			return ud;
//		}
		return null;
	}
}
