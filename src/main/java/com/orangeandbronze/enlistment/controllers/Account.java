package com.orangeandbronze.enlistment.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.orangeandbronze.enlistment.dao.jdbc.DataSourceManager;
import com.orangeandbronze.enlistment.dao.jdbc.StudentDaoJdbc;
import com.orangeandbronze.enlistment.domain.Student;
import com.orangeandbronze.enlistment.service.LoginService;

@WebServlet("/account")
public class Account extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String account = request.getParameter("acct");
		
		if(account.equalsIgnoreCase("admin")) {
			response.sendRedirect("admin.html");
		} else {
			response.sendRedirect("student.html");
		}
		
	}
}
