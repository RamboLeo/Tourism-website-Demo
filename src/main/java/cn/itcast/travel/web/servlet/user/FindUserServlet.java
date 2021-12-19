package cn.itcast.travel.web.servlet.user;

import cn.itcast.travel.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/findUserServlet")
public class FindUserServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
//		response.setContentType("application/json;charset:utf-8");
//		// 将当前登录的用户名写回
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.writeValue(response.getOutputStream(), user);
		String s = "{\"user\":\"" + user.getName() + "\"}";
		response.setContentType("application/json;charset:utf-8");
		response.getWriter().write(s);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
