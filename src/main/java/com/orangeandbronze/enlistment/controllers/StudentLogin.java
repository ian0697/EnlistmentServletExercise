package com.orangeandbronze.enlistment.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.orangeandbronze.enlistment.dao.jdbc.DataSourceManager;
import com.orangeandbronze.enlistment.dao.jdbc.StudentDaoJdbc;
import com.orangeandbronze.enlistment.domain.Student;
import com.orangeandbronze.enlistment.service.LoginService;

@WebServlet("/student-login")
public class StudentLogin extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int studentNumber = Integer.parseInt(request.getParameter("username"));
		StudentDaoJdbc studentDao;

		try {
			studentDao = new StudentDaoJdbc(DataSourceManager.getDataSource());
			LoginService loginService = new LoginService(studentDao);
			
			if(loginService.doesStudentExist(studentNumber)) {
				Student student = studentDao.findWithoutSectionsBy(studentNumber);
				
				// put student to session
				request.getSession().setAttribute("student", student);
		        response.sendRedirect("home");
				
			} else {
				request.getRequestDispatcher("/invalid_email")
	            .forward(request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
