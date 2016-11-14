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
import com.onsemi.hms.model.WhInventoryMgt;
import com.onsemi.hms.tools.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryMgtDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryMgtDAO.class);
    private final Connection conn;
    private final DataSource dataSource;

    public InventoryMgtDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
        this.dataSource = db.getDataSource();
    }

    public QueryResult insertInventoryDetails(WhInventoryMgt whInventoryMgt) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hms_inventory_mgt (rack_id, shelf_id, hardware_id, modified_date, date_created) VALUES (?,?,?,NOW(),NOW())", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, whInventoryMgt.getRackId());
            ps.setString(2, whInventoryMgt.getShelfId());
            ps.setString(3, whInventoryMgt.getHardwareId());
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

    public QueryResult updateInventoryDetails(WhInventoryMgt whInventoryMgt) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE hms_inventory_mgt " +
                    "SET hardware_id = ?, modified_date = NOW() " +
                    "WHERE rack_id = ? AND shelf_id = ? "
            );
            ps.setString(1, whInventoryMgt.getHardwareId());
            ps.setString(2, whInventoryMgt.getModifiedDate());
            ps.setString(3, whInventoryMgt.getRackId());
            ps.setString(4, whInventoryMgt.getShelfId());
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

    public QueryResult deleteInventoryDetails(String shelfId) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM hms_inventory_mgt WHERE shelf_id = '" + shelfId + "'"
            );
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

    public WhInventoryMgt getInventoryDetails(String rackId) {
        String sql = "SELECT * "
                   + "FROM hms_inventory_mgt "
                   + "WHERE rack_id = '" + rackId + "' ";
        WhInventoryMgt whInventoryMgt = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventoryMgt = new WhInventoryMgt();
                whInventoryMgt.setId(rs.getString("id"));
                whInventoryMgt.setRackId(rs.getString("rack_id"));
                whInventoryMgt.setShelfId(rs.getString("shelf_id"));
                whInventoryMgt.setHardwareId(rs.getString("hardware_id"));
                whInventoryMgt.setDateCreated(rs.getString("date_created"));
                whInventoryMgt.setModifiedDate(rs.getString("modified_date"));
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
        return whInventoryMgt;
    }

    public List<WhInventoryMgt> getInventoryDetailsList(String query) {
        String sql = "SELECT * FROM hms_inventory_mgt " + query + " ORDER BY id ASC";
        List<WhInventoryMgt> whInventoryMgtList = new ArrayList<WhInventoryMgt>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventoryMgt whInventoryMgt;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventoryMgt = new WhInventoryMgt();
                whInventoryMgt.setId(rs.getString("id"));
                whInventoryMgt.setRackId(rs.getString("rack_id"));
                whInventoryMgt.setShelfId(rs.getString("shelf_id"));
                whInventoryMgt.setHardwareId(rs.getString("hardware_id"));
                whInventoryMgt.setDateCreated(rs.getString("date_created"));
                whInventoryMgt.setModifiedDate(rs.getString("modified_date"));
                whInventoryMgtList.add(whInventoryMgt);
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
        return whInventoryMgtList;
    }
    
    public Integer getCountExistingData(String shelfId) {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) AS count FROM hms_inventory_mgt WHERE rack_id = '" + shelfId + "' "
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