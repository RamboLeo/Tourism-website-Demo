package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDAO;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDAOImp implements RouteDAO {
	private final JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

	@Override
	public int findTotalCount(int cid, String rname) {
		String sql = "SELECT count(*) FROM tab_route where 1 = 1 ";
		List<Object> paras = new ArrayList<>();
		if (cid != 0) {
			sql += "and cid = ? ";
			paras.add(cid);
		}
		if (rname != null) {
			sql += "and rname like ? ";
			paras.add("%" + rname + "%");
		}
		int count = jdbcTemplate.queryForObject(sql, Integer.class, paras.toArray());
		return count;
	}

	@Override
	public List<Route> findByPage(int cid, String rname, int start, int pageSize) {
		String sql = "SELECT * FROM tab_route WHERE 1 = 1 ";
		List<Object> paras = new ArrayList<>();
		if (cid != 0) {
			sql += "and cid = ? ";
			paras.add(cid);
		}
		if (rname != null) {
			sql += "and rname like ? ";
			paras.add("%" + rname + "%");
		}
		sql += "LIMIT ?, ?;";
		paras.add(start);
		paras.add(pageSize);
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Route>(Route.class), paras.toArray());
	}

	@Override
	public Route findOne(int rid) {
		String sql = "SELECT * FROM tab_route WHERE rid = ?;";
		Route route = null;
		try {
			route = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return route;
	}
}
