package com.orangeandbronze.enlistment.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;

import org.junit.Test;

import com.orangeandbronze.enlistment.dao.EnlistmentsDAO;
import com.orangeandbronze.enlistment.dao.SectionDAO;
import com.orangeandbronze.enlistment.dao.jdbc.DataSourceManager;
import com.orangeandbronze.enlistment.dao.jdbc.EnlistmentDaoJdbc;
import com.orangeandbronze.enlistment.dao.jdbc.SectionDaoJdbc;

public class SectionDaoJdbcIT {

	@Test
	public void createSection() throws Exception{
		DataSource ds = DataSourceManager.defaultDataSource(getClass());
	    SectionDAO dao = new SectionDaoJdbc(ds); 

		// Create section if not exist
	    if(dao.findBy("CAPACITY1") == null){
    	    Section section = new Section("CAPACITY1");
	 	    dao.create(section);
	    }
	    
	    // Check section
	    Section expectedSection = new Section("CAPACITY1");
	    assertEquals(expectedSection.getSectionId(), dao.findBy("CAPACITY1").getSectionId());
	}
	
	@Test
	public void findAll() throws Exception {
		DataSource ds = DataSourceManager.defaultDataSource(getClass());
	    SectionDAO dao = new SectionDaoJdbc(ds); 
	    
	    Collection<Section> sections = new ArrayList<>(dao.findAll());
		System.out.println("There are " + sections.size() + " sections in the database.");
		
		int expectedRowCount = 8;
	    assertEquals(expectedRowCount, sections.size());
	    
	}
	

}
