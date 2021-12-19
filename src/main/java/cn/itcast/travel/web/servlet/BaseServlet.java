package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**ds
 * 利用反射机制实现对应Servlet的调用
 */
public class BaseServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 完成方法的分发
		// 1. 获取请求路径
		String uri = req.getRequestURI();
		// 2. 获取方法名称，请求路径的最后一个部分就是方法
		String methodName = uri.substring(uri.lastIndexOf('/') + 1);
		try {
			// 3. 获取方法对象Method
			Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			// 4. 执行方法
			method.invoke(this, req, resp);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用一个方法简化返回Json数据的流程
	 *
	 * @param obj
	 * @param response
	 */
	public void returnJson(Object obj, HttpServletResponse response) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		response.setContentType("application/json;charset=utf-8");
		ServletOutputStream outputStream = response.getOutputStream();
		objectMapper.writeValue(outputStream, obj);
	}
}
