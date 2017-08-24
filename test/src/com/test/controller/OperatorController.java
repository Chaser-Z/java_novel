package com.test.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.test.model.NovelList;
import com.test.service.FeedbackService;
import com.test.service.NovelListService;
import com.test.service.NovelUserService;

@Controller
@RequestMapping(value = "/operator")
public class OperatorController {

	@Autowired
	private NovelUserService novelUserService;
	@Autowired
	private FeedbackService feedbackService;
	@Autowired
	private NovelListService novelListService;
	
	
	/**
	 * 用户信息
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/user")
	public ModelAndView user(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView();
		view.setViewName("operator/user");
		return view;
	}
	
	@RequestMapping(value = "/userList")
	public void userList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rows = request.getParameter("rows"); // 总页数
		String curr = request.getParameter("page"); // 当前页
		String name = request.getParameter("name");
		String identityType = request.getParameter("identityType");

		Map<String, Object> jsonMap = novelUserService.page(rows, curr, name, identityType);
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}

	/**
	 * 用户反馈
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/feedback")
	public ModelAndView feedback(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView();
		view.setViewName("operator/feedback");
		return view;
	}
	
	@RequestMapping(value = "/feedbackList")
	public void feedbackList(HttpServletRequest request, HttpServletResponse response) throws Exception  {
		String rows = request.getParameter("rows"); // 总页数
		String curr = request.getParameter("page"); // 当前页
		String content = request.getParameter("content");
		Map<String, Object> jsonMap = feedbackService.page(rows, curr, content);
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}

	/**
	 * 统计分析
	 * 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "/statistics")
	public ModelAndView statistics(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView();
		view.setViewName("operator/statistics");
		return view;
	}
	
	@RequestMapping(value = "/regStatistics")
	public void regStatistics(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = novelUserService.statistics();		
		response.getWriter().print(JSON.toJSONString(map));
	}
	
	@RequestMapping(value = "/articleStatistics")
	public void articleStatistics(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rows = request.getParameter("rows"); // 总页数
		String curr = request.getParameter("page"); // 当前页
		String sort = request.getParameter("sort");
		String order = request.getParameter("order");
		
		Map<String, Object> jsonMap = novelListService.page(rows, curr);
		//System.out.println(JSON.toJSONString(jsonMap));
		List<NovelList> list = (List)jsonMap.get("rows");
		for(NovelList as : list){
			NovelList a = novelListService.load(as.getArticle_id());
			as.setTitle(a.getTitle());
		}		
		System.out.println(JSON.toJSONString(jsonMap));
		response.getWriter().print(JSON.toJSONString(jsonMap));
	}
}
