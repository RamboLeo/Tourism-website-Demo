package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDAO;
import cn.itcast.travel.dao.RouteDAO;
import cn.itcast.travel.dao.RouteImgDAO;
import cn.itcast.travel.dao.SellerDAO;
import cn.itcast.travel.dao.impl.FavoriteDAOImp;
import cn.itcast.travel.dao.impl.RouteDAOImp;
import cn.itcast.travel.dao.impl.RouteImgDAOImp;
import cn.itcast.travel.dao.impl.SellerDAOImp;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImp implements RouteService {
	private final RouteDAO routeDAO = new RouteDAOImp();
	private final SellerDAO sellerDAO = new SellerDAOImp();
	private final RouteImgDAO routeImgDAO = new RouteImgDAOImp();
	private final FavoriteDAO favoriteDAO = new FavoriteDAOImp();

	@Override
	public PageBean<Route> pageQuery(int cid, String rname, int currentPage, int pageSize) {
		PageBean<Route> routePageBean = new PageBean<>();
		routePageBean.setPageSize(pageSize);
		routePageBean.setCurrentPage(currentPage);
		int totalCount = routeDAO.findTotalCount(cid, rname);
		routePageBean.setTotalCount(totalCount);
		int totalPage = totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1);
		routePageBean.setTotalPage(totalPage);
		int start = (currentPage - 1) * pageSize;
		routePageBean.setList(routeDAO.findByPage(cid, rname, start, pageSize));
		return routePageBean;
	}

	@Override
	public Route findOne(int rid) {
		Route route = routeDAO.findOne(rid);
		// 补全需要的route信息
		int sid = route.getSid();
		Seller seller = sellerDAO.findBySid(sid);
		route.setSeller(seller);
		List<RouteImg> routeImgList = routeImgDAO.findByRid(rid);
		route.setRouteImgList(routeImgList);
		int count = favoriteDAO.findCountByRid(rid);
		route.setCount(count);
		return route;
	}
}
