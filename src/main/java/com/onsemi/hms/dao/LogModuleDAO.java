package com.onsemi.hms.dao;

import com.onsemi.hms.db.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import com.onsemi.hms.model.LogModule;
import com.onsemi.hms.tools.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogModuleDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogModuleDAO.class);
    private final Connection conn;
    private final DataSource dataSource;

    public LogModuleDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
        this.dataSource = db.getDataSource();
    }

    public QueryResult insertLog(LogModule log) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hms_wh_log (reference_id, module_id, module_name, status, timestamp) "
                  + "VALUES (?,?,?,?,NOW())", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, log.getReferenceId());
            ps.setString(2, log.getModuleId());
            ps.setString(3, log.getModuleName());
            ps.setString(4, log.getStatus());
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
    
    public QueryResult insertLogForVerification(LogModule log) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hms_wh_log (reference_id, module_id, module_name, status, timestamp, verified_by, verified_date) "
                  + "VALUES (?,?,?,?,NOW(),?,?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, log.getReferenceId());
            ps.setString(2, log.getModuleId());
            ps.setString(3, log.getModuleName());
            ps.setString(4, log.getStatus());
            ps.setString(5, log.getVerifiedBy());
            ps.setString(6, log.getVerifiedDate());
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
    
    public LogModule getLogModule(String referenceId) {
        String sql  = "SELECT * "
                    + "FROM hms_wh_log "
                    + "WHERE reference_id = '" + referenceId + "' ";
        LogModule log = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                log = new LogModule();
                log.setReferenceId(referenceId);
                log.setModuleId(rs.getString("module_id"));
                log.setModuleName(rs.getString("module_name"));
                log.setStatus(rs.getString("status"));
                log.setTimestamp(rs.getString("timestamp"));
                log.setVerifiedBy(rs.getString("verified_by"));
                log.setVerifiedDate(rs.getString("verified_date"));
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
        return log;
    }

    public List<LogModule> getLogModuleList(String referenceId) {
        String sql = "SELECT * "
                   + "FROM hms_wh_log "
                   + "WHERE reference_id = '" + referenceId + "' "
                   + "ORDER BY timestamp ASC ";
        List<LogModule> logModuleList = new ArrayList<LogModule>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            LogModule log;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                log = new LogModule();
                log.setReferenceId(referenceId);
                log.setModuleId(rs.getString("module_id"));
                log.setModuleName(rs.getString("module_name"));
                log.setStatus(rs.getString("status"));
                log.setTimestamp(rs.getString("timestamp"));
                log.setVerifiedBy(rs.getString("verified_by"));
                log.setVerifiedDate(rs.getString("verified_date"));
                logModuleList.add(log);
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
        return logModuleList;
    }
    
      public Integer getCountExistingData(String referenceId) {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) AS count "
              + "FROM hms_wh_log "
              + "WHERE reference_id = '" + referenceId + "' "
            );

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("count");
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
        return count;
    }
}