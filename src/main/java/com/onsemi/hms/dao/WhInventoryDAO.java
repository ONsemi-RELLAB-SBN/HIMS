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
import com.onsemi.hms.model.WhInventory;
import com.onsemi.hms.tools.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WhInventoryDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhInventoryDAO.class);
    private final Connection conn;
    private final DataSource dataSource;

    public WhInventoryDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
        this.dataSource = db.getDataSource();
    }

    public QueryResult insertWhInventory(WhInventory whInventory) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO hms_wh_inventory_list (retrieve_id, material_pass_no, inventory_date, inventory_rack, inventory_shelf, inventory_by, status, flag) "
              + "VALUES (?,?,NOW(),?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, whInventory.getRefId());
            ps.setString(2, whInventory.getMaterialPassNo());
            ps.setString(3, whInventory.getInventoryRack());
            ps.setString(4, whInventory.getInventoryShelf());
            ps.setString(5, whInventory.getInventoryBy());
            ps.setString(6, whInventory.getStatus());
            ps.setString(7, whInventory.getFlag());
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

    public QueryResult updateWhInventory(WhInventory whInventory) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_inventory_list "
                   + "SET inventory_date = NOW(), inventory_rack = ?, inventory_shelf = ?, inventory_by = ? "
                   + "WHERE retrieve_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whInventory.getInventoryRack());
            ps.setString(2, whInventory.getInventoryShelf());
            ps.setString(3, whInventory.getInventoryBy());
            ps.setString(4, whInventory.getRefId());
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

    public QueryResult deleteWhInventory(String whInventoryId) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM hms_wh_inventory_list WHERE retrieve_id = '" + whInventoryId + "'"
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

    public WhInventory getWhInventory(String whInventoryId) {
        String sql = "SELECT * "
                   + "FROM hms_wh_inventory_list "
                   + "WHERE retrieve_id = '" + whInventoryId + "'";
        WhInventory whInventory = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventory();
                whInventory.setId(rs.getString("id"));
                whInventory.setRefId(rs.getString("retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("material_pass_no"));
                whInventory.setInventoryDate(rs.getString("inventory_date"));
                whInventory.setInventoryRack(rs.getString("inventory_rack"));
                whInventory.setInventoryShelf(rs.getString("inventory_shelf"));
                whInventory.setInventoryBy(rs.getString("inventory_by"));
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
        
    public WhInventory getWhInventoryMergeWithRetrievePdf(String whInventoryId) {
        String sql = "SELECT IL.*, RL.*, DATE_FORMAT(RL.material_pass_expiry,'%d %M %Y') AS mp_expiry_view , DATE_FORMAT(RL.requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                   + "DATE_FORMAT(RL.date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(IL.inventory_date,'%d %M %Y %h:%i %p') AS inventory_date_view "
                   + "FROM hms_wh_inventory_list IL, hms_wh_retrieval_list RL "
                   + "WHERE IL.retrieve_id = RL.retrieve_id AND IL.retrieve_id = '" + whInventoryId + "' ";
        WhInventory whInventory = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventory();
                whInventory.setRefId(rs.getString("IL.retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("IL.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whInventory.setEquipmentType(rs.getString("RL.equipment_type"));
                whInventory.setEquipmentId(rs.getString("RL.equipment_id"));
                whInventory.setPcbA(rs.getString("RL.pcb_a"));
                whInventory.setQtyQualA(rs.getString("RL.qty_qual_a"));
                whInventory.setPcbB(rs.getString("RL.pcb_b"));
                whInventory.setQtyQualB(rs.getString("RL.qty_qual_b"));
                whInventory.setPcbC(rs.getString("RL.pcb_c"));
                whInventory.setQtyQualC(rs.getString("RL.qty_qual_c"));
                whInventory.setPcbControl(rs.getString("RL.pcb_control"));
                whInventory.setQtyControl(rs.getString("RL.qty_control"));
                whInventory.setQuantity(rs.getString("RL.quantity"));
                whInventory.setRemarks(rs.getString("RL.remarks"));
                whInventory.setRequestedBy(rs.getString("RL.requested_by"));
                whInventory.setRequestedDate(rs.getString("requested_date_view"));
                whInventory.setBarcodeVerify(rs.getString("RL.barcode_verify"));
                whInventory.setDateVerify(rs.getString("date_verify_view"));
                whInventory.setUserVerify(rs.getString("RL.user_verify"));
                whInventory.setInventoryDate(rs.getString("inventory_date_view"));
                whInventory.setInventoryRack(rs.getString("IL.inventory_rack"));
                whInventory.setInventoryShelf(rs.getString("IL.inventory_shelf"));
                whInventory.setInventoryBy(rs.getString("IL.inventory_by"));
                whInventory.setStatus(rs.getString("RL.status"));
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
    
    public WhInventory getWhInventoryMergeWithRetrieve(String whInventoryId) {
        String sql = "SELECT IL.*, RL.* "
                   + "FROM hms_wh_inventory_list IL, hms_wh_retrieval_list RL  "
                   + "WHERE IL.retrieve_id = RL.retrieve_id AND IL.retrieve_id = '" + whInventoryId + "' ";
        WhInventory whInventory = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventory();
                whInventory.setRefId(rs.getString("retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("IL.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("material_pass_expiry"));
                whInventory.setEquipmentType(rs.getString("equipment_type"));
                whInventory.setEquipmentId(rs.getString("equipment_id"));
                whInventory.setPcbA(rs.getString("RL.pcb_a"));
                whInventory.setQtyQualA(rs.getString("RL.qty_qual_a"));
                whInventory.setPcbB(rs.getString("RL.pcb_b"));
                whInventory.setQtyQualB(rs.getString("RL.qty_qual_b"));
                whInventory.setPcbC(rs.getString("RL.pcb_c"));
                whInventory.setQtyQualC(rs.getString("RL.qty_qual_c"));
                whInventory.setPcbControl(rs.getString("RL.pcb_control"));
                whInventory.setQtyControl(rs.getString("RL.qty_control"));
                whInventory.setQuantity(rs.getString("quantity"));
                whInventory.setRemarks(rs.getString("remarks"));
                whInventory.setRequestedBy(rs.getString("requested_by"));
                whInventory.setRequestedEmail(rs.getString("requested_email"));
                whInventory.setRequestedDate(rs.getString("requested_date"));
                whInventory.setBarcodeVerify(rs.getString("barcode_verify"));
                whInventory.setDateVerify(rs.getString("date_verify"));
                whInventory.setUserVerify(rs.getString("user_verify"));
                whInventory.setInventoryDate(rs.getString("inventory_date"));
                whInventory.setInventoryRack(rs.getString("inventory_rack"));
                whInventory.setInventoryShelf(rs.getString("inventory_shelf"));
                whInventory.setInventoryBy(rs.getString("inventory_by"));
                whInventory.setStatus(rs.getString("RL.status"));
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

    public List<WhInventory> getWhInventoryListMergeRetrieve() {
        String sql = "SELECT IL.*, RL.*, DATE_FORMAT(RL.material_pass_expiry,'%d %M %Y') AS mp_expiry_view , DATE_FORMAT(RL.requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                   + "DATE_FORMAT(RL.date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(IL.inventory_date,'%d %M %Y %h:%i %p') AS inventory_date_view "
                   + "FROM hms_wh_inventory_list IL, hms_wh_retrieval_list RL "
                   + "WHERE IL.retrieve_id = RL.retrieve_id ";
        List<WhInventory> whInventoryList = new ArrayList<WhInventory>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventory whInventory;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventory();
                whInventory.setRefId(rs.getString("IL.retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("IL.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whInventory.setEquipmentType(rs.getString("RL.equipment_type"));
                whInventory.setEquipmentId(rs.getString("RL.equipment_id"));
                whInventory.setPcbA(rs.getString("RL.pcb_a"));
                whInventory.setQtyQualA(rs.getString("RL.qty_qual_a"));
                whInventory.setPcbB(rs.getString("RL.pcb_b"));
                whInventory.setQtyQualB(rs.getString("RL.qty_qual_b"));
                whInventory.setPcbC(rs.getString("RL.pcb_c"));
                whInventory.setQtyQualC(rs.getString("RL.qty_qual_c"));
                whInventory.setPcbControl(rs.getString("RL.pcb_control"));
                whInventory.setQtyControl(rs.getString("RL.qty_control"));
                whInventory.setQuantity(rs.getString("RL.quantity"));
                whInventory.setRemarks(rs.getString("RL.remarks"));
                whInventory.setRequestedBy(rs.getString("RL.requested_by"));
                whInventory.setRequestedDate(rs.getString("requested_date_view"));
                whInventory.setBarcodeVerify(rs.getString("RL.barcode_verify"));
                whInventory.setDateVerify(rs.getString("date_verify_view"));
                whInventory.setUserVerify(rs.getString("RL.user_verify"));
                whInventory.setInventoryDate(rs.getString("inventory_date_view"));
                whInventory.setInventoryRack(rs.getString("inventory_rack"));
                whInventory.setInventoryShelf(rs.getString("inventory_shelf"));
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
                "SELECT COUNT(*) AS count FROM hms_wh_inventory_list WHERE retrieve_id = '" + id + "' "
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
    
    public List<WhInventory> getWhInventoryMpExpiryList() {
        String sql = "SELECT RL.*, IL.* "
                   + "FROM hms_wh_retrieval_list RL, hms_wh_inventory_list IL "
                   + "WHERE DATE(RL.material_pass_expiry) >= DATE(NOW()) "
                   + "AND DATE(RL.material_pass_expiry) <= ADDDATE(DATE(NOW()),30) "
                   + "AND RL.retrieve_id = IL.retrieve_id "
                   + "ORDER BY RL.material_pass_expiry ASC ";
        List<WhInventory> whInventoryList = new ArrayList<WhInventory>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventory whInventory;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventory();
                whInventory.setRefId(rs.getString("IL.retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("IL.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("RL.material_pass_expiry"));
                whInventory.setEquipmentType(rs.getString("RL.equipment_type"));
                whInventory.setEquipmentId(rs.getString("RL.equipment_id"));
                whInventory.setPcbA(rs.getString("RL.pcb_a"));
                whInventory.setQtyQualA(rs.getString("RL.qty_qual_a"));
                whInventory.setPcbB(rs.getString("RL.pcb_b"));
                whInventory.setQtyQualB(rs.getString("RL.qty_qual_b"));
                whInventory.setPcbC(rs.getString("RL.pcb_c"));
                whInventory.setQtyQualC(rs.getString("RL.qty_qual_c"));
                whInventory.setPcbControl(rs.getString("RL.pcb_control"));
                whInventory.setQtyControl(rs.getString("RL.qty_control"));
                whInventory.setQuantity(rs.getString("RL.quantity"));
                whInventory.setRequestedBy(rs.getString("RL.requested_by"));
                whInventory.setRequestedDate(rs.getString("RL.requested_date"));
                whInventory.setShippingDate(rs.getString("RL.shipping_date"));
                whInventory.setRemarks(rs.getString("RL.remarks"));
                whInventory.setReceivedDate(rs.getString("RL.received_date"));
                whInventory.setBarcodeVerify(rs.getString("RL.barcode_verify"));
                whInventory.setDateVerify(rs.getString("RL.date_verify"));
                whInventory.setUserVerify(rs.getString("RL.user_verify"));
                whInventory.setInventoryDate(rs.getString("IL.inventory_date"));
                whInventory.setInventoryRack(rs.getString("inventory_rack"));
                whInventory.setInventoryShelf(rs.getString("inventory_shelf"));
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
    
    public Integer getCountMpExpiry() {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(RL.material_pass_expiry) AS count " +
                "FROM hms_wh_retrieval_list RL, hms_wh_inventory_list IL " +
                "WHERE DATE(RL.material_pass_expiry) >= DATE(NOW()) " +
                "AND DATE(RL.material_pass_expiry) <= ADDDATE(DATE(NOW()),30) " +
                "AND RL.retrieve_id = IL.retrieve_id "
            );

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("count");
            }
            LOGGER.info("count MP expiry..........." + count.toString());
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
    
    public List<WhInventory> getWhInventoryMpExpiryAlertList() {
        String sql = "SELECT RL.*, IL.* "
                   + "FROM hms_wh_retrieval_list RL, hms_wh_inventory_list IL "
                   + "WHERE DATE(RL.material_pass_expiry) >= DATE(NOW()) "
                   + "AND DATE(RL.material_pass_expiry) <= ADDDATE(DATE(NOW()),3) "
                   + "AND RL.retrieve_id = IL.retrieve_id "
                   + "ORDER BY RL.material_pass_expiry ASC ";
        List<WhInventory> whInventoryList = new ArrayList<WhInventory>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventory whInventory;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventory();
                whInventory.setRefId(rs.getString("IL.retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("IL.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("RL.material_pass_expiry"));
                whInventory.setEquipmentType(rs.getString("RL.equipment_type"));
                whInventory.setEquipmentId(rs.getString("RL.equipment_id"));
                whInventory.setPcbA(rs.getString("RL.pcb_a"));
                whInventory.setQtyQualA(rs.getString("RL.qty_qual_a"));
                whInventory.setPcbB(rs.getString("RL.pcb_b"));
                whInventory.setQtyQualB(rs.getString("RL.qty_qual_b"));
                whInventory.setPcbC(rs.getString("RL.pcb_c"));
                whInventory.setQtyQualC(rs.getString("RL.qty_qual_c"));
                whInventory.setPcbControl(rs.getString("RL.pcb_control"));
                whInventory.setQtyControl(rs.getString("RL.qty_control"));
                whInventory.setQuantity(rs.getString("RL.quantity"));
                whInventory.setRequestedBy(rs.getString("RL.requested_by"));
                whInventory.setRequestedDate(rs.getString("RL.requested_date"));
                whInventory.setShippingDate(rs.getString("RL.shipping_date"));
                whInventory.setRemarks(rs.getString("RL.remarks"));
                whInventory.setReceivedDate(rs.getString("RL.received_date"));
                whInventory.setBarcodeVerify(rs.getString("RL.barcode_verify"));
                whInventory.setDateVerify(rs.getString("RL.date_verify"));
                whInventory.setUserVerify(rs.getString("RL.user_verify"));
                whInventory.setInventoryDate(rs.getString("IL.inventory_date"));
                whInventory.setInventoryRack(rs.getString("inventory_rack"));
                whInventory.setInventoryShelf(rs.getString("inventory_shelf"));
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
    
    public Integer getCountMpExpiryAlert() {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(RL.material_pass_expiry) AS count " +
                "FROM hms_wh_retrieval_list RL, hms_wh_inventory_list IL " +
                "WHERE DATE(RL.material_pass_expiry) >= DATE(NOW()) " +
                "AND DATE(RL.material_pass_expiry) <= ADDDATE(DATE(NOW()),3) " +
                "AND RL.retrieve_id = IL.retrieve_id "
            );

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("count");
            }
            LOGGER.info("count MP expiry..........." + count.toString());
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