package com.orangeandbronze.enlistment.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.orangeandbronze.enlistment.dao.FacultyDAO;
import com.orangeandbronze.enlistment.dao.RoomDAO;
import com.orangeandbronze.enlistment.dao.SubjectDAO;
import com.orangeandbronze.enlistment.dao.jdbc.DataSourceManager;
import com.orangeandbronze.enlistment.dao.jdbc.FacultyDaoJdbc;
import com.orangeandbronze.enlistment.dao.jdbc.RoomDaoJdbc;
import com.orangeandbronze.enlistment.dao.jdbc.SubjectDaoJdbc;

@WebServlet("/add_section")
public class AddSection extends HttpServlet{

	private final DataSource dataSource = DataSourceManager.getDataSource();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		
		RoomDAO roomDao = new RoomDaoJdbc(dataSource);
		FacultyDAO facultyDao = new FacultyDaoJdbc(dataSource);
		SubjectDAO subjectDao = new SubjectDaoJdbc(dataSource);
		
		
		session.setAttribute("rooms", roomDao.findAll());
		session.setAttribute("faculty", facultyDao.findAll());
		session.setAttribute("subjects", subjectDao.findAll());
		
		req.getRequestDispatcher("/WEB-INF/AddSection.jsp")
		.forward(req, resp);
	}
}
