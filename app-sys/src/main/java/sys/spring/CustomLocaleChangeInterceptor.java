package sys.spring;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import sys.util.StringUtil;

public class CustomLocaleChangeInterceptor extends LocaleChangeInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(CustomLocaleChangeInterceptor.class);

	@Autowired
	CookieLocaleResolver localeResolver;

	/**
	 * after the handler is executed
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (handler instanceof HandlerMethod) {
			// Method method = ((HandlerMethod) handler).getMethod();
			// LOG.debug(" after the handler is executed ");
			if (modelAndView != null) {
				String name = StringUtil.trim(localeResolver.getCookieName());
				String lang = "zh_TW";
				Locale locale = LocaleContextHolder.getLocale();
				if (locale != null)
					lang = locale.toString();
				// LOG.debug("locale[{}] name[{}] lang[{}]", locale, name,
				// lang);
				modelAndView.addObject(name, lang);
			}
		}
	}
}
