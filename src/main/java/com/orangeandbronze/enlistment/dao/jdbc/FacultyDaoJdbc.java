package com.orangeandbronze.enlistment.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import com.orangeandbronze.enlistment.dao.DataAccessException;
import com.orangeandbronze.enlistment.dao.FacultyDAO;
import com.orangeandbronze.enlistment.domain.Faculty;

public class FacultyDaoJdbc implements FacultyDAO {

	private DataSource dataSource;
	
	public FacultyDaoJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public Collection<Faculty> findAll() {
		try(Connection conn = dataSource.getConnection()){
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM faculty");
			boolean found = false;
	        Collection<Faculty> faculty = new ArrayList<>();
	        try (ResultSet rs = stmt.executeQuery()) {
		        while (rs.next()) {
		        	if (!found) {
	                    found = true;
	                }
		        	
	                if (!StringUtils.isBlank(rs.getString("faculty_number"))) {
	                	faculty.add(
	                		new Faculty(
	                				rs.getInt("faculty_number"), 
	                				rs.getString("firstname"),
	                				rs.getString("lastname")
	            		));                   
	                }
	            }
		        
		        return found ? new ArrayList<Faculty>(faculty): Collections.EMPTY_LIST;
	        }
		} catch (SQLException e) {
			throw new DataAccessException();
		}
	}

	@Override
	public Faculty findBy(int facultyNumber) {
		try (Connection conn = dataSource.getConnection();
	            PreparedStatement stmt = conn.prepareStatement(
	                    "SELECT * FROM faculty WHERE faculty_number = ?")) {
		        stmt.setInt(1, facultyNumber);
		        Faculty faculty = Faculty.TBA;
		        try (ResultSet rs = stmt.executeQuery()) {
		            if (rs.next()) {
		                String firstName = rs.getString("firstname");
		                String lastName = rs.getString("lastname");
		                faculty = new Faculty(facultyNumber, firstName, lastName);
		            }
		        }
		        return faculty;
		    } catch (SQLException e) {
		        throw new DataAccessException(
		                "Problem retrieving student data for student " + 
		                        "with student number " + facultyNumber, e);
		    }
	}

}
