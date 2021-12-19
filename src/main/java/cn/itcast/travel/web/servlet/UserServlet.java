package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImp;
import cn.itcast.travel.util.UuidUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
	private final UserService userService = new UserServiceImp();

	/**
	 * 完成查询当前登录用户的功能
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		response.setContentType("application/json;charset:utf-8");
		// 将当前登录的用户名写回
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(response.getOutputStream(), user);
//		String s = "";
//		try {
//			s = "{\"user\":\"" + user.getName() + "\"}";
//		} catch (Exception ignored) {
//		}
//		response.setContentType("application/json;charset:utf-8");
//		response.getWriter().write(s);
	}

	/**
	 * 完成登录的功能
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 先验证一下验证码
		String check = request.getParameter("check");
		String checkCode = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
		// 记得要将session中的验证码清空一下
		request.getSession().removeAttribute("CHECKCODE_SERVER");
		// 验证码不通过的情况，返回一下false的resultInfo对象的消息
		if (!checkCode.equalsIgnoreCase(check)) {
			ResultInfo resultInfo = new ResultInfo(false, null, "验证码错误❌");
			returnJson(resultInfo, response);
			return;
		};

		// 2. 将数据封装到User对象中
		// 获取表单数据
		Map<String, String[]> parameterMap = request.getParameterMap();

		User user = new User();
		try {
			BeanUtils.populate(user, parameterMap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		// 3. 调用UserService对象的login
		User u = userService.login(user);

		// 4.根据返回的user对象来返回响应
		if (u == null) {
			ResultInfo resultInfo = new ResultInfo(false, null, "登录失败，用户不存在或密码错误");
			returnJson(resultInfo, response);
		} else {
			if ("N".equalsIgnoreCase(u.getStatus())) {
				ResultInfo resultInfo = new ResultInfo(false, null, "登录失败，用户尚未激活");
				returnJson(resultInfo, response);
			} else if ("Y".equalsIgnoreCase(u.getStatus())) {
				// 将u添加进session
				request.getSession().setAttribute("user", u);
				ResultInfo resultInfo = new ResultInfo(true, null, null);
				returnJson(resultInfo, response);
			}
		}
	}

	/**
	 * 完成退出的功能
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 将session销毁
		request.getSession().invalidate();
		// 重定向至登录页面
		response.sendRedirect(request.getContextPath() + "/login.html");
	}

	/**
	 * 完成注册的功能
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 先验证一下验证码
		String check = request.getParameter("check");
		String checkCode = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
		// 记得要将session中的验证码清空一下
		request.getSession().removeAttribute("CHECKCODE_SERVER");
		// 验证码不通过的情况，返回一下false的resultInfo对象的消息
		if (!checkCode.equalsIgnoreCase(check)) {
			ResultInfo resultInfo = new ResultInfo(false, null, "验证码错误❌");
			returnJson(resultInfo, response);
			return;
		};

		// 2. 将数据封装到User对象中
		// 获取表单数据
		Map<String, String[]> parameterMap = request.getParameterMap();

		User user = new User();
		try {
			BeanUtils.populate(user, parameterMap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		// 补充一下user的激活状态和激活码
		user.setStatus("N");
		user.setCode(UuidUtil.getUuid());
		// 3. 调用UserService对象的register方法
		if (!userService.register(user)) {
			ResultInfo resultInfo = new ResultInfo(false, null, "注册失败，用户已经存在");
			returnJson(resultInfo, response);
		} else {
			ResultInfo resultInfo = new ResultInfo(true, null, null);
			returnJson(resultInfo, response);
		}
	}

	public void activeUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1.获取激活码
		String code = request.getParameter("code");
		// 2.判断激活码是否为null,不为空再激活
		if (code != null) {
			boolean active = userService.active(code);
			// 3.判断标记
			String msg = null;
			if (active) {
				// 激活成功
				msg = "激活成功，请<a href='" + request.getContextPath() +
						"/login.html'>登录</a>";
			} else {
				// 激活失败
				msg = "激活失败，请联系管理员";
			}
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(msg);
		} else {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("注册码为空，注册失败");
		}
	}

}
