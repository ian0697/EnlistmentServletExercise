package com.orangeandbronze.enlistment.domain;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

import com.orangeandbronze.enlistment.dao.EnlistmentsDAO;
import com.orangeandbronze.enlistment.dao.SectionDAO;
import com.orangeandbronze.enlistment.dao.StudentDAO;
import com.orangeandbronze.enlistment.dao.jdbc.DataSourceManager;
import com.orangeandbronze.enlistment.dao.jdbc.EnlistmentDaoJdbc;
import com.orangeandbronze.enlistment.dao.jdbc.SectionDaoJdbc;
import com.orangeandbronze.enlistment.dao.jdbc.StudentDaoJdbc;

public class EnlistmentDaoJdbcIT {

	// run once only
	@Ignore
	@Test
	public void insertEnlistment() throws Exception{
		DataSource ds = DataSourceManager.defaultDataSource(getClass());
		
		// insert enlistment
	    EnlistmentsDAO dao = new EnlistmentDaoJdbc(ds); 
	    Student student = new Student(1);
	    Section section = new Section("MHY987");
	    dao.create(student, section);
	    
	    // database must have enlistment with student#1 having section - MHY987
	    StudentDAO studentDao = new StudentDaoJdbc(ds); 
	    Student actualStudent = studentDao.findWithSectionsBy(1);
	    Collection<Section> actualSections = actualStudent.getSections();

	    // assertion
	    assertThat(actualSections,
    	       Matchers.contains(new Section("MHY987"))
	       );
	}

	// run once only4
	@Ignore
	@Test
	public void insertEnlistment_2() throws Exception{
		DataSource ds = DataSourceManager.defaultDataSource(getClass());
		
		// insert enlistment
	    EnlistmentsDAO dao = new EnlistmentDaoJdbc(ds); 
	    Student student = new Student(2);
	    Section section = new Section("TFZ321");
	    dao.create(student, section);

	    // database must have enlistment with student#2 having section - TFZ321
	    StudentDAO studentDao = new StudentDaoJdbc(ds); 
	    Student actualStudent = studentDao.findWithSectionsBy(2);
	    Collection<Section> actualSections = actualStudent.getSections();
	    
	    // assertion
	    assertThat(actualSections,
	       Matchers.contains(new Section("TFZ321"))
        );
	}

	@Test
	public void deleteEnlistment() throws Exception{
		DataSource ds = DataSourceManager.defaultDataSource(getClass());
		
		// delete enlistment
	    EnlistmentsDAO dao = new EnlistmentDaoJdbc(ds); 
	    dao.delete(3, "HASSTUDENTS");

	    // section of student # 1 must be removed
	    Integer studentNumber = 1;
	    StudentDAO studentDao = new StudentDaoJdbc(ds);
	    Student actualStudent = studentDao.findWithoutSectionsBy(1);
	    
	    // assertion
	    assertEquals(studentNumber, actualStudent.getStudentNumber());
	    assertEquals("Mickey", actualStudent.getFirstname());
	    assertEquals("Mouse", actualStudent.getLastname());
	}

	@Test
	public void deleteEnlistment_1() throws Exception{
		DataSource ds = DataSourceManager.defaultDataSource(getClass());
		
		//Delete enlistment
	    EnlistmentsDAO dao = new EnlistmentDaoJdbc(ds); 
	    dao.delete(3, "HASSTUDENTS");

	    // section of student # 2 must be removed
	    Integer studentNumber = 2;
	    StudentDAO studentDao = new StudentDaoJdbc(ds);
	    Student actualStudent = studentDao.findWithoutSectionsBy(2);
	    
	    // assertion
	    assertEquals(studentNumber, actualStudent.getStudentNumber());
	    assertEquals("Bugs", actualStudent.getFirstname());
	    assertEquals("Bunny", actualStudent.getLastname());
	}

	@Test
	public void simultaneousCreate() throws Exception{
		DataSource ds = DataSourceManager.defaultDataSource(getClass());
		
	    EnlistmentsDAO enlistmentDao = new EnlistmentDaoJdbc(ds); 
	    SectionDAO sectionDao = new SectionDaoJdbc(ds);

	    Student student1 = new Student(1);
	    Student student2 = new Student(2);
	    Thread thread1 = new Thread() {
	        public void run() {
	            Section section = sectionDao.findBy("CAPACITY1"); // capacity 1
	            student1.enlist(section);
	            try {
	                Thread.sleep(100);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            enlistmentDao.create(student1, section);
	        }
	    };
	    Thread thread2 = new Thread() {
	        public void run() {
	            Section section = sectionDao.findBy("CAPACITY1"); // capacity 1
	            student2.enlist(section);
	            try {
	                Thread.sleep(100);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            enlistmentDao.create(student2, section);
	        }
	    };
	    thread1.start();
	    thread2.start();
	    thread1.join();
	    thread2.join();
	    String sql = "SELECT COUNT(*) FROM enlistments "
	            + " WHERE section_id = 'CAPACITY1'";
        ResultSet rs = ds.getConnection().prepareStatement(sql).executeQuery();
        rs.next();
        assertEquals(1, rs.getInt(1));
	}

	
	
	
}
