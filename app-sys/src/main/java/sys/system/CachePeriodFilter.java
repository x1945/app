package sys.system;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CachePeriodFilter implements Filter {

	private HashMap<String, String> mapping;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		mapping = new HashMap<String, String>();
		Enumeration<?> entry = filterConfig.getInitParameterNames();
		while (entry.hasMoreElements()) {
			String key = String.valueOf(entry.nextElement());
			mapping.put(key, filterConfig.getInitParameter(key));
		}
	}

	@Override
	public void destroy() {
		mapping.clear();
		mapping = null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String uri = req.getRequestURI();
		for (Entry<String, String> entry : mapping.entrySet()) {
			if (uri.startsWith(entry.getKey())) {
				resp.setHeader("Cache-Control", entry.getValue());
				resp.setHeader("Pragma", entry.getValue());

				break;
			}
		}
		chain.doFilter(request, response);
	}

}
