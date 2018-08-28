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
import com.orangeandbronze.enlistment.dao.RoomDAO;
import com.orangeandbronze.enlistment.domain.Room;
import com.orangeandbronze.enlistment.domain.Schedule;
import com.orangeandbronze.enlistment.domain.Section;

public class RoomDaoJdbc implements RoomDAO{
	private final DataSource dataSource;
	
	public RoomDaoJdbc(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	@Override
	public Collection<Room> findAll() {
		try(Connection conn = dataSource.getConnection()){
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rooms");
			boolean found = false;
	        Collection<Room> rooms = new ArrayList<>();
	        try (ResultSet rs = stmt.executeQuery()) {
		        while (rs.next()) {
		        	if (!found) {
	                    found = true;
	                }
		        	
	                if (!StringUtils.isBlank(rs.getString("room_name"))) {
	                    rooms.add(
	                		new Room(rs.getString("room_name"), rs.getInt("capacity")) 
	            		);                    
	                }
	            }
		        
		        return found ? new ArrayList<Room>(rooms): Collections.EMPTY_LIST;
	        }
		} catch (SQLException e) {
			throw new DataAccessException();
		}
	}

	@Override
	public Room findBy(String roomName) {
		try(Connection conn = dataSource.getConnection()){
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rooms WHERE room_name = ?");
			stmt.setString(1, roomName);
			boolean found = false;
	        Room room = Room.TBA;
	        try (ResultSet rs = stmt.executeQuery()) {
		        while (rs.next()) {
		        	if (!found) {
	                    found = true;
	                }
		        	
	                if (!StringUtils.isBlank(rs.getString("room_name"))) {
	                    room = new Room(rs.getString("room_name"), rs.getInt("capacity"));
	                }
	            }
		        
		        return found ? room: Room.TBA;
	        }
		} catch (SQLException e) {
			throw new DataAccessException();
		}
	}

	@Override
	public Collection<String> findAllIds() {
		// TODO Auto-generated method stub
		return null;
	}

}
