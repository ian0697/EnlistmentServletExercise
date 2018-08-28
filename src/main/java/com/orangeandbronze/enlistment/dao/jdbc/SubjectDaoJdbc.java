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
import com.orangeandbronze.enlistment.dao.SubjectDAO;
import com.orangeandbronze.enlistment.domain.Room;
import com.orangeandbronze.enlistment.domain.Subject;

public class SubjectDaoJdbc implements SubjectDAO{

private final DataSource dataSource;
	
	public SubjectDaoJdbc(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	@Override
	public Collection<Subject> findAll() {
		try(Connection conn = dataSource.getConnection()){
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM subjects");
			boolean found = false;
	        Collection<Subject> subjects = new ArrayList<>();
	        try (ResultSet rs = stmt.executeQuery()) {
		        while (rs.next()) {
		        	if (!found) {
	                    found = true;
	                }
	                if (!StringUtils.isBlank(rs.getString("subject_id"))) {
	                	subjects.add(
                			new Subject(rs.getString("subject_id"))
            			);                    
	                }
	            }
		        return found ? new ArrayList<Subject>(subjects): Collections.EMPTY_LIST;
	        }
		} catch (SQLException e) {
			throw new DataAccessException();
		}
	}

	@Override
	public Collection<String> findAllIds() {
		try(Connection conn = dataSource.getConnection()){
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM subjects");
			boolean found = false;
	        Collection<String> subjectIds = new ArrayList<>();
	        try (ResultSet rs = stmt.executeQuery()) {
		        while (rs.next()) {
		        	if (!found) {
	                    found = true;
	                }
	                if (!StringUtils.isBlank(rs.getString("subject_id"))) {
	                	subjectIds.add(rs.getString("subject_id"));                    
	                }
	            }
		        return found ? new ArrayList<String>(subjectIds): Collections.EMPTY_LIST;
	        }
		} catch (SQLException e) {
			throw new DataAccessException();
		}
	}

}
