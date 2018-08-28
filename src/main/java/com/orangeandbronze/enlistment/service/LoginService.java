package com.orangeandbronze.enlistment.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.orangeandbronze.enlistment.dao.DataAccessException;
import com.orangeandbronze.enlistment.dao.jdbc.DataSourceManager;
import com.orangeandbronze.enlistment.dao.jdbc.StudentDaoJdbc;
import com.orangeandbronze.enlistment.domain.Student;

public class LoginService {

	private final DataSource dataSource = DataSourceManager.getDataSource();
	
	private StudentDaoJdbc studentDao;
	
	public LoginService(StudentDaoJdbc studentDao) {
		this.studentDao = studentDao;
	}
	
	public boolean doesStudentExist(int studentNumber){	
		Student student = studentDao.findWithoutSectionsBy(studentNumber);
		return student != Student.NONE;
	}
	
}
