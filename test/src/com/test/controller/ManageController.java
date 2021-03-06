package com.test.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.miger.commons.dto.Page;
import com.miger.commons.utils.Md5Util;
import com.miger.commons.utils.StringUtil;
import com.test.model.NovelChapter;
import com.test.model.NovelChapterSave;
import com.test.model.NovelList;
import com.test.model.NovelListSave;
import com.test.model.manager.Limit;
import com.test.model.manager.Menu;
import com.test.model.manager.User;
import com.test.service.LimitService;
import com.test.service.LogService;
import com.test.service.MenuService;
import com.test.service.NovelChapterSaveService;
import com.test.service.NovelChapterService;
import com.test.service.NovelContentSaveService;
import com.test.service.NovelContentService;
import com.test.service.NovelListSaveService;
import com.test.service.NovelListService;
import com.test.service.UserService;
import com.test.utils.RequestUtil;

/**
 * 
 * @author zhanghainan
 * 
 *  
 */

@Controller
@RequestMapping(value = "/manage")
public class ManageController {
	
	private static final Logger log = Logger.getLogger(ManageController.class);

	public static final String SESSION_ID = "novel.test.0814152001";// SESSID
	
	
	@Autowired
	private UserService userService;
	@Autowired
	private LimitService limitsService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private LogService logService;
	@Autowired
	private NovelListService novelListService;
	@Autowired
	private NovelListSaveService novelListSaveService;
	@Autowired
	private NovelChapterService novelChapterService;
	@Autowired
	private NovelChapterSaveService novelChapterSaveService;
	@Autowired
	private NovelContentService novelContentService;
	@Autowired
	private NovelContentSaveService novelContentSaveService;
	
