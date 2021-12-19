package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDAO;
import cn.itcast.travel.dao.impl.UserDAOImp;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;

public class UserServiceImp implements UserService {
	private final UserDAO userDAO = new UserDAOImp();
	@Override
	public boolean register(User user) {
		// 1. 查询一下username是否已经注册
		String username = user.getUsername();
		if (userDAO.findByUsername(username) != null) {
			return false;
		}
		// 2.将user保存到mysql数据库中
		userDAO.save(user);

		// 3.发送激活邮件
		String content = "<a href='http://localhost/travel/user/activeUser?code=" + user.getCode()
				+ "'>点击链接激活账户</a>";
		MailUtils.sendMail(user.getEmail(), content, "激活邮件");
		return true;
	}

	@Override
	public boolean active(String code) {
		// 1. 通过code查找user
		User user = userDAO.findByCode(code);
		// 如果code查不到就返回false
		if (user == null) {
			return false;
		}
		// 2. 更新状态
		try {
			userDAO.updateStatus(user);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public User login(User user) {
		return userDAO.findByUsernameAndPassword(user.getUsername(), user.getPassword());
	}
}
