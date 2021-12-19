package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.SellerDAO;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class SellerDAOImp implements SellerDAO {
	private final JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
	@Override
	public Seller findBySid(int sid) {
		String sql = "SELECT * FROM tab_seller WHERE sid = ?;";
		Seller seller = null;
		try {
			seller = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Seller>(Seller.class), sid);
		} catch (DataAccessException e) {
//			e.printStackTrace();
		}
		return seller;
	}
}