	/**
	 * 后台首页
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/index.html")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView();
		String userid = StringUtil.getSessionId(request, SESSION_ID);
		if (!StringUtil.isNull(userid)) {
			User user = userService.load(userid);
			List<Menu> menuList = menuService.getLimitsByUserid(userid);
			if (user.getLimitsId() != null) {
				Limit limit = limitsService.loadLimits(user.getLimitsId());
				if (limit != null) {
					view.addObject("menuList", menuList);
					view.addObject("username", user.getName());
					view.setViewName("manage/index");
				}
			}
		}
		return view;
	}

	/**
	 * 后台欢迎
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/welcome")
	public ModelAndView welcome(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("manage/welcome");
		return view;
	}

	/**
	 * 后台登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login.html")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("manage/login");
		return view;
	}

	/**
	 * 登录验证
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/loginValid")
	public void loginValid(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String gotopage = request.getParameter("goto_page");
		User info = null;
		if (!StringUtil.isNull(username) && !StringUtil.isNull(password)) {
			// 用户名、密码验证
			info = userService.valid(username, password);
			if (info != null) {
				// SESSION设置
				HttpSession session = request.getSession();
				session.setAttribute(SESSION_ID, info.getId());
				session.setMaxInactiveInterval(12 * 3600);// 12小时过期
				// 增加登录日志
				logService.insert(1, info.getId(), "用户登录：" + info.getName() + ",IP:" + RequestUtil.getIpAddr(request));
			}
		}
		// 定义JSON map 
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("success", info != null ? true : false);
		if (!StringUtil.isNull(gotopage)) {
			json.put("goto_page", gotopage);
		}
		response.getWriter().print(JSON.toJSONString(json));
	}

	/**
	 * 登录状态
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/loginStatus")
	public void loginStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userid = StringUtil.getSessionId(request, SESSION_ID);
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("success", !StringUtil.isNull(userid) ? true : false);
		response.getWriter().print(JSON.toJSONString(json));
	}
	
	/**
	 * 网站注销
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws IOException
	 */
	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		if (session != null && session.getAttribute(SESSION_ID) != null) {
			// 增加注销日志
			String userid = StringUtil.getSessionId(request, SESSION_ID);
			User user = userService.load(userid);
			logService.insert(1, userid, "用户注销：" + (user != null ? user.getName() : "未知") + ",IP:" + RequestUtil.getIpAddr(request));
			// 清除SESSION
			session.removeAttribute(SESSION_ID);
		}
		ModelAndView view = new ModelAndView();
		view.setViewName("redirect:/manage/login.html");
		return view;
	}
	
	/** ***********************************系统管理**************************************** */
	/** ***********************************系统日志**************************************** */

	/**
	 * 日志显示
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/log")
	public ModelAndView log(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("manage/record");
		return view;
	}

	/**
	 * 日志列表
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/loglist")
	public void loglist(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rows = request.getParameter("rows"); // 总页数
		String current = request.getParameter("page"); // 当前页
		String username = request.getParameter("username");
		String content = request.getParameter("content");

		Map<String, Object> jsonMap = logService.page(rows, current, username, content);
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}
	
	/** ***********************************菜单**************************************** */

	/**
	 * 菜单显示
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/menu")
	public ModelAndView menu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("manage/menu");
		return view;
	}

	/**
	 * 菜单列表
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/menuList")
	public void menuList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rows = request.getParameter("rows"); // 总页数
		String curr = request.getParameter("page"); // 当前页
		String pid = request.getParameter("pid");

		Map<String, Object> jsonMap = menuService.page(rows, curr, pid);
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}

	/**
	 * 菜单保存
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/menuSave")
	public void menuSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		String pid = request.getParameter("pid");
		String name = request.getParameter("name");
		String url = request.getParameter("url");
		String iconCls = request.getParameter("iconCls");
		String sequence = request.getParameter("sequence");

		Menu info = menuService.load(id);
		if (info == null) {
			info = new Menu();
		}
		info.setPid(pid);
		info.setName(name);
		info.setUrl(url);
		info.setIconCls(iconCls);
		info.setSequence(sequence != null ? Integer.valueOf(sequence) : 0);
		info.setStatus(1);
		menuService.save(info);

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", info != null ? true : false);
		response.getWriter().print(JSON.toJSONString(jsonMap));

	}

	/**
	 * 菜单加载
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/menuLoad")
	public void menuLoad(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		if (!StringUtil.isNull(id)) {
			Menu info = menuService.load(id);
			response.getWriter().print(JSON.toJSONString(info));
		}
	}

	/**
	 * 菜单删除
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/menuDel")
	public void menuDel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		if (!StringUtil.isNull(id)) {
			menuService.delete(id);
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", !StringUtil.isNull(id));
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}
	
	/** ***********************************后台账号管理**************************************** */
	/** ***********************************用户管理**************************************** */

	/**
	 * 用户显示
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/viewUser")
	public ModelAndView viewUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("账号编辑");
		ModelAndView view = new ModelAndView();
		// 权限列表
		List<Limit> limits = limitsService.getAllList();
		view.addObject("limits", limits);
		view.setViewName("manage/user");
		return view;
	}

	/**
	 * 分组设置
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/viewGroup")
	public ModelAndView viewGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("分组设置");
		ModelAndView view = new ModelAndView();
		view.setViewName("manage/grouplist");
		return view;
	}

	/**
	 * 用户列表
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/userlist")
	public void userlist(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("用户列表");
		// 1.参数
		String rows = request.getParameter("rows"); // 总页数
		String current = request.getParameter("page"); // 当前页
		String name = request.getParameter("name");

		// 2.列表
		Map<String, Object> jsonMap = userService.page(rows, current, name);
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}

	/**
	 * 用户加载
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/userLoad")
	public void userLoad(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("用户加载");
		// 1.参数
		String id = request.getParameter("id");
		if (!StringUtil.isNull(id)) {
			User user = userService.load(id);
			response.getWriter().print(JSON.toJSONString(user));
		}
	}

	/**
	 * 用户删除
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/userDel")
	public void userDel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("用户删除");
		// 1.参数
		String id = request.getParameter("id");
		boolean success = false;
		if (!StringUtil.isNull(id)) {
			userService.delete(id);
			success = true;
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", success);
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}

	/**
	 * 用户注册
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/register")
	public void register(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("用户注册");
		// 1.参数
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String limitsId = request.getParameter("limitsId");
		// 2.保存用户
		User info = new User();
		if (!StringUtil.isNull(id)) {
			info = userService.load(id);
			if (!Md5Util.checkpassword(password, info.getPassword())) {
				info.setPassword(Md5Util.EncoderByMd5(password));
			}
		} else {
			info.setFrequency(0);
			info.setPassword(Md5Util.EncoderByMd5(password));
		}
		info.setName(name);
		if (!StringUtil.isNull(email))
			info.setEmail(email);
		info.setLimitsId(limitsId);
		info.setStatus(1);
		userService.save(info);

		// 3.定义map 
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", true);
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}
	
	/**
	 * 编辑用户
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/update")
	public void update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("编辑用户");
		// 1.参数
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String limitsId = request.getParameter("limitsId");
		// 2.保存用户
		User info = new User();
		if (!StringUtil.isNull(id)) {
			info = userService.load(id);			
		} else {
			info.setFrequency(0);
		}
		info.setName(name);
		if (!StringUtil.isNull(email))
			info.setEmail(email);
		info.setLimitsId(limitsId);
		info.setStatus(1);
		userService.save(info);

		// 3.定义map 
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", true);
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}
	
	/**
	 * 编辑密码
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/editPwd")
	public void editPwd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("编辑密码");
		// 1.参数
		String id = request.getParameter("id");
		System.out.println("---------");
		System.out.println(id);
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		System.out.println(password);
		System.out.println(email);

		System.out.println("---------");

		// 2.保存用户
		if(!StringUtil.isNull(id)){
			User info = userService.load(id);
			if(info!=null){
				info.setPassword(Md5Util.EncoderByMd5(password));
			}
			userService.save(info);
		}		

		// 3.定义map 
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", true);
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}

	
	/** ***********************************权限管理**************************************** */

	/**
	 * 权限显示
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/viewLimit")
	public ModelAndView viewLimits(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("权限设置");
		ModelAndView view = new ModelAndView();
		// 菜单列表
		List<Menu> menus = menuService.pageList(null, null);
		view.addObject("menus", menus);
		view.setViewName("manage/limit");
		return view;
	}

	/**
	 * 权限列表
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/limitlist")
	public void limitlist(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("权限列表");
		// 1.参数
		String rows = request.getParameter("rows"); // 总页数
		String current = request.getParameter("page"); // 当前页
		String name = request.getParameter("name");

		// 2.总数total
		int total = limitsService.pageCount(name);

		// 3.分页page
		Page<Limit> page = new Page<Limit>(Integer.valueOf(current), total, Integer.valueOf(rows));

		// 4.查询list
		List<Limit> list = limitsService.pageList(name, page);

		// 5.定义map 
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("total", total);
		jsonMap.put("rows", list);
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}

	/**
	 * 保存权限
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/limitSave")
	public void limitSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("保存权限");
		// 1.参数
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String limitIds = request.getParameter("limitIds");
		String limitNames = request.getParameter("limitNames");
		// 2.保存
		Limit info = new Limit();
		if (!StringUtil.isNull(id)) {
			info = limitsService.load(id);
		}
		info.setName(name);
		info.setLimitIds(limitIds);
		info.setLimitNames(limitNames);
		info.setStatus(1);
		limitsService.save(info);

		// 3.定义map 
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", true);
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}

	/**
	 * 权限加载
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/limitsLoad")
	public void limitsLoad(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("权限加载");
		String id = request.getParameter("id");
		if (!StringUtil.isNull(id)) {
			Limit info = limitsService.load(id);
			response.getWriter().print(JSON.toJSONString(info));
		}
	}

	/**
	 * 权限删除
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/limitsDel")
	public void limitsDel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("权限删除");
		// 1.参数
		String id = request.getParameter("id");
		boolean success = false;
		if (!StringUtil.isNull(id)) {
			limitsService.delete(id);
			success = true;
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", success);
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}
	
	
	/** ***********************************小说列表管理**************************************** */
	/** *********************************** 小说列表**************************************** */

	
	/**
	 * 小说页面
	 */
	@RequestMapping(value = "/magazine")
	public ModelAndView magazine(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("manage/magazine");
		return view;
	}
	/**
	 * 小说列表
	 */
	@RequestMapping(value = "/magazinelist")
	public void magazinelist(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rows = request.getParameter("rows"); // 总页数
		String curr = request.getParameter("page"); // 当前页
		Map<String, Object> jsonMap = novelListService.page(rows, curr);
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}
	/**
	 * 小说删除
	 */
	@RequestMapping(value = "/magazineDel")
	public void magazineDel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("article_id");
		if (!StringUtil.isNull(id)) {
			novelListSaveService.delete(id);
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", !StringUtil.isNull(id));
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}
	/**
	 * 根据小说id 获取 小说信息
	 */
	@RequestMapping(value = "/magazineLoad")
	public void magazineLoad(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("article_id");
		if (!StringUtil.isNull(id)) {
			NovelList info = novelListService.load(id);
			System.out.println(JSON.toJSONString(info));
			response.getWriter().print(JSON.toJSONString(info));
		}
	}
	
	@RequestMapping(value = "/magazineSave")
	public void magazineSave(@RequestParam(value = "covers", required = false) MultipartFile mediacovers,@RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String article_id = request.getParameter("article_id");
		String id = request.getParameter("id");
		String link = request.getParameter("link");
		String title = request.getParameter("title");
		String author = request.getParameter("author");
		String article_abstract = request.getParameter("article_abstract");
		String image_link = request.getParameter("image_link");
		
		String article_type = request.getParameter("article_type");
		String status = request.getParameter("status");
		
		NovelListSave info = novelListSaveService.findUnique(article_id);
		if (info == null) {
			info = new NovelListSave();
			info.setId(id);
			//info.setIsDel(0);
			//info.setCreateTime(new Date().getTime());
		}
		info.setId(id);
		info.setArticle_id(article_id);
		info.setTitle(title);
		info.setAuthor(author);
		info.setArticle_abstract(article_abstract);
		info.setLink(link);
		info.setImage_link(image_link);
		info.setArticle_type(article_type);
		info.setStatus(status);
		
		novelListSaveService.save(info);

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", info != null ? true : false);
		response.getWriter().print(JSON.toJSONString(jsonMap));

	}
	
	/** ***********************************小说章节列表**************************************** */

	/**
	 * 小说章节页面
	 */
	
	@RequestMapping(value = "/article")
	public ModelAndView article(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("manage/article");
		return view;
	}
	/**
	 * 小说章节列表
	 */
	@RequestMapping(value = "/articlelist")
	public void articlelist(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rows = request.getParameter("rows"); // 总页数
		String curr = request.getParameter("page"); // 当前页
		String title = request.getParameter("title");
        String article_directory = request.getParameter("article_directory");
		Map<String, Object> jsonMap = novelChapterService.page(rows, curr, title, article_directory);
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}
	
	@RequestMapping(value = "/articleSave")
	public void articleSave(@RequestParam(value = "mediacoverLink", required = false) MultipartFile mediacoverLink,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		String article_id = request.getParameter("article_id");
		String title = request.getParameter("title");
		String update_status = request.getParameter("update_status");
		String last_update_date = request.getParameter("last_update_date");
		String last_update_directory = request.getParameter("last_update_directory");
		String article_directory = request.getParameter("article_directory");
		String article_directory_link = request.getParameter("article_directory_link");

		NovelChapterSave info = novelChapterSaveService.findUnique(id);
		if(info == null){
			info = new NovelChapterSave();
		}
		info.setId(id);
		info.setArticle_id(article_id);
		info.setTitle(title);
		info.setUpdate_status(update_status);
		info.setLast_update_date(last_update_date);
		info.setLast_update_directory(last_update_directory);
		info.setArticle_directory(article_directory);
		info.setArticle_directory_link(article_directory_link);
		
		novelChapterSaveService.save(info);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", info != null ? true : false);
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}
	
	/**
	 * 根据杂志id 获取 小说信息
	 */
	@RequestMapping(value = "/articleLoad")
	public void articleLoad(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
	
		if (!StringUtil.isNull(id)) {
			Integer chapterId = new Integer(id);
			NovelChapter info = novelChapterService.load(chapterId);
			System.out.println(JSON.toJSONString(info));
			response.getWriter().print(JSON.toJSONString(info));
		}
	}
	/**
	 * 小说删除
	 */
	@RequestMapping(value = "/articleDel")
	public void articleDel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		if (!StringUtil.isNull(id)) {
			novelChapterSaveService.delete(id);
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", !StringUtil.isNull(id));
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}
	
	
}