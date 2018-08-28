package com.orangeandbronze.enlistment.domain;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.Collection;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;

import com.orangeandbronze.enlistment.dao.StudentDAO;
import com.orangeandbronze.enlistment.dao.jdbc.DataSourceManager;
import com.orangeandbronze.enlistment.dao.jdbc.StudentDaoJdbc;

public class StudentDaoJdbcIT {
	@Test
	public void findStudentWithNoSections() throws Exception {
		DataSource ds = initializeDataSource("StudentNoSections.xml");
		StudentDAO dao = new StudentDaoJdbc(ds);
		Integer studentNumber = 1;
		Student actualStudent = dao.findWithoutSectionsBy(studentNumber);
	    assertEquals(studentNumber, actualStudent.getStudentNumber());
	    assertEquals("Mickey", actualStudent.getFirstname());
	    assertEquals("Mouse", actualStudent.getLastname());
	}
	
	@Test
	public void findStudentThatHasSections() throws Exception {
		DataSource ds = initializeDataSource("DefaultDataset.xml");
	    StudentDAO dao = new StudentDaoJdbc(ds); 
	    Student actualStudent = dao.findWithoutSectionsBy(3);
		Integer studentNumber = 3;
	    assertEquals(studentNumber, actualStudent.getStudentNumber());
	    assertEquals("Scooby", actualStudent.getFirstname());
	    assertEquals("Doo", actualStudent.getLastname());
	}
	
	@Test
	public void findStudentThatHasSections_verifySchedule() throws Exception {
		DataSource ds = initializeDataSource("DefaultDataset.xml");
	    StudentDAO dao = new StudentDaoJdbc(ds); 
	    Student actualStudent = dao.findWithSectionsBy(3);
	    Collection<Section> actualSections = actualStudent.getSections();
	    assertThat(actualSections,
	    		Matchers.contains(
	    				new Section(
	    						"HASSTUDENTS", 
    							new Subject("COM1"), 
    							Schedule.valueOf("TF 11:30-13:00"), 
    							new Room("AVR1"))));
		
	}

	
	private DataSource initializeDataSource(String resourceDataSet) throws Exception {
		DataSource ds = DataSourceManager.getDataSource();
	    Connection jdbcConnection = ds.getConnection();
	    jdbcConnection.createStatement().execute("SET CONSTRAINTS ALL DEFERRED;");
	    
	    IDatabaseConnection dbUnitConnection =
	            new DatabaseConnection(jdbcConnection);
	    
	    FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
	    builder.setDtdMetadata(false);
	    
	    IDataSet dataSet = builder.build(getClass().getClassLoader()
	            .getResourceAsStream(resourceDataSet));
	    try {
	        DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataSet);
	    } finally {
	        dbUnitConnection.close(); // don't forget to close the connection!
	    }
	    return ds;
	}
	
}
