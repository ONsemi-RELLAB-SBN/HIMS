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
            ps.setString(1, whInventoryHist.getRefId());
            ps.setString(2, whInventoryHist.getId());
            ps.setString(3, whInventoryHist.getMaterialPassNo());
            ps.setString(4, whInventoryHist.getInventoryDate());
            ps.setString(5, whInventoryHist.getInventoryLoc());
            ps.setString(6, whInventoryHist.getInventoryBy());
            ps.setString(7, whInventoryHist.getUpdatedBy());
            ps.setString(8, whInventoryHist.getStatus());
            ps.setString(9, whInventoryHist.getFlag());
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
                whInventory.setRefId(rs.getString("retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("material_pass_no"));
                whInventory.setStatus(rs.getString("status"));
                whInventory.setFlag(rs.getString("flag"));                
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
        String sql = "SELECT IL.*, RL.*, DATE_FORMAT(RL.material_pass_expiry,'%d %M %Y') AS mp_expiry_view , DATE_FORMAT(RL.requested_date,'%d %M %Y') AS requested_date_view, "
                   + "DATE_FORMAT(RL.date_verify,'%d %M %Y') AS date_verify_view, DATE_FORMAT(IL.inventory_date,'%d %M %Y') AS inventory_date_view "
                   + "FROM hms_wh_inventory_history IL, hms_wh_retrieval_list RL "
                   + "WHERE IL.retrieve_id = RL.ref_id AND IL.retrieve_id = '" + whInventoryId + "' ";
        WhInventoryHist whInventory = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventoryHist();
                whInventory.setRefId(rs.getString("IL.retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("IL.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whInventory.setEquipmentType(rs.getString("RL.equipment_type"));
                whInventory.setEquipmentId(rs.getString("RL.equipment_id"));
                whInventory.setType(rs.getString("RL.type"));
                whInventory.setQuantity(rs.getString("RL.quantity"));
                whInventory.setRemarks(rs.getString("RL.remarks"));
                whInventory.setRequestedBy(rs.getString("RL.requested_by"));
                whInventory.setRequestedDate(rs.getString("requested_date_view"));
                whInventory.setBarcodeVerify(rs.getString("RL.barcode_verify"));
                whInventory.setDateVerify(rs.getString("date_verify_view"));
                whInventory.setUserVerify(rs.getString("RL.user_verify"));
                whInventory.setInventoryDate(rs.getString("inventory_date_view"));
                whInventory.setInventoryLoc(rs.getString("IL.inventory_loc"));
                whInventory.setInventoryBy(rs.getString("IL.inventory_by"));
                whInventory.setStatus(rs.getString("IL.status"));
                whInventory.setFlag(rs.getString("IL.flag"));
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
    
    public WhInventoryHist getWhInventoryMergeWithRetrievePdf(String whInventoryId) {
        String sql = "SELECT IL.*, RL.* "
                   + "FROM hms_wh_inventory_history IL, hms_wh_retrieval_list RL "
                   + "WHERE IL.retrieve_id = RL.ref_id AND IL.retrieve_id = '" + whInventoryId + "' ";
        WhInventoryHist whInventory = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventoryHist();
                whInventory.setRefId(rs.getString("IL.retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("IL.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("RL.material_pass_expiry"));
                whInventory.setEquipmentType(rs.getString("RL.equipment_type"));
                whInventory.setEquipmentId(rs.getString("RL.equipment_id"));
                whInventory.setType(rs.getString("RL.type"));
                whInventory.setQuantity(rs.getString("RL.quantity"));
                whInventory.setRemarks(rs.getString("RL.remarks"));
                whInventory.setRequestedBy(rs.getString("RL.requested_by"));
                whInventory.setRequestedEmail(rs.getString("RL.requested_email"));
                whInventory.setRequestedDate(rs.getString("RL.requested_date"));
                whInventory.setBarcodeVerify(rs.getString("RL.barcode_verify"));
                whInventory.setDateVerify(rs.getString("RL.date_verify"));
                whInventory.setUserVerify(rs.getString("RL.user_verify"));
                whInventory.setInventoryDate(rs.getString("IL.inventory_date"));
                whInventory.setInventoryLoc(rs.getString("IL.inventory_loc"));
                whInventory.setInventoryBy(rs.getString("IL.inventory_by"));
                whInventory.setStatus(rs.getString("IL.status"));
                whInventory.setFlag(rs.getString("IL.flag"));
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

    public List<WhInventoryHist> getWhInventoryListMergeRetrieve() {
        String sql = "SELECT IL.*, RL.*, DATE_FORMAT(RL.material_pass_expiry,'%d %M %Y') AS mp_expiry_view , DATE_FORMAT(RL.requested_date,'%d %M %Y') AS requested_date_view, "
                   + "DATE_FORMAT(RL.date_verify,'%d %M %Y') AS date_verify_view, DATE_FORMAT(IL.inventory_date,'%d %M %Y') AS inventory_date_view "
                   + "FROM hms_wh_inventory_history IL, hms_wh_retrieval_list RL "
                   + "WHERE IL.retrieve_id = RL.ref_id ";
        List<WhInventoryHist> whInventoryList = new ArrayList<WhInventoryHist>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventoryHist whInventory;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventoryHist();
                whInventory.setRefId(rs.getString("IL.retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("IL.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whInventory.setEquipmentType(rs.getString("RL.equipment_type"));
                whInventory.setEquipmentId(rs.getString("RL.equipment_id"));
                whInventory.setType(rs.getString("RL.type"));
                whInventory.setQuantity(rs.getString("RL.quantity"));
                whInventory.setRemarks(rs.getString("RL.remarks"));
                whInventory.setRequestedBy(rs.getString("RL.requested_by"));
                whInventory.setRequestedDate(rs.getString("requested_date_view"));
                whInventory.setBarcodeVerify(rs.getString("RL.barcode_verify"));
                whInventory.setDateVerify(rs.getString("date_verify_view"));
                whInventory.setUserVerify(rs.getString("RL.user_verify"));
                whInventory.setInventoryDate(rs.getString("inventory_date_view"));
                whInventory.setInventoryLoc(rs.getString("IL.inventory_loc"));
                whInventory.setInventoryBy(rs.getString("IL.inventory_by"));
                whInventory.setStatus(rs.getString("IL.status"));
                whInventory.setFlag(rs.getString("IL.flag"));
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
                "SELECT COUNT(*) AS count FROM hms_wh_inventory_history WHERE retrieve_id = '" + id + "' "
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