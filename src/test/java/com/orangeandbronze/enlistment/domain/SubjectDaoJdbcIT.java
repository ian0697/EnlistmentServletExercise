package com.orangeandbronze.enlistment.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.orangeandbronze.enlistment.dao.SectionDAO;
import com.orangeandbronze.enlistment.dao.SubjectDAO;
import com.orangeandbronze.enlistment.dao.jdbc.DataSourceManager;
import com.orangeandbronze.enlistment.dao.jdbc.SectionDaoJdbc;
import com.orangeandbronze.enlistment.dao.jdbc.SubjectDaoJdbc;

public class SubjectDaoJdbcIT {

	@Test
	public void findAll() throws Exception {
		 
		DataSource ds = DataSourceManager.defaultDataSource(getClass());
	    SubjectDAO dao = new SubjectDaoJdbc(ds); 
	    
	    Collection<Subject> subjects = new ArrayList<>(dao.findAll());
		System.out.println("There are " + subjects.size() + " subjects in the database.");
		
		int expectedRowCount = 5;
	    assertEquals(expectedRowCount, subjects.size());
		
	}
	
	@Test 
	public void findAllIds() throws Exception {
		DataSource ds = DataSourceManager.defaultDataSource(getClass());
	    SubjectDAO dao = new SubjectDaoJdbc(ds); 
	    
	    Collection<String> subjectIds = new ArrayList<>(dao.findAllIds());
		System.out.println("There are " + subjectIds.size() + " subject ids in the database.");
		
	    assertThat(subjectIds, Matchers.containsInAnyOrder("PHYSICS71" , "KAS1", "MATH11", "PHILO1","COM1"));
	}

}
