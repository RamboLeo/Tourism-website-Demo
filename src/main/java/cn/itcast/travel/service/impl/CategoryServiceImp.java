package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDAO;
import cn.itcast.travel.dao.impl.CategoryDAOImp;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImp implements CategoryService {
	private final CategoryDAO categoryDAO = new CategoryDAOImp();

	@Override
	public List<Category> findAll() {
		List<Category> categories = null;
		// 1. 先从Redis中获取
		Jedis jedis = JedisUtil.getJedis();
		Set<Tuple> categoriesRedis = jedis.zrangeWithScores("category", 0, -1);
		if (categoriesRedis == null || categoriesRedis.size() == 0) {
			// 获取不到，就去mysql中回去
			categories = categoryDAO.findAll();
			// 再将数据保存到Redis中
			for (Category category : categories) {
				jedis.zadd("category", category.getCid(), category.getCname());
			}
		} else {
			// 将从redis中获得的set转为list
			categories = new ArrayList<>();
			for (Tuple tuple : categoriesRedis) {
				Category category = new Category((int) tuple.getScore(), tuple.getElement());
				categories.add(category);
			}
		}
		return categories;
	}

}
