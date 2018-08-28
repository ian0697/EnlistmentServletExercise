package com.orangeandbronze.enlistment.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.orangeandbronze.enlistment.dao.AdminDAO;
import com.orangeandbronze.enlistment.dao.jdbc.AdminDaoJdbc;
import com.orangeandbronze.enlistment.dao.jdbc.DataSourceManager;
import com.orangeandbronze.enlistment.dao.jdbc.StudentDaoJdbc;
import com.orangeandbronze.enlistment.domain.Admin;
import com.orangeandbronze.enlistment.domain.Student;
import com.orangeandbronze.enlistment.service.LoginService;

@WebServlet("/admin-login")
public class AdminLogin extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int adminId = Integer.parseInt(request.getParameter("username"));
		
		AdminDAO adminDao = new AdminDaoJdbc(DataSourceManager.getDataSource());
		Map<String, Object> adminList = new HashMap<>(adminDao.findAdminInfoBy(adminId));
		
		if(!adminList.isEmpty()){
			Admin admin = new Admin(
				    Integer.parseInt(adminList.get("id").toString()), 
					(String)adminList.get("lastname"), 
					(String)adminList.get("firstname")
				);
			request.getSession().setAttribute("admin", admin);
			response.sendRedirect("home-admin");
		} else {
			request.getRequestDispatcher("/invalid_email")
            .forward(request, response);
		}
	}

}
