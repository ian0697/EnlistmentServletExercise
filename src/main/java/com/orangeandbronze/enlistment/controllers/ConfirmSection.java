package com.orangeandbronze.enlistment.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.orangeandbronze.enlistment.domain.Days;
import com.orangeandbronze.enlistment.domain.Schedule;

@WebServlet("/confirm_section")
public class ConfirmSection extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
	    PrintWriter out = resp.getWriter();
	    System.out.println(req.getParameter("sectionId"));
	    
	    String scheduleString = "";
	    Days input = Days.valueOf(req.getParameter("day"));
	    String period = req.getParameter("period");
	    
	    scheduleString = input + " " + period;
	    Schedule schedule = Schedule.valueOf(scheduleString);
	    
	    out.println(req.getParameter("sectionId"));
	    out.println(req.getParameter("subject"));
	    out.println(req.getParameter("room"));
	    out.println(req.getParameter("fac"));
	    out.println(schedule.toString());
	}
}
