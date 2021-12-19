package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteImgDAO;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class RouteImgDAOImp implements RouteImgDAO {
	private final JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
	@Override
	public List<RouteImg> findByRid(int rid) {
		String sql = "SELECT * FROM tab_route_img WHERE rid = ?;";
		List<RouteImg> routeImgList = null;
		try {
			routeImgList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<RouteImg>(RouteImg.class), rid);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return routeImgList;
	}
}
