package com.orangeandbronze.enlistment.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.orangeandbronze.enlistment.dao.AdminDAO;
import com.orangeandbronze.enlistment.dao.DataAccessException;

public class AdminDaoJdbc implements AdminDAO{
	
	private final DataSource dataSource;
	
	public AdminDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }
	
	
	@Override
	public Map<String, String> findUserInfobById(int id) {
		Map<String, String> map = new HashMap<>();
		String sql = "SELECT * FROM admins WHERE id = ?";
		try(Connection conn = dataSource.getConnection()){
		    PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			try(ResultSet rs = stmt.executeQuery()) {
				if(rs.next()) {
					map.put("id", rs.getString("id"));
					map.put("lastname", rs.getString("lastname"));
					map.put("firstname", rs.getString("firstname"));
				}
			} 
		}   catch (SQLException e) {
			throw new DataAccessException("Problem retrieving admin data for student with student # " + id , e);
		}
		return map;
	}

	@Override
	public Map<String, String> findAdminInfoBy(int adminId) {
		Map<String, String> map = new HashMap<>();
		String sql = "SELECT * FROM admins WHERE id = ?";
		try(Connection conn = dataSource.getConnection()){
		    PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, adminId);
			try(ResultSet rs = stmt.executeQuery()) {
				if(rs.next()) {
					map.put("id", rs.getString("id"));
					map.put("lastname", rs.getString("lastname"));
					map.put("firstname", rs.getString("firstname"));
				}
			} 
		}   catch (SQLException e) {
			throw new DataAccessException("Problem retrieving admin data for ADMIN with ADMIN # " + adminId , e);
		}
		return map;
	}

}
