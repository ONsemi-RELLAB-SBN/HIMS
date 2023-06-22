package com.onsemi.hms.db;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class DB extends SpringBeanAutowiringSupport {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DB.class);

    @Autowired
    private DataSource dataSource;

    public Connection getConnection() {
        Connection conn;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return conn;
    }
    
    public DataSource getDataSource() {
        return dataSource;
    }
}
