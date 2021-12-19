package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDAO;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDAOImp implements FavoriteDAO {
	private final JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

	@Override
	public Favorite findByRidAndUid(int rid, int uid) {
		String sql = "SELECT * FROM tab_favorite WHERE rid = ? AND uid = ?;";
		Favorite favorite = null;
		try {
			favorite = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
		} catch (DataAccessException ignored) {
		}
		return favorite;
	}

	@Override
	public int findCountByRid(int rid) {
		String sql = "SELECT COUNT(*) FROM tab_favorite WHERE rid = ?;";
		int count = 0;
		try {
			count = jdbcTemplate.queryForObject(sql, Integer.class, rid);
		} catch (DataAccessException ignored) {
		}
		return count;
	}

	@Override
	public boolean add(int rid, int uid) {
		String sql = "INSERT INTO tab_favorite VALUES(?, ?, ?);";
		int update = 0;
		try {
			update = jdbcTemplate.update(sql, rid, new Date(), uid);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return update > 0;
	}

	@Override
	public boolean remove(int rid, int uid) {
		String sql = "DELETE FROM tab_favorite WHERE rid = ? and uid = ?;";
		int update = 0;
		try {
			update = jdbcTemplate.update(sql, rid, uid);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return update > 0;
	}

}
