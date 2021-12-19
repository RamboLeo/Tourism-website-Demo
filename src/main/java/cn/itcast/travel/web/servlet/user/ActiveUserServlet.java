package cn.itcast.travel.web.servlet.user;

import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/activeUserServlet")
public class ActiveUserServlet extends HttpServlet {
	private final UserService userService = new UserServiceImp();
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1.获取激活码
		String code = request.getParameter("code");
		// 2.判断激活码是否为null,不为空再激活
		if (code != null) {
			boolean active = userService.active(code);
			// 3.判断标记
			String msg = null;
			if (active) {
				// 激活成功
				msg = "激活成功，请<a href='login.html'>登录</a>";
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

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
