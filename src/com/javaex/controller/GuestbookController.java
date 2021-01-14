package com.javaex.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestVo;


@WebServlet("/gbc")
public class GuestbookController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("guestbook controller");
		
		String action = request.getParameter("action");
		System.out.println("action=" + action);
		
		if("addlist".equals(action)) {
			System.out.println("방명록");
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/addList.jsp");
		}else if("add".equals(action)) {
			System.out.println("방명록 등록");
			
			//파라미터 값
			String name = request.getParameter("name");
			String password = request.getParameter("pass");
			String content = request.getParameter("content");
			
			//Vo로 묶기
			GuestVo guestVo = new GuestVo(name, password, content);
			
			//dao addguest(vo) 사용
			GuestDao gusetDao = new GuestDao();
			gusetDao.addGuest(guestVo);
			
			//다시 addList로 리다이렉트
			WebUtil.redirect(request, response, "/mysite2/gbc?action=addList");
			
		}else if("deleteForm".equals(action)) {
			System.out.println("삭제 폼");
			
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/deleteForm.jsp");
		}else if("delete".equals(action)) {
			System.out.println("방명록 삭제");
			
			//파라미터 값
			int no = Integer.parseInt(request.getParameter("no"));
			String password = request.getParameter("pass");
			
			//dao delete
			GuestDao geustDao = new GuestDao();
			int confirm = geustDao.guestDelete(no, password);
			
			if(confirm == 1) {
				WebUtil.redirect(request, response, "/mysite2/gbc?action=addList");
			}else {
				WebUtil.redirect(request, response, "/mysite2/gbc?action=deleteForm");
				System.out.println("비밀번호 오류");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
