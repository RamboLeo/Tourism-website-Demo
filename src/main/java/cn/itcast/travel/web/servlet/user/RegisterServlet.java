package cn.itcast.travel.web.servlet.user;

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

@WebServlet("/registerServlet")
public class RegisterServlet extends HttpServlet {
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
			returnResultInfo(false, "checkCode", "验证码错误❌", response);
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
			returnResultInfo(false, "userRegistered", "注册失败，用户已经存在", response);
		} else {
			returnResultInfo(true, null, null, response);
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
		PrintWriter writer = response.getWriter();
		objectMapper.writeValue(writer, resultInfo);
	}
}
