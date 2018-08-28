package com.orangeandbronze.enlistment.domain;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.*;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

import com.orangeandbronze.enlistment.dao.RoomDAO;
import com.orangeandbronze.enlistment.dao.jdbc.DataSourceManager;
import com.orangeandbronze.enlistment.dao.jdbc.RoomDaoJdbc;

public class RoomDaoJdbcIT {

	@Test
	public void findByRoomName() throws Exception {
		DataSource ds = DataSourceManager.defaultDataSource(getClass());
		RoomDAO roomDao = new RoomDaoJdbc(ds);
		String roomName = "AVR1";
		
		Room expectedRoom = new Room("AVR1", 10, 0);
		Room actualRoom = roomDao.findBy(roomName);
		
		System.out.println(expectedRoom.getName() + " = " + actualRoom.getName());
		assertEquals(expectedRoom.getName(), actualRoom.getName());
	}
	
	@Test
	public void findAll() throws Exception {
		DataSource ds = DataSourceManager.defaultDataSource(getClass());
		RoomDAO roomDao = new RoomDaoJdbc(ds);
		
		Collection<Room> rooms = new ArrayList<>(roomDao.findAll());
		System.out.println("There are " + rooms.size() + " rooms in the database.");
		
		int expectedRowCount = 6;
		assertEquals(expectedRowCount, rooms.size());
	}
}
