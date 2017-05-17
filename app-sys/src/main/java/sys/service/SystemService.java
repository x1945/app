package sys.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import sys.beans.User;
import sys.spring.CustomUserDetails;

@Service
public class SystemService {

	private static final Logger LOG = LoggerFactory.getLogger(SystemService.class);

	@Autowired(required = false)
	private HttpServletRequest request;

	private Map<String, String> seriesMap = new HashMap<String, String>();

	// @Autowired
	// @Qualifier("sessionRegistry")
	// private SessionRegistry sessionRegistry;

	// @Resource(name = "sessionRegistry")
	// private SessionRegistryImpl sessionRegistry;

	public User getLoginUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// LOG.debug("auth[{}]", auth);
		if (auth != null && auth.getPrincipal() != null) {
			if ("anonymousUser".equals(auth.getPrincipal().toString())) {
				// return null;
				User user = new User();
				user.setUsername("anonymousUser");
				user.setEmail("");
				return user;
				// if (request != null) {
				// user.setBrowser(RequestUtil.getClientBrowser(request));
				// user.setOs(RequestUtil.getClientOperatingSystem(request));
				// user.setIp(RequestUtil.getClientAddr(request));
				// user.setHost(RequestUtil.getClientHost(request));
				// }
				// return user;
			}
			CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
			if (userDetails != null) {
				return userDetails.getUser();
			}
		}

		return null;
	}

	public String getUserId() {
		User user = getLoginUser();
		if (user != null)
			return user.getId();
		return null;
	}

	public String getSeries(String userId) {
		if (userId != null)
			return seriesMap.get(userId);
		return null;
	}

	public void setSeries(User user) {
		if (user != null)
			seriesMap.put(user.getUsername(), user.getSeries());
	}

	public void removeSeries(String userId) {
		if (userId != null)
			seriesMap.remove(userId);
	}
}
