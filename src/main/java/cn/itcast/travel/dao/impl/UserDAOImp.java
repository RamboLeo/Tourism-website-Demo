package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDAO;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import cn.itcast.travel.util.Md5Util;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDAOImp implements UserDAO {
	private final JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

	@Override
	public User findByUsername(String username) {
		User user = null;
		// 1.定义sql
		String sql = "select * from tab_user where username = ?;";

		// 2.执行sql
		try {
			user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
		} catch (DataAccessException ignored) {

		}
		return user;

	}

	@Override
	public void save(User user) {
		// 1. 定义sql
		String sql = "insert into tab_user(username, password, name, birthday, sex, telephone, email, status, code) values(" +
				"?, ?, ?, ?, ?, ?, ?, ?, ?);";
		// 2.执行sql语句
		jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getName(), user.getBirthday(), user.getSex(),
				user.getTelephone(), user.getEmail(), user.getStatus(), user.getCode());
	}

	@Override
	public User findByCode(String code) {
		User user = null;
		// 1.定义sql
		String sql = "select * from tab_user where code = ?;";

		// 2.执行sql
		try {
			user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
		} catch (DataAccessException ignored) {
		}
		return user;
	}

	@Override
	public void updateStatus(User user) {
		// 1. 定义sql
		String sql = "UPDATE tab_user SET `status`='Y' where username=?;";
		// 2.执行sql语句

		jdbcTemplate.update(sql, user.getUsername());
	}

	@Override
	public User findByUsernameAndPassword(String username, String password) {
		User user = null;
		// 1.定义sql
		String sql = "select * from tab_user where username = ? and password = ?;";

		// 2.执行sql
		try {
			user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
		} catch (DataAccessException ignored) {

		}
		return user;
	}
}
