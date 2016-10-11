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
import com.onsemi.hms.model.InventoryDetails;
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

    public QueryResult insertInventoryDetails(InventoryDetails inventoryDetails) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hms_inventory_management (hardware_type, rack_type, loc_code, rack_code, label_key, created_by, created_date) VALUES (?,?,?,?,?,?,NOW())", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, inventoryDetails.getHardwareType());
            ps.setString(2, inventoryDetails.getRackType());
            ps.setString(3, inventoryDetails.getLocCode());
            ps.setString(4, inventoryDetails.getRackCode());
            ps.setString(5, inventoryDetails.getLabelKey());
            ps.setString(6, inventoryDetails.getCreatedBy());
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

    public QueryResult updateInventoryDetails(InventoryDetails inventoryDetails) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE hms_inventory_management " +
                    "SET loc_code = ?, rack_code = ?, label_key = ?, modified_by = ?, modified_date = NOW() " +
                    "WHERE hardware_type = ? AND rack_type = ? "
            );
            ps.setString(1, inventoryDetails.getLocCode());
            ps.setString(2, inventoryDetails.getRackCode());
            ps.setString(3, inventoryDetails.getLabelKey());
            ps.setString(4, inventoryDetails.getModifiedBy());
            ps.setString(5, inventoryDetails.getHardwareType());
            ps.setString(6, inventoryDetails.getRackType());
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

    public QueryResult deleteInventoryDetails(String inventoryDetailsId) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM hms_inventory_management WHERE id = '" + inventoryDetailsId + "'"
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

    public InventoryDetails getInventoryDetails(String labelKey) {
        String sql = "SELECT * "
                   + "FROM hms_inventory_management "
                   + "WHERE label_key = '" + labelKey + "' ";
        InventoryDetails inventoryDetails = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                inventoryDetails = new InventoryDetails();
                inventoryDetails.setId(rs.getString("id"));
                inventoryDetails.setHardwareType(rs.getString("hardware_type"));
                inventoryDetails.setRackType(rs.getString("rack_type"));
                inventoryDetails.setLocCode(rs.getString("loc_code"));
                inventoryDetails.setRackCode(rs.getString("rack_code"));
                inventoryDetails.setLabelKey(rs.getString("label_key"));
                inventoryDetails.setCreatedBy(rs.getString("created_by"));
                inventoryDetails.setCreatedDate(rs.getString("created_date"));
                inventoryDetails.setModifiedBy(rs.getString("modified_by"));
                inventoryDetails.setModifiedDate(rs.getString("modified_date"));
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
        return inventoryDetails;
    }

    public List<InventoryDetails> getInventoryDetailsList() {
        String sql = "SELECT * FROM hms_inventory_management ORDER BY id ASC";
        List<InventoryDetails> inventoryDetailsList = new ArrayList<InventoryDetails>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            InventoryDetails inventoryDetails;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                inventoryDetails = new InventoryDetails();
                inventoryDetails.setId(rs.getString("id"));
                inventoryDetails.setHardwareType(rs.getString("hardware_type"));
                inventoryDetails.setRackType(rs.getString("rack_type"));
                inventoryDetails.setLocCode(rs.getString("loc_code"));
                inventoryDetails.setRackCode(rs.getString("rack_code"));
                inventoryDetails.setLabelKey(rs.getString("label_key"));
                inventoryDetails.setCreatedBy(rs.getString("created_by"));
                inventoryDetails.setCreatedDate(rs.getString("created_date"));
                inventoryDetails.setModifiedBy(rs.getString("modified_by"));
                inventoryDetails.setModifiedDate(rs.getString("modified_date"));
                inventoryDetailsList.add(inventoryDetails);
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
        return inventoryDetailsList;
    }
}