package sys.system;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//import app.annotation.PageSet;
//import app.service.PageService;
import sys.service.SystemService;
import sys.util.RequestUtil;

/**
 * Controller Interceptor
 * 
 * @author Fantasy
 * 
 */
public class ControllerInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOG = LoggerFactory.getLogger(ControllerInterceptor.class);

	@Autowired
	SystemService sys;

//	@Autowired
//	PageService pageService;

	/**
	 * before the handler is executed
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (handler instanceof HandlerMethod) {
			ControllerContext.init();

			// User user = systemService.getLoginUser();
			// // logback setting user
			// if (user != null)
			// MDC.put("userId", user.getId());

			Method method = ((HandlerMethod) handler).getMethod();
			Map<String, String> params = RequestUtil.transformParams(request.getParameterMap());
			LOG.info("method[{}] type[{}] params:{}", method.getName(), request.getMethod(), params);

			// if (Util.equals(method.getName(), "index") &&
			// RequestUtil.isAjax()) {
			// // throw new WebException("session已失效，請重新登入！");
			// throw new WebException("ajax-link-index");
			// }

			/**
			 * 分頁設定
			 */
			// if (method.getReturnType() == Page.class) {
			// PaginationContext.setPagination(true);
			// PaginationContext.setNumber(params.get("page.page"));
			// PaginationContext.setSize(params.get("page.size"));
			// }
		}

		return true;
	}

	/**
	 * after the handler is executed
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (handler instanceof HandlerMethod) {

			Method method = ((HandlerMethod) handler).getMethod();
			// 沒有@ResponseBody
			if (!method.isAnnotationPresent(ResponseBody.class)) {
				LOG.info("postHandle 使用者 : {}", sys.getLoginUser());
				// 將user info輸出至畫面
				modelAndView.addObject("user", sys.getLoginUser());

				// pageSet
//				if (method.isAnnotationPresent(PageSet.class)) {
//					Map<String, Object> pageInfo = pageService.getInfo();
//					modelAndView.addObject("Org_website_data", pageInfo.get("Org_website_data"));

//					Map<String, List<Map<String, Object>>> items = pageService.findItems();
//					modelAndView.addObject("items", items);

					// LOG.debug("items:{}", items);

//				}
			}
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (handler instanceof HandlerMethod) {
		}
	}
}
