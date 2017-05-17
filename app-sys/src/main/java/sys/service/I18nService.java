package sys.service;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;

@Service
public class I18nService {

	private static final Logger LOG = LoggerFactory.getLogger(I18nService.class);

	@Autowired
	ReloadableResourceBundleMessageSource rrbms;

	/**
	 * 取得訊息
	 * 
	 * @param params
	 */
	public String getMessage(String code) {
		Locale locale = LocaleContextHolder.getLocale();

		try {
			return rrbms.getMessage(code, null, locale != null ? locale : Locale.TRADITIONAL_CHINESE);
		} catch (Exception e) {
			LOG.warn("locale[{}] code[{}] no message!", locale, code);
		}
		return code;
	}
}
