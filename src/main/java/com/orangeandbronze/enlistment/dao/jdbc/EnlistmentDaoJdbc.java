package com.orangeandbronze.enlistment.dao.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.sql.DataSource;

import com.orangeandbronze.enlistment.dao.DataAccessException;
import com.orangeandbronze.enlistment.dao.EnlistmentsDAO;
import com.orangeandbronze.enlistment.dao.StaleDataException;
import com.orangeandbronze.enlistment.domain.Section;
import com.orangeandbronze.enlistment.domain.Student;

public class EnlistmentDaoJdbc implements EnlistmentsDAO {
	
	private final DataSource dataSource;
	private final static Map<String, String> sqlCache = new HashMap<>();
	
	public EnlistmentDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

	@Override
	public void create(Student student, Section section) {
//		try(Connection conn = dataSource.getConnection()){
//			PreparedStatement stmt = conn.prepareStatement(getSql("InsertEnlistment.sql"));
//			stmt.setInt(1, student.getStudentNumber());
//			stmt.setString(2, section.getSectionId());
//		
//		} catch (SQLException e) {
//			throw new DataAccessException(
//		            "Problem creating enlistement data for student with student number "
//		                    + student.getStudentNumber(), e);
//		}
		
//		final String sql = "INSERT  INTO enlistments(student_number, section_id) " 
//				+ "VALUES(?,?)";
//		withConn(conn-> {
//			
//			try(PreparedStatement stmt = conn.prepareStatement(sql)) {
//				stmt.setInt(1, student.getStudentNumber());
//				stmt.setString(2, section.getSectionId());
//				stmt.executeUpdate();
//				
//			} catch(SQLException e){
//				throw new DataAccessException("Problem during insert enlistment operation for " + student, e);
//			}
//		});
		
		try (Connection conn = dataSource.getConnection()) {
	        conn.setAutoCommit(false);
	        try (PreparedStatement stmtUpdateVersion = conn.prepareStatement(
	                "UPDATE sections SET version = version + 1 WHERE section_id = ? AND version = ?")) {
	            stmtUpdateVersion.setString(1, section.getSectionId());
	            stmtUpdateVersion.setInt(2, section.getVersion());
	            int recordsUpdated = stmtUpdateVersion.executeUpdate();
	            if (recordsUpdated != 1) {
	                conn.rollback();
	                throw new StaleDataException("Enlistment data for "
	                        + section + " was not updated to current version.");
	            }
	        } catch (SQLException e) {
	            conn.rollback();
	            throw e;
	        }
	        try (PreparedStatement stmt = conn.prepareStatement(
	                "INSERT INTO enlistments(student_number, section_id) VALUES(?, ?)")) {
	            stmt.setInt(1, student.getStudentNumber());
	            stmt.setString(2, section.getSectionId());
	            if (stmt.executeUpdate() > 0) {
	                conn.commit();
	            } else {
	                conn.rollback();
	            }
	        } catch (SQLException e) {
	            conn.rollback();
	            throw e;
	        }
	        conn.setAutoCommit(true);
	    } catch (SQLException e) {
	        throw new DataAccessException(
	                "while inserting into enlistments table for " + student + ", " + section,   e);
	    }
	}


	@Override
	public void delete(int studentNumber, String sectionId) {
		try(Connection conn = dataSource.getConnection()){
			PreparedStatement stmt = conn.prepareStatement(getSql("DeleteEnlistment.sql"));
			stmt.setInt(1, studentNumber);
			stmt.setString(2, sectionId);
			stmt.execute();
			System.out.println("Student with student number: " + studentNumber + " deleted!");
		
		} catch (SQLException e) {
			throw new DataAccessException(
		            "Problem deleting enlistment data!");
		}
	}
	
	
	void withConn(Consumer<Connection> consumer) {
		try(Connection conn = dataSource.getConnection()) {
			consumer.accept(conn);
			
		} catch (SQLException e) {
			throw new DataAccessException("Problem retrieving connection from datasource" ,e);
		}
	}
	
	
	private String getSql(String sqlFile) {
		 if (!sqlCache.containsKey(sqlFile)) {
		    try (Reader reader = new BufferedReader(new InputStreamReader(
		            getClass().getClassLoader().getResourceAsStream(sqlFile)))) {
		        StringBuilder bldr = new StringBuilder();
		        int i = 0;
		        while ((i = reader.read()) > 0) {
		            bldr.append((char) i);
		        }
		        return bldr.toString();
		    } catch (IOException e) {
		        throw new DataAccessException("Problem while trying to read file '"
		                    + sqlFile + "' from classpath.", e);
		    }
		 }
		 return sqlCache.get(sqlFile);
	}

}
