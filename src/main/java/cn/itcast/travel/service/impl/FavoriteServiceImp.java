package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDAO;
import cn.itcast.travel.dao.impl.FavoriteDAOImp;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.FavoriteService;

public class FavoriteServiceImp implements FavoriteService {
	private final FavoriteDAO favoriteDAO = new FavoriteDAOImp();
	@Override
	public boolean isFavorite(int rid, int uid) {
		Favorite favorite = favoriteDAO.findByRidAndUid(rid, uid);
		return favorite != null;
	}

	@Override
	public boolean addFavorite(int rid, int uid) {
		return favoriteDAO.add(rid, uid);
	}

	@Override
	public boolean removeFavorite(int rid, int uid) {
		return favoriteDAO.remove(rid, uid);
	}
}
