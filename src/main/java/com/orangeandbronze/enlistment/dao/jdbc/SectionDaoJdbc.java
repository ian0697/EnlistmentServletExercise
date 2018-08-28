package com.orangeandbronze.enlistment.dao.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import com.orangeandbronze.enlistment.dao.DataAccessException;
import com.orangeandbronze.enlistment.dao.SectionDAO;
import com.orangeandbronze.enlistment.domain.Faculty;
import com.orangeandbronze.enlistment.domain.Room;
import com.orangeandbronze.enlistment.domain.Schedule;
import com.orangeandbronze.enlistment.domain.Section;
import com.orangeandbronze.enlistment.domain.Student;
import com.orangeandbronze.enlistment.domain.Subject;

public class SectionDaoJdbc implements SectionDAO {
	
	private final DataSource dataSource;
	private final static Map<String, String> sqlCache = new HashMap<>();
	
	public SectionDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }
	
	@Override
	public Section findBy(String sectionId) {
		return withConn(conn -> {
			Map<String, Object> result = findOne(conn
					, getSql("FindSectionById.sql"), sectionId);
			Subject subject = newSubject(result.get("subject_id").toString());
			Schedule schedule = Schedule.valueOf(result.get("schedule").toString());
			Integer facultyNumber = (int) result.get("faculty_number");
			Faculty faculty = (facultyNumber !=null || facultyNumber == 0) ?
					Faculty.TBA : 
						new Faculty(facultyNumber,
								result.get("firstname").toString(),
								result.get("lastname").toString()
							);
			String roomName = result.get("room_name").toString();
			Room room = (StringUtils.isBlank(roomName) || roomName.trim().equalsIgnoreCase("TBA")) ? Room.TBA :
				new Room(roomName, (int)result.get("capacity"));
			int version = (int) result.get("version");
			return new Section(sectionId, subject, schedule, room, faculty, retrieveStudents(sectionId, conn),version);
		});
	}


	@Override
	public Collection<Section> findByStudentNo(int studentNumber) {
			
		try(Connection conn = dataSource.getConnection()){
			String sql = "SELECT * FROM enlistments WHERE student_number = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, studentNumber);
		
		} catch (SQLException e) {
			throw new DataAccessException();
		}

		
		return null;
	}

	@Override
	public Collection<Section> findByNotStudentNo(int studentNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(Section section) {
		try(Connection conn = dataSource.getConnection()){
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(getSql("InsertSection.sql"));
			stmt.setString(1, section.getSectionId());
			stmt.setString(2, section.getSchedule().toString());
			stmt.setString(3, section.getSubject().getSubjectId());
			stmt.setString(4, section.getRoom().getName());
			stmt.setInt(5, section.getFaculty().getFacultyNumber());
			stmt.setInt(6, section.getVersion());
			int rowsUpdated = stmt.executeUpdate();
			if(rowsUpdated > 0 ) {
				System.out.println("Section " + section + " created!");
				conn.commit();
			} else {
				System.out.println("Section " + section + " was rollbacked");
				conn.rollback();
			}
			
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
		
	}

	
	@Override
	public Collection<Section> findAll() {
		try(Connection conn = dataSource.getConnection()){
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sections");
			boolean found = false;
	        Collection<Section> sections = new ArrayList<>();
	        try (ResultSet rs = stmt.executeQuery()) {
		        while (rs.next()) {
		        	if (!found) {
	                    found = true;
	                }
		        	
	                if (!StringUtils.isBlank(rs.getString("section_id"))) {
	                    sections.add(
	                		new Section(
	            				rs.getString("section_id"),
	            				newSubject(rs.getString("subject_id")), 
	            				Schedule.valueOf(rs.getString("schedule")),
	            				new Room(rs.getString("room_name")))
	            		);                    
	                }
	            }
		        
		        return found ? new ArrayList<Section>(sections): Collections.EMPTY_LIST;
	        }
		} catch (SQLException e) {
			throw new DataAccessException();
		}
	}

	private Collection<Student> retrieveStudents(String sectionId, Connection conn) {
		String sql = "SELECT student_number FROM enlistments WHERE section_id = ?";
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, sectionId);
			try(ResultSet rs = stmt.executeQuery()){ 
				Collection<Student> students = new ArrayList<>();
				while(rs.next()){
					students.add(new Student(rs.getInt(1)));
				}
				return students;
			}
		} catch (SQLException e1) {
			throw new DataAccessException("PROBLEM RETRIEVING STUDENT NUMBER" ,e1);
		} 
	}
	
	static Subject newSubject(String subjectId){
		return StringUtils.isBlank(subjectId) || subjectId.trim().equalsIgnoreCase("NONE") ? Subject.NONE 
				: new Subject(subjectId);
	}
	
	Section withConn(Function<Connection, Section> function) {
		 try (Connection conn = dataSource.getConnection()) {
			 return function.apply(conn);
		 } catch(SQLException e) {
			 throw new DataAccessException("Problem retrieving connection from datasource " , e);
		 }
	 }

	Map<String, Object> findOne(Connection conn, String sql, String id) {
		Map<String, Object> map = new HashMap<>();
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, id);
			try(ResultSet rs = stmt.executeQuery()) {
				if(rs.next()) {
					map.put("subject_id", rs.getString("subject_id"));
					map.put("schedule", rs.getString("schedule"));
					map.put("faculty_number", 0);
					map.put("room_name", rs.getString("room_name"));
					map.put("capacity", rs.getInt("capacity"));
					map.put("version", rs.getInt("version"));
				}
			} 

		} catch (SQLException e) {
			throw new DataAccessException("Problem retrieving student data for student with student # " + id , e);
		}
		return map;
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
