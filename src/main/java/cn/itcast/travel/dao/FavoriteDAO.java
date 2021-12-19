package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDAO {
	Favorite findByRidAndUid(int rid, int uid);
	int findCountByRid(int rid);
	boolean add(int rid, int uid);
	boolean remove(int rid, int uid);
}
