package cn.itcast.travel.web.servlet.user;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
	private final UserService userService = new UserServiceImp();
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 先验证一下验证码
		String check = request.getParameter("check");
		String checkCode = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
		// 记得要将session中的验证码清空一下
		request.getSession().removeAttribute("CHECKCODE_SERVER");
		// 验证码不通过的情况，返回一下false的resultInfo对象的消息
		if (!checkCode.equalsIgnoreCase(check)) {
			returnResultInfo(false, null, "验证码错误❌", response);
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
			returnResultInfo(false, null, "登录失败，用户不存在或密码错误", response);
		} else {
			if ("N".equalsIgnoreCase(u.getStatus())) {
				returnResultInfo(false, null, "登录失败，用户尚未激活", response);
			} else if ("Y".equalsIgnoreCase(u.getStatus())) {
				// 将u添加进session
				request.getSession().setAttribute("user", u);
				returnResultInfo(true, null, null, response);
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

	/**
	 * 用一个方法来简化返回信息给注册页面的操作
	 * @param flag
	 * @param data
	 * @param errorMsg
	 * @param response
	 * @throws IOException
	 */
	private void returnResultInfo(boolean flag, Object data, String errorMsg, HttpServletResponse response) throws IOException {
		ResultInfo resultInfo = new ResultInfo(flag, data, errorMsg);
		// 把resultInfo转换为JSON
		ObjectMapper objectMapper = new ObjectMapper();
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = response.getWriter();
		objectMapper.writeValue(writer, resultInfo);
	}
}
