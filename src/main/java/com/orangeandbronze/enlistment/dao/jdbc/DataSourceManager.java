package com.orangeandbronze.enlistment.dao.jdbc;

import java.io.*;
import java.sql.Connection;
import java.util.*;
import javax.sql.*;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.postgresql.ds.*;

public class DataSourceManager {

	private static DataSource dataSource;

	public static DataSource getDataSource() {
	    if (dataSource == null) {
	        Properties prop = new Properties();
	        String propFileName = "pg.datasource.properties";
	        try (Reader reader = new BufferedReader(
	                new InputStreamReader(
	                        DataSourceManager.class.getClassLoader()
	                        .getResourceAsStream(propFileName)))) {
	            prop.load(reader);
	        } catch (IOException e) {
	            throw new RuntimeException("problem reading file "
	                    + propFileName, e);
	        }
	        PGSimpleDataSource ds = new PGSimpleDataSource();
	        ds.setServerName(prop.getProperty("servername"));
	        ds.setDatabaseName(prop.getProperty("database"));
	        ds.setUser(prop.getProperty("user"));
	        ds.setPassword(prop.getProperty("password"));
	        dataSource = ds;
	    }
	    return dataSource;
	}
	
	public static DataSource defaultDataSource(Class getclass) throws Exception {
		DataSource ds = DataSourceManager.getDataSource();
	    Connection jdbcConnection = ds.getConnection();
	    jdbcConnection.createStatement().execute("SET CONSTRAINTS ALL DEFERRED;");
	    
	    IDatabaseConnection dbUnitConnection =
	            new DatabaseConnection(jdbcConnection);
	    
	    FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
	    builder.setDtdMetadata(false);
	    
	    IDataSet dataSet = builder.build(getclass.getClassLoader()
	            .getResourceAsStream("DefaultDataset.xml"));
	    try {
	        DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataSet);
	    } finally {
	        dbUnitConnection.close(); // don't forget to close the connection!
	    }
	    return ds;
	}
	
}
