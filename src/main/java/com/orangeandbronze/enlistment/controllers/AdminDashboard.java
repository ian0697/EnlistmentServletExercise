package com.orangeandbronze.enlistment.controllers;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.orangeandbronze.enlistment.dao.RoomDAO;
import com.orangeandbronze.enlistment.dao.SectionDAO;
import com.orangeandbronze.enlistment.dao.SubjectDAO;
import com.orangeandbronze.enlistment.dao.jdbc.DataSourceManager;
import com.orangeandbronze.enlistment.dao.jdbc.RoomDaoJdbc;
import com.orangeandbronze.enlistment.dao.jdbc.SectionDaoJdbc;
import com.orangeandbronze.enlistment.dao.jdbc.SubjectDaoJdbc;
import com.orangeandbronze.enlistment.domain.Room;
import com.orangeandbronze.enlistment.domain.Section;
import com.orangeandbronze.enlistment.domain.Subject;


@WebServlet("/home-admin")
public class AdminDashboard extends HttpServlet{
	final DataSource dataSource = DataSourceManager.getDataSource();
		
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		// display subjects, rooms and sections
		showSubjects(request, session);
		showRooms(request,session);
		showSections(request, session);
			
		request.getRequestDispatcher("/WEB-INF/AdminDashboard.jsp")
			.forward(request, response);
	}
	
	private void showSubjects(HttpServletRequest request, HttpSession session){
		SubjectDAO subjectDao = new SubjectDaoJdbc(dataSource);
		ArrayList<Subject> subjects = new ArrayList<>(subjectDao.findAll());
		request.setAttribute("subjects", subjects);
		session.setAttribute("subjects", subjects);
	}
	
	private void showRooms(HttpServletRequest request, HttpSession session){
		RoomDAO roomDao = new RoomDaoJdbc(dataSource);
		ArrayList<Room> rooms = new ArrayList<>(roomDao.findAll());
		request.setAttribute("rooms", rooms);
		session.setAttribute("rooms", rooms);
	}
	
	private void showSections(HttpServletRequest request, HttpSession session){
		SectionDAO sectionDao = new SectionDaoJdbc(dataSource);
		ArrayList<Section> sections = new ArrayList<>(sectionDao.findAll());
		request.setAttribute("sections", sections);
		session.setAttribute("sections", sections);
	}
}
