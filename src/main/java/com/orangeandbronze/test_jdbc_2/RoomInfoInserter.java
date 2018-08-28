package com.orangeandbronze.test_jdbc;

import java.sql.*;
import java.util.Arrays;
import java.util.Collection;

import javax.sql.*;

import org.postgresql.ds.PGSimpleDataSource;

import com.orangeandbronze.enlistment.domain.Room;

public class RoomInfoInserter {

	private final DataSource ds;

    RoomInfoInserter() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setDatabaseName("enlistment");
        ds.setUser("postgres");
        ds.setPassword("password");
        this.ds = ds;
    }  
	
    void insertNewRooms(Collection<Room> rooms) {
        try (Connection conn = ds.getConnection();
               PreparedStatement stmt = conn.prepareStatement(
                   "INSERT INTO rooms (room_name, capacity) VALUES (?, ?)")) {
            for (Room room : rooms) {
              stmt.setString(1, room.getName());
              stmt.setInt(2, room.getCapacity());
              stmt.execute();
            }
        } catch (SQLException e) {
          throw new RuntimeException(
            "problem while inserting rooms "+ rooms, e);
        }
      }

      public static void main(String[] args) {
        Collection rooms = Arrays.asList(new Room("MUSIC25", 30),
            new Room("CAL203", 35));
        new RoomInfoInserter().insertNewRooms(rooms);
      }
}
