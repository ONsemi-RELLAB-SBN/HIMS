/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.onsemi.hms.dao;

import com.onsemi.hms.db.DB;
import com.onsemi.hms.model.RunningNumber;
import com.onsemi.hms.tools.QueryResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zbqb9x
 */
public class RunningNumberDAO {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RunningNumberDAO.class);
    private final Connection conn;
    private final DataSource dataSource;

    public RunningNumberDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
        this.dataSource = db.getDataSource();
    }
    
    public QueryResult insertRunningNumber(String year, String month, String runNo) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hms_running_number (year, month, running_number) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, year);
            ps.setString(2, month);
            ps.setString(3, runNo);
            queryResult.setResult(ps.executeUpdate());
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                queryResult.setGeneratedKey(Integer.toString(rs.getInt(1)));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            queryResult.setErrorMessage(e.getMessage());
            LOGGER.error(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        return queryResult;
    }
    
    public QueryResult updateRunningNumber(String runNo, String year, String month) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_running_number SET running_number = ? WHERE year = '" + year + "' AND month = '" + month + "'" ;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, runNo);
            queryResult.setResult(ps.executeUpdate());
            ps.close();
        } catch (SQLException e) {
            queryResult.setErrorMessage(e.getMessage());
            LOGGER.error(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        return queryResult;
    }
    
    public String getLatestRunning(String year, String month) {
        String value = "";
        String sql = "SELECT running_number AS running_number FROM hms_running_number WHERE year = '" + year + "' AND month = '" + month + "'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                value = rs.getString("running_number");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        return value;
    }
    
    public String getLatest(String year, String month) {
        String value = "";
        String sql = "SELECT * FROM hms_running_number WHERE year = '" + year + "' AND month = '" + month + "'";
        RunningNumber run = new RunningNumber();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                value = rs.getString("running_number");
                value = year + month + rs.getString("running_number");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        return value;
    }
    
}