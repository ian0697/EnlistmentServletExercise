package com.orangeandbronze.enlistment.controllers;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/invalid_email")
public class InvalidStudentNumber extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("No student with student # "+ request.getParameter("username") + " exists. Please submit your student number again.</p>");
		request.getRequestDispatcher("/index.html").include(request, response);
	}

}
