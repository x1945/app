package sys.system;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import sys.beans.User;
import sys.service.SystemService;
import sys.util.PropUtil;
import sys.util.RequestUtil;
import sys.util.SpringUtil;

/**
 * 錯誤訊息攔截器
 * 
 * @author Fantasy
 * 
 */
public class ExceptionResolver implements HandlerExceptionResolver, Ordered {

	private static final Logger LOG = LoggerFactory.getLogger(ExceptionResolver.class);

	private String encoding = "utf-8";

	private SystemService systemService;

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		if (ex != null) {
			if (systemService == null) {
				systemService = SpringUtil.get(SystemService.class);
			}
			try {
				Throwable cause = ex;
				while (cause.getCause() != null) {
					cause = cause.getCause();
				}

				// StringBuilder sb = new StringBuilder();
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setContentType("text/html; charset=" + encoding);
				PrintWriter out = response.getWriter();

				JSONObject exception = new JSONObject();

				boolean allowOutput = false;
				// WebException or FlowException
				if (cause instanceof WebException) {
					exception.put("type", "web");
					allowOutput = true;
					// } else if (cause instanceof FlowException) {
					// exception.put("type", "flow");
				} else if (cause instanceof ValidException) {
					exception.put("type", "valid");
					allowOutput = true;
				} else {
					LOG.error("cause error:", cause);
					exception.put("type", "other");
					StringWriter str = new StringWriter();
					ex.printStackTrace(new PrintWriter(str));
				}
				String errMsg = cause.getMessage();

				// 判斷執行環境和權限(prop 要 admin權限才可以看錯誤訊息!)
				if (PropUtil.isProd() && !allowOutput) {
					boolean isAdmin = false;
					if (systemService != null) {
						User user = systemService.getLoginUser();
						// if (user != null)
						// isAdmin = user.getIsAdmin();
					}
					if (!isAdmin)
						errMsg = "系統執行有誤，請聯絡系統管理者！";
				}

				exception.put("message", errMsg);
				// sb.append(errMsg);
				// LOG.debug("RequestUtil.isAjax(request): {}",
				// RequestUtil.isAjax(request));
				// 判斷 AJAX REQUEST 且不是 DOWNLOAD SUBMIT add by fantasy 2013/12/18
				// LOG.debug("isAjaxRequest:{}", RequestUtil.isAjax(request));
				if (RequestUtil.isAjax(request)) {
					// 直接輸出錯誤訊息
					// sb.delete(0, sb.length());
					// sb.append(cause.getMessage());
					// LOG.debug("RequestUtil.isAjax(request): {}",
					// exception.toString());
					out.append(exception.toString());
				} else {
					// response.sendRedirect("error/e0000");
					// response.sendRedirect("/error/post");
					// TODO
				}

				// mark by fantasy 2015/09/25
				// out.append(exception.toString());
			} catch (Exception e) {
				LOG.error("resolveException error:", e);
				throw new RuntimeException(e);
			}
		}

		return new ModelAndView();
	}

}
