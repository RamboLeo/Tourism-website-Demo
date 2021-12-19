package cn.itcast.travel.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 这个Filter用来统一处理编码的问题
 */
@WebFilter("/*")
public class CheckCharacterFilter implements Filter {
	public void init(FilterConfig config) throws ServletException {
	}

	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
		// 父接口转为子接口
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		// 获取请求方法
		String method = httpServletRequest.getMethod();
		// 解决post请求中文数据乱码的问题
		if (method.equalsIgnoreCase("post")) {
			httpServletRequest.setCharacterEncoding("UTF-8");
		}
		// 处理响应乱码
		httpServletResponse.setContentType("text/html;charset=utf-8");
		// 将处理过的request和response放行
		chain.doFilter(httpServletRequest, httpServletResponse);
	}
}
