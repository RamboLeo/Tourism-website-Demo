package cn.itcast.travel.service;

public interface FavoriteService {
	boolean isFavorite(int rid, int uid);
	boolean addFavorite(int rid, int uid);
	boolean removeFavorite(int rid, int uid);
}
