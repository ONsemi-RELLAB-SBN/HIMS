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
import com.onsemi.hms.model.WhInventoryHist;
import com.onsemi.hms.tools.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WhInventoryHistDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhInventoryHistDAO.class);
    private final Connection conn;
    private final DataSource dataSource;

    public WhInventoryHistDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
        this.dataSource = db.getDataSource();
    }

    public QueryResult insertWhInventoryHist(WhInventoryHist whInventoryHist) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO hms_wh_inventory_history (retrieve_id, inventory_id, material_pass_no, inventory_date, inventory_loc, inventory_by, updated_date, updated_by, status, flag) "
              + "VALUES (?,?,?,?,?,?,NOW(),?,?,?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, whInventoryHist.getRetrieveId());
            ps.setString(2, whInventoryHist.getInventoryId());
            ps.setString(3, whInventoryHist.getMaterialPassNo());
            ps.setString(4, whInventoryHist.getInventoryDate());
            ps.setString(5, whInventoryHist.getInventoryLoc());
            ps.setString(6, whInventoryHist.getInventoryBy());
            ps.setString(7, whInventoryHist.getInventoryUpdatedBy());
            ps.setString(8, whInventoryHist.getInventoryStatus());
            ps.setString(9, whInventoryHist.getInventoryFlag());
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
    
    public WhInventoryHist getWhInventory(String whInventoryId) {
        String sql = "SELECT * "
                   + "FROM hms_wh_inventory_history "
                   + "WHERE retrieve_id = '" + whInventoryId + "'";
        WhInventoryHist whInventory = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventoryHist();
                whInventory.setRetrieveId(rs.getString("retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("material_pass_no"));
                whInventory.setInventoryStatus(rs.getString("status"));
                whInventory.setInventoryFlag(rs.getString("flag"));                
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
        return whInventory;
    }
        
    public WhInventoryHist getWhInventoryMergeWithRetrieve(String whInventoryId) {
        String sql = "SELECT IH.*, RH.*, DATE_FORMAT(RH.material_pass_expiry,'%d %M %Y') AS mp_expiry_view , DATE_FORMAT(RH.requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                   + "DATE_FORMAT(RH.date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(IH.inventory_date,'%d %M %Y %h:%i %p') AS inventory_date_view, "
                   + "DATE_FORMAT(RH.updated_date,'%d %M %Y %h:%i %p') AS r_updated_date_view, DATE_FORMAT(IH.updated_date,'%d %M %Y %h:%i %p') AS i_updated_date_view, "
                   + "DATE_FORMAT(RH.shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view "
                   + "FROM hms_wh_inventory_history IH, hms_wh_retrieval_history RH "
                   + "WHERE IH.retrieve_id = RH.retrieve_id AND IH.retrieve_id = '" + whInventoryId + "' ";
        WhInventoryHist whInventory = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventoryHist();
                whInventory.setRetrieveId(rs.getString("IH.retrieve_id"));
                whInventory.setInventoryId(rs.getString("IH.inventory_id"));
                whInventory.setMaterialPassNo(rs.getString("IH.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whInventory.setEquipmentType(rs.getString("RH.equipment_type"));
                whInventory.setEquipmentId(rs.getString("RH.equipment_id"));
                whInventory.setQuantity(rs.getString("RH.quantity"));
                whInventory.setRequestedBy(rs.getString("RH.requested_by"));
                whInventory.setRequestedEmail(rs.getString("RH.requested_email"));
                whInventory.setRequestedDate(rs.getString("requested_date_view"));
                whInventory.setShippingDate(rs.getString("shipping_date_view"));
                whInventory.setRemarks(rs.getString("RH.remarks"));
                whInventory.setReceivedDate(rs.getString("RH.received_date"));
                whInventory.setBarcodeVerify(rs.getString("RH.barcode_verify"));
                whInventory.setUserVerify(rs.getString("RH.user_verify"));
                whInventory.setDateVerify(rs.getString("date_verify_view"));
                whInventory.setRetrieveStatus(rs.getString("RH.status"));
                whInventory.setRetrieveFlag(rs.getString("RH.flag"));
                whInventory.setRetrieveUpdatedDate("r_updated_date_view");
                whInventory.setRetrieveUpdatedBy(rs.getString("RH.updated_by"));
                whInventory.setInventoryDate(rs.getString("inventory_date_view"));
                whInventory.setInventoryLoc(rs.getString("IH.inventory_loc"));
                whInventory.setInventoryBy(rs.getString("IH.inventory_by"));
                whInventory.setInventoryStatus(rs.getString("IH.status"));
                whInventory.setInventoryFlag(rs.getString("IH.flag"));
                whInventory.setInventoryUpdatedDate("i_updated_date_view");
                whInventory.setInventoryUpdatedBy(rs.getString("IH.updated_by"));
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
        return whInventory;
    }
    
    public List<WhInventoryHist> getWhInventoryListMergeRetrieve(String whInventoryId) {
        String sql = "SELECT IH.*, RH.*, DATE_FORMAT(RH.material_pass_expiry,'%d %M %Y') AS mp_expiry_view , DATE_FORMAT(RH.requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                   + "DATE_FORMAT(RH.date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(IH.inventory_date,'%d %M %Y %h:%i %p') AS inventory_date_view, "
                   + "DATE_FORMAT(RH.updated_date,'%d %M %Y %h:%i %p') AS r_updated_date_view, DATE_FORMAT(IH.updated_date,'%d %M %Y %h:%i %p') AS i_updated_date_view, "
                   + "DATE_FORMAT(RH.shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view "
                   + "FROM hms_wh_inventory_history IH, hms_wh_retrieval_history RH "
                   + "WHERE IH.retrieve_id = RH.retrieve_id AND IH.retrieve_id = '" + whInventoryId + "' ";
        List<WhInventoryHist> whInventoryList = new ArrayList<WhInventoryHist>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventoryHist whInventory;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventoryHist();
                whInventory.setRetrieveId(rs.getString("IH.retrieve_id"));
                whInventory.setInventoryId(rs.getString("IH.inventory_id"));
                whInventory.setMaterialPassNo(rs.getString("RH.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whInventory.setEquipmentType(rs.getString("RH.equipment_type"));
                whInventory.setEquipmentId(rs.getString("RH.equipment_id"));
                whInventory.setQuantity(rs.getString("RH.quantity"));
                whInventory.setRequestedBy(rs.getString("RH.requested_by"));
                whInventory.setRequestedEmail(rs.getString("RH.requested_email"));
                whInventory.setRequestedDate(rs.getString("requested_date_view"));
                whInventory.setShippingDate(rs.getString("shipping_date_view"));
                whInventory.setRemarks(rs.getString("RH.remarks"));
                whInventory.setReceivedDate(rs.getString("RH.received_date"));
                whInventory.setBarcodeVerify(rs.getString("RH.barcode_verify"));
                whInventory.setUserVerify(rs.getString("RH.user_verify"));
                whInventory.setDateVerify(rs.getString("date_verify_view"));
                whInventory.setRetrieveStatus(rs.getString("RH.status"));
                whInventory.setRetrieveFlag(rs.getString("RH.flag"));
                whInventory.setRetrieveUpdatedDate("r_updated_date_view");
                whInventory.setRetrieveUpdatedBy(rs.getString("RH.updated_by"));
                whInventory.setInventoryDate(rs.getString("inventory_date_view"));
                whInventory.setInventoryLoc(rs.getString("IH.inventory_loc"));
                whInventory.setInventoryBy(rs.getString("IH.inventory_by"));
                whInventory.setInventoryStatus(rs.getString("IH.status"));
                whInventory.setInventoryFlag(rs.getString("IH.flag"));
                whInventory.setInventoryUpdatedDate("i_updated_date_view");
                whInventory.setInventoryUpdatedBy(rs.getString("IH.updated_by"));
                whInventoryList.add(whInventory);
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
        return whInventoryList;
    }
    
    public Integer getCountExistingData(String id) {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) AS count FROM hms_wh_inventory_history WHERE inventory_id = '" + id + "' "
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