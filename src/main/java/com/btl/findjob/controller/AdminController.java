package com.btl.findjob.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.btl.findjob.model.AdminCriteria;
import com.btl.findjob.model.AdminPageDTO;
import com.btl.findjob.model.BoardCriteria;
import com.btl.findjob.model.BoardPageDTO;
import com.btl.findjob.model.CompanyListVO;
import com.btl.findjob.model.UserDTO;
import com.btl.findjob.service.AdminService;
import com.btl.findjob.service.BoardService;
import com.btl.findjob.service.CompanyService;
import com.btl.findjob.service.MypageService;
import com.btl.findjob.service.UserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class AdminController {

@Autowired
AdminService adminservice;
@Inject
UserService userservice;
@Autowired
CompanyService companyService;

    @RequestMapping(value = "admin_page", method = {RequestMethod.GET, RequestMethod.POST})
    public String admin_page(AdminCriteria cri,Model model) {
        model.addAttribute("user_list", adminservice.getList(cri));
        int total = adminservice.getTotal(cri);
        model.addAttribute("pageMaker", new AdminPageDTO(cri,total));
        return "admin/admin_page";
    }

    @RequestMapping(value = "user_status", method = {RequestMethod.GET, RequestMethod.POST})
    public String user_status(Model model, @Param("user_email") String user_email) {

        model.addAttribute("Uinfo_list", userservice.user_info(user_email));

        return "admin/user_status";
    }

    @RequestMapping(value = "/user_InterviewReview", method = {RequestMethod.GET, RequestMethod.POST})
    public String user_InterviewReview(@Param("user_id") int user_id, Model model) {

        model.addAttribute("user_id", user_id);
        model.addAttribute("myInterviewList", adminservice.myInterviewReview(user_id));

        return "admin/user_InterviewReview";
    }

    @RequestMapping(value = "/user_ReviewComment", method = {RequestMethod.GET, RequestMethod.POST})
    public String user_ReviewComment(@Param("user_id") int user_id, Model model) {

        model.addAttribute("myReviewCommentList", adminservice.myReviewComment(user_id));

        return "admin/user_ReviewComment";
    }

    //관리자 인터셉터
    @RequestMapping(value = "adminceptor", method = {RequestMethod.GET, RequestMethod.POST})
    public String interceptor(Model model) {

    	Map<String, List<CompanyListVO>> map1 = new HashMap<>();
        map1.put("follow 많은 기업", companyService.getManyFollowOrdersList());
        map1.put("면접리뷰 많은 기업", companyService.getManyInterviewReviewOrdersList());
        
        Map<Integer, Map<String, List<CompanyListVO>>> map2 = new HashMap<>();
        List<Map<String, List<CompanyListVO>>> list = new ArrayList<>();
        String[] categoryArr = companyService.getCategoryArr();
        for(int i = 0; i < categoryArr.length/2; i++) {
        	list.add(new HashMap<>());
        	for(int j = 2*i; j < categoryArr.length; j++) {
            	if(list.get(i).size()==2) continue;
        		list.get(i).put(categoryArr[j], companyService.getMostCtOrdersList(j));
            	if(j%2 == 1) map2.put(i, list.get(i)); 
            }
        }

        model.addAttribute("map1", map1);
        model.addAttribute("map2", map2);

        String atr = "";
        atr += "<script>";
        atr += "Swal.fire({";
        atr += "type: 'warning',";
        atr += "html: '관리자 페이지입니다.<br> 관리자 계정으로 로그인 해주십시오.'";
        atr += "})";
        atr += "</script>";
        
        model.addAttribute("atr", atr);

        return "index";
    }

    @RequestMapping(value = "/grade_modify", method = {RequestMethod.POST})
    @ResponseBody
    public String grade_modify(@Param("authorization_id") String authorization_id, @Param("user_id") int user_id) {

        adminservice.grade_modify(user_id, authorization_id);
        return "1";
    }

    @RequestMapping(value = "user_search", method = {RequestMethod.GET})
    public String user_search(@Param("user_name") String user_name, Model model) {

        model.addAttribute("user_list", adminservice.user_search(user_name));
        return "admin/user_search";
    }
}
