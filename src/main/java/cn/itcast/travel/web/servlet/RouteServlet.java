package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImp;
import cn.itcast.travel.service.impl.RouteServiceImp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
	private final RouteService routeService = new RouteServiceImp();
	private final FavoriteService favoriteService = new FavoriteServiceImp();

	/**
	 * 实现分页查询的功能
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cidStr = request.getParameter("cid");
		int cid = 0;
		if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
			cid = Integer.parseInt(request.getParameter("cid"));
		}
		String rname = request.getParameter("rname");
		rname = URLDecoder.decode(rname);
		if ("null".equals(rname) || rname == null) {
			rname = null;
		} else {
			rname = new String(rname.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
		}
		int currentPage = Integer.parseInt(request.getParameter("currentPage"));

		// 默认pageSize = 10;
		int pageSize = 10;
		PageBean<Route> routePageBean = routeService.pageQuery(cid, rname, currentPage, pageSize);
		// 将routePageBean响应回前端
		returnJson(routePageBean, response);
	}


	/**
	 * 完成根据id查询一个旅游线路的详细信息的功能
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 读取前端传来的rid
		String ridStr = request.getParameter("rid");
		int rid = 0;
		if (ridStr != null) {
			rid = Integer.parseInt(ridStr);
		}
		Route route = routeService.findOne(rid);
		returnJson(route, response);
	}


	/**
	 * 完成判断是否收藏的功能
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取rid
		String ridStr = request.getParameter("rid");
		int rid = 0;
		if (ridStr != null && ridStr.length() > 0) {
			rid = Integer.parseInt(ridStr);
		}
		// 获取uid
		int uid = 0;
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			uid = user.getUid();
		}
		ResultInfo resultInfo = new ResultInfo();
		resultInfo.setFlag(favoriteService.isFavorite(rid, uid));
		// resultInfo序列化后传回前端
		returnJson(resultInfo, response);
	}


	/**
	 * 完成为用户添加收藏的功能
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取uid
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setFlag(false);
			resultInfo.setErrorMsg("您尚未登录!");
			returnJson(resultInfo, response);
			return;
		}
		int uid = user.getUid();
		// 获取rid
		String ridStr = request.getParameter("rid");
		int rid = 0;
		if (ridStr != null && ridStr.length() > 0) {
			rid = Integer.parseInt(ridStr);
		}
		boolean flag = favoriteService.addFavorite(rid, uid);
		ResultInfo resultInfo = new ResultInfo();
		if (flag) {
			resultInfo.setFlag(flag);
		} else {
			resultInfo.setFlag(flag);
			resultInfo.setErrorMsg("收藏失败");
		}
		returnJson(resultInfo, response);
	}

	/**
	 * 完成为用户取消收藏的功能
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void removeFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取uid
		User user = (User) request.getSession().getAttribute("user");
		int uid = user.getUid();
		// 获取rid
		String ridStr = request.getParameter("rid");
		int rid = 0;
		if (ridStr != null && ridStr.length() > 0) {
			rid = Integer.parseInt(ridStr);
		}
		boolean flag = favoriteService.removeFavorite(rid, uid);
		ResultInfo resultInfo = new ResultInfo();
		if (flag) {
			resultInfo.setFlag(flag);
		} else {
			resultInfo.setFlag(flag);
			resultInfo.setErrorMsg("取消失败");
		}
		returnJson(resultInfo, response);
	}
}
