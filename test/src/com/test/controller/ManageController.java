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
import com.test.model.NovelList;
import com.test.model.NovelSave;
import com.test.model.manager.Limit;
import com.test.model.manager.Menu;
import com.test.model.manager.User;
import com.test.service.NovelListService;
import com.test.service.LimitService;
import com.test.service.LogService;
import com.test.service.MenuService;
import com.test.service.NovelSaveService;
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

	public static final String SESSION_ID = "hytc.bss.0814152001";// SESSID
	
	
	@Autowired
	private UserService userService;
	@Autowired
	private LimitService limitsService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private LogService logService;
	@Autowired
	private NovelListService articleHotService;
	@Autowired
	private NovelSaveService novelSaveService;

	
	
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
	
	/** ***********************************日志**************************************** */

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
	
	/** ***********************************用户**************************************** */

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
		String password = request.getParameter("password");
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

	
	/** ***********************************权限**************************************** */

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
		//String pid = request.getParameter("pid");

		Map<String, Object> jsonMap = articleHotService.page(rows, curr);
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}
	/**
	 * 小说删除
	 */
	@RequestMapping(value = "/magazineDel")
	public void magazineDel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("article_id");
		if (!StringUtil.isNull(id)) {
			novelSaveService.delete(id);
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
			NovelList info = articleHotService.load(id);
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

//		String templangCode = "";
//		if("1".equals(langCode)){templangCode = "FR";}
//		else if( "2".equals(langCode)){templangCode = "AR";}
//		else if( "3".equals(langCode)){templangCode = "TH";}
//		else if( "4".equals(langCode)){templangCode = "RU";}
//		else if( "5".equals(langCode)){templangCode = "EN";}
//		else if( "6".equals(langCode)){templangCode = "SP";}
//		else if( "7".equals(langCode)){templangCode = "JA";}
//		else if( "8".equals(langCode)){templangCode = "LT";}
//		else if( "9".equals(langCode)){templangCode = "GE";}
//		else if( "10".equals(langCode)){templangCode = "PO";}
//		else if( "11".equals(langCode)){templangCode = "KO";}
//		
//		String tempimagePath= "";
//		String imagepath = request.getSession().getServletContext().getRealPath(File.separator) + File.separator + "images" + File.separator+"journal_cover"+File.separator+year+File.separator;// 文件存放路径
//		if(mediacovers != null && ! mediacovers.isEmpty()){
//			String fileType = "." + mediacovers.getOriginalFilename().substring(mediacovers.getOriginalFilename().lastIndexOf(".") + 1);// 文件后缀
//			String fileName = templangCode +year+liveIssue+ fileType;// 文件名称
//			// 1.保存文件
//	        File targetFile = new File(imagepath, fileName);
//	        if (!targetFile.exists()) {
//	            targetFile.mkdirs();
//	        }
//	        try {
//	        	mediacovers.transferTo(targetFile);
//	        	tempimagePath = "images/journal_cover/"+year+"/"+fileName;
//	        } catch (Exception e) {
//	            log.error("图片上传异常", e);
//	        }
//		}
		
		NovelSave info = novelSaveService.findUnique(article_id);
		if (info == null) {
			info = new NovelSave();
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
		
		novelSaveService.save(info);

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", info != null ? true : false);
		response.getWriter().print(JSON.toJSONString(jsonMap));

	}
	
//	/**
//	 * 文章页面
//	 */
//	@Autowired
//	private ArticleMenuService articleMenuService;
//	@Autowired
//	private LanguageService LanguageService;
//	@RequestMapping(value = "/article")
//	public ModelAndView article(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		ModelAndView view = new ModelAndView();
//		//获取栏目列表
//		List<ArticleMenu> articlemenuList = articleMenuService.getAll();
//		//获取所有杂志
//		List<Journal> jorunalList= journalService.getAll();
//		//获取所有语种列表
//		List<Language> languageList=LanguageService.getAll();
//		request.setAttribute("menulist", articlemenuList);
//		request.setAttribute("jorunalList", jorunalList);
//		request.setAttribute("languageList", languageList);
//		view.setViewName("manage/article");
//		return view;
//	}
//	/**
//	 * 杂志列表
//	 */
//	@RequestMapping(value = "/articlelist")
//	public void articlelist(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String rows = request.getParameter("rows"); // 总页数
//		String curr = request.getParameter("page"); // 当前页
//		String pid = request.getParameter("pid");
//		String programa = request.getParameter("programa");
//		String journal_Name = request.getParameter("journal_Name");
//        String articleTitle = request.getParameter("articleTitle");
//		//Map<String, Object> jsonMap = journalService.page(rows, curr);
//		Map<String, Object> articJsonMap = articleService.page(rows,curr,pid,programa,journal_Name,articleTitle);
//		response.getWriter().print(JSON.toJSONString(articJsonMap));
//	}
//	
//	@RequestMapping(value = "/articleSave")
//	public void articleSave(@RequestParam(value = "mediacoverLink", required = false) MultipartFile mediacoverLink,HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String id = request.getParameter("id");
//		String title = request.getParameter("title");
//		String subTitle = request.getParameter("subTitle");
//		String pid = request.getParameter("pid");
//		String menuId = request.getParameter("menuId");
//		String digest = request.getParameter("digest");
//		String link = request.getParameter("link");
//		String coverLink = request.getParameter("coverLink");
//		String langCode = request.getParameter("langCode");
//		String journalName = request.getParameter("journalName");
//		String journallink = request.getParameter("journallink");
//		String source = request.getParameter("source");
//		String hsk = request.getParameter("hsk");
//		String content_editor = request.getParameter("content_editor");
//		//先处理上传的文件
//		String tempimagePath= "";
//		String imagepath = request.getSession().getServletContext().getRealPath(File.separator) + File.separator + "hbcms"+ File.separator + "userfiles" + File.separator+"images"+File.separator+"cms"+File.separator+"article"+File.separator;// 文件存放路径
//		if(mediacoverLink != null && ! mediacoverLink.isEmpty()){
//			String fileType = "." + mediacoverLink.getOriginalFilename().substring(mediacoverLink.getOriginalFilename().lastIndexOf(".") + 1);// 文件后缀
//			String fileName = UUID.randomUUID().toString().replace("-", "")+ fileType;// 文件名称
//			// 1.保存文件
//	        File targetFile = new File(imagepath, fileName);
//	        if (!targetFile.exists()) {
//	            targetFile.mkdirs();
//	        }
//	        try {
//	        	mediacoverLink.transferTo(targetFile);
//	        	tempimagePath = File.separator + "hbcms"+ File.separator + "userfiles" + File.separator+"images"+File.separator+"cms"+File.separator+"article"+File.separator+fileName;
//	        } catch (Exception e) {
//	            log.error("图片上传异常", e);
//	        }
//		}
//		
//		ArticleSave article = articleSaveService.load(id);
//		if(article == null){
//			article = new ArticleSave();
//			article.setIsDel(0);
//			article.setPublishDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//			article.setCreatedTime(new Date());
//			article.setCreateTime(new Date().getTime()/1000);
//			article.setLastUpdateTime(new Date().getTime()/1000);
//		}
//		article.setTitle(title);
//		article.setSubTitle(subTitle);
//		article.setParentId(pid);
//		if(StringUtils.isNotBlank(menuId)){
//			article.setMenuId(menuId.split("_")[0]);
//			article.setTopic(menuId.split("_")[1]);
//		}
//		article.setDigest(digest);
//		article.setLink(link);
//		if(StringUtils.isNotBlank(langCode)){
//			article.setLangCode(Integer.parseInt(langCode.split("_")[0]));
//			article.setLangName(langCode.split("_")[1]);
//		}
//		
//		
//		if(StringUtils.isNotBlank(journalName)){
//			article.setJournalId(journalName.split("_")[0]);
//			article.setJournalName(journalName.split("_")[1]);
//		}
//		article.setJournalLink(journallink);
//		article.setSource(source);
//		article.setHsk(hsk);
//		if(mediacoverLink !=null && !mediacoverLink.isEmpty()){
//			article.setCoverLink(tempimagePath);
//		}else{
//			article.setCoverLink(coverLink);
//		}
//		article.setContent(content_editor);
//		articleSaveService.save(article);
//		Map<String, Object> jsonMap = new HashMap<String, Object>();
//		jsonMap.put("success", article != null ? true : false);
//		response.getWriter().print(JSON.toJSONString(jsonMap));
//		System.out.println(content_editor);
//	}
//	
//	/**
//	 * 根据杂志id 获取 杂志信息
//	 */
//	@RequestMapping(value = "/articleLoad")
//	public void articleLoad(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String id = request.getParameter("id");
//		if (!StringUtil.isNull(id)) {
//		     ArticleSave info = articleSaveService.load(id);
//			System.out.println(JSON.toJSONString(info));
//			response.getWriter().print(JSON.toJSONString(info));
//		}
//	}
//	/**
//	 * 杂志删除
//	 */
//	@RequestMapping(value = "/articleDel")
//	public void articleDel(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String id = request.getParameter("id");
//		if (!StringUtil.isNull(id)) {
//			articleService.delete(id);
//		}
//		Map<String, Object> jsonMap = new HashMap<String, Object>();
//		jsonMap.put("success", !StringUtil.isNull(id));
//		response.getWriter().print(JSON.toJSONString(jsonMap));
//	}
//	
	
}