package com.asposetest.sample.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.asposetest.sample.support.SupportUtils;

/**
 * This class is used for customizing every httpServletRequest.
 * 
 * @author HARIHARAN MANI
 * @since 07-01-2022
 */
@Component
public class AsposeTestHandlerInterceptor implements HandlerInterceptor {

	private static final String START_TIME = "h2StartTime";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		request.setAttribute(START_TIME, System.currentTimeMillis());

		String servletURL = request.getRequestURI().substring(request.getContextPath().length());
		String requestIP = request.getRemoteAddr();
		SupportUtils.printSysout(String.format("[%s] - preHandle [%s]", servletURL, requestIP));

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		String servletURL = request.getRequestURI().substring(request.getContextPath().length());
		long startTime = (Long) request.getAttribute(START_TIME);
		long endTime = System.currentTimeMillis();

		String timeTakenMsg = SupportUtils.getTimeDuration(endTime - startTime);

		// TimeDuration Log will be added for all Angular request
		SupportUtils.printSysout(String.format("[%s] - timeTaken = %s", servletURL, timeTakenMsg));
	}
}