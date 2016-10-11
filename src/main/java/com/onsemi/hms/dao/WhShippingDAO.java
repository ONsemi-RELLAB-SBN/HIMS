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
import com.onsemi.hms.model.WhShipping;
import com.onsemi.hms.model.WhShippingLog;
import com.onsemi.hms.tools.QueryResult;
import com.onsemi.hms.tools.SpmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WhShippingDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhShippingDAO.class);
    private final Connection conn;
    private final DataSource dataSource;

    public WhShippingDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
        this.dataSource = db.getDataSource();
    }

    public QueryResult insertWhShipping(WhShipping whShipping) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO hms_wh_shipping_list (request_id, material_pass_no, status, flag) "
              + "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, whShipping.getRequestId());
            ps.setString(2, whShipping.getMaterialPassNo());
            ps.setString(3, whShipping.getStatus());
            ps.setString(4, whShipping.getFlag());
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

    public QueryResult updateWhShipping(WhShipping whShipping) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_shipping_list "
                   + "SET shipping_date = ?, shipping_by = ?, status = ?, flag = ? "
                   + "WHERE material_pass_no = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whShipping.getShippingDate());
            ps.setString(2, whShipping.getShippingBy());
            ps.setString(3, whShipping.getStatus());
            ps.setString(4, whShipping.getFlag());
            ps.setString(5, whShipping.getMaterialPassNo());
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
    
    public QueryResult updateWhShippingStatus(WhShipping whShipping) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_shipping_list "
                   + "SET status = ? "
                   + "WHERE request_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whShipping.getStatus());
            ps.setString(2, whShipping.getRequestId());
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

    public QueryResult deleteWhShipping(String whShippingId) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM hms_wh_shipping_list WHERE request_id = '" + whShippingId + "'"
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

    public WhShipping getWhShipping(String whShippingId) {
        String sql = "SELECT * "
                   + "FROM hms_wh_shipping_list "
                   + "WHERE request_id = '" + whShippingId + "'";
        WhShipping whShipping = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whShipping = new WhShipping();
                whShipping.setId(rs.getString("id"));
                whShipping.setRequestId(rs.getString("request_id"));
                whShipping.setMaterialPassNo(rs.getString("material_pass_no"));
                whShipping.setShippingDate(rs.getString("shipping_date"));
                whShipping.setShippingBy(rs.getString("shipping_by"));
                whShipping.setStatus(rs.getString("status"));
                whShipping.setFlag(rs.getString("flag"));                
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
        return whShipping;
    }
        
    public WhShipping getWhShippingMergeWithRequest(String whShippingId) {
        String sql = "SELECT SL.*, RL.*, DATE_FORMAT(RL.material_pass_expiry,'%d %M %Y') AS mp_expiry_view , DATE_FORMAT(RL.requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                   + "DATE_FORMAT(RL.date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(SL.shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view "
                   + "FROM hms_wh_shipping_list SL, hms_wh_request_list RL "
                   + "WHERE SL.request_id = RL.request_id AND SL.request_id = '" + whShippingId + "' ";
        WhShipping whShipping = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whShipping = new WhShipping();
                whShipping.setId(rs.getString("id"));
                whShipping.setRequestId(rs.getString("SL.request_id"));
                whShipping.setMaterialPassNo(rs.getString("SL.material_pass_no"));
                whShipping.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whShipping.setEquipmentType(rs.getString("RL.equipment_type"));
                whShipping.setEquipmentId(rs.getString("RL.equipment_id"));
                whShipping.setQuantity(rs.getString("RL.quantity"));
                whShipping.setRequestedBy(rs.getString("RL.requested_by"));
                whShipping.setRequestedEmail(rs.getString("RL.requested_email"));
                whShipping.setRequestedDate(rs.getString("requested_date_view"));
//                whShipping.setInventoryLoc(rs.getString("RL.inventory_loc"));
                whShipping.setInventoryRack(rs.getString("RL.inventory_rack"));
                whShipping.setInventoryShelf(rs.getString("RL.inventory_shelf"));
                whShipping.setRemarks(rs.getString("RL.remarks"));
                whShipping.setBarcodeVerify(rs.getString("RL.barcode_verify"));
                whShipping.setUserVerify(rs.getString("RL.user_verify"));
                whShipping.setDateVerify(rs.getString("date_verify_view"));
                whShipping.setShippingDate(rs.getString("shipping_date_view"));
                whShipping.setShippingBy(rs.getString("SL.shipping_by"));
                whShipping.setStatus(rs.getString("SL.status"));
                whShipping.setFlag(rs.getString("SL.flag"));
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
        return whShipping;
    }
    
    public List<WhShipping> getWhShippingListMergeRequest() {
        String sql = "SELECT SL.*, RL.*, DATE_FORMAT(RL.material_pass_expiry,'%d %M %Y') AS mp_expiry_view , DATE_FORMAT(RL.requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                   + "DATE_FORMAT(RL.date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(SL.shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view "
                   + "FROM hms_wh_shipping_list SL, hms_wh_request_list RL "
                   + "WHERE SL.request_id = RL.request_id ";
        List<WhShipping> whShippingList = new ArrayList<WhShipping>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhShipping whShipping;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whShipping = new WhShipping();
                whShipping.setRequestId(rs.getString("SL.request_id"));
                whShipping.setMaterialPassNo(rs.getString("SL.material_pass_no"));
                whShipping.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whShipping.setEquipmentType(rs.getString("RL.equipment_type"));
                whShipping.setEquipmentId(rs.getString("RL.equipment_id"));
                whShipping.setQuantity(rs.getString("RL.quantity"));
                whShipping.setRequestedBy(rs.getString("RL.requested_by"));
                whShipping.setRequestedEmail(rs.getString("RL.requested_email"));
                whShipping.setRequestedDate(rs.getString("requested_date_view"));
//                whShipping.setInventoryLoc(rs.getString("RL.inventory_loc"));
                whShipping.setInventoryRack(rs.getString("RL.inventory_rack"));
                whShipping.setInventoryShelf(rs.getString("RL.inventory_shelf"));
                whShipping.setRemarks(rs.getString("RL.remarks"));
                whShipping.setBarcodeVerify(rs.getString("RL.barcode_verify"));
                whShipping.setUserVerify(rs.getString("RL.user_verify"));
                whShipping.setDateVerify(rs.getString("date_verify_view"));
                whShipping.setShippingDate(rs.getString("shipping_date_view"));
                whShipping.setShippingBy(rs.getString("SL.shipping_by"));
                whShipping.setStatus(rs.getString("SL.status"));
                whShipping.setFlag(rs.getString("SL.flag"));
                whShippingList.add(whShipping);
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
        return whShippingList;
    }
    
    public Integer getCountExistingData(String id) {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) AS count FROM hms_wh_shipping_list WHERE request_id = '" + id + "' "
            );

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("count");
            }
            LOGGER.info("count id..........." + count.toString());
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
    
    public Integer getCountMpNo(String mpno) {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) AS count FROM hms_wh_shipping_list WHERE material_pass_no = '" + mpno + "' "
            );

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("count");
            }
            LOGGER.info("count mpno..........." + count.toString());
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
    
    public WhShipping getWhShippingMergeWithRequestByMpNo(String whShippingMpNo) {
        String sql = "SELECT SL.*, RL.*, DATE_FORMAT(RL.material_pass_expiry,'%d %M %Y') AS mp_expiry_view , DATE_FORMAT(RL.requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                   + "DATE_FORMAT(RL.date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(SL.shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view "
                   + "FROM hms_wh_shipping_list SL, hms_wh_request_list RL "
                   + "WHERE SL.material_pass_no = '" + whShippingMpNo + "' ";
        WhShipping whShipping = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whShipping = new WhShipping();
                whShipping.setRequestId(rs.getString("SL.request_id"));
                whShipping.setMaterialPassNo(rs.getString("SL.material_pass_no"));
                whShipping.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whShipping.setEquipmentType(rs.getString("RL.equipment_type"));
                whShipping.setEquipmentId(rs.getString("RL.equipment_id"));
                whShipping.setQuantity(rs.getString("RL.quantity"));
                whShipping.setRequestedBy(rs.getString("RL.requested_by"));
                whShipping.setRequestedEmail(rs.getString("RL.requested_email"));
                whShipping.setRequestedDate(rs.getString("requested_date_view"));
//                whShipping.setInventoryLoc(rs.getString("RL.inventory_loc"));
                whShipping.setInventoryRack(rs.getString("RL.inventory_rack"));
                whShipping.setInventoryShelf(rs.getString("RL.inventory_shelf"));
                whShipping.setRemarks(rs.getString("RL.remarks"));
                whShipping.setBarcodeVerify(rs.getString("RL.barcode_verify"));
                whShipping.setUserVerify(rs.getString("RL.user_verify"));
                whShipping.setDateVerify(rs.getString("date_verify_view"));
                whShipping.setShippingDate(rs.getString("shipping_date_view"));
                whShipping.setShippingBy(rs.getString("shipping_date_view"));
                whShipping.setStatus(rs.getString("SL.status"));
                whShipping.setFlag(rs.getString("SL.flag"));
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
        return whShipping;
    }
    
    public List<WhShippingLog> getWhShippingReqLog(String whShippingId) {
        String sql  = "SELECT *, DATE_FORMAT(timestamp,'%d %M %Y %h:%i %p') AS timestamp_view, DATE_FORMAT(verified_date,'%d %M %Y %h:%i %p') AS verified_date_view, "
                    + "DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, DATE_FORMAT(received_date,'%d %M %Y %h:%i %p') AS received_date_view, "
                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(requested_date, received_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(requested_date, received_date)), 24), ' hours, ', MINUTE(TIMEDIFF(requested_date, received_date)), ' mins') AS request_receive, "
                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(received_date, date_verify)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(received_date, date_verify)), 24), ' hours, ', MINUTE(TIMEDIFF(received_date, date_verify)), ' mins') AS receive_verify1, "
                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(date_verify, inventory_date_verify)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(date_verify, inventory_date_verify)), 24), ' hours, ', MINUTE(TIMEDIFF(date_verify, inventory_date_verify)), ' mins') AS verify1_verify2, "
//                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(received_date, inventory_date_verify)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(received_date, inventory_date_verify)), 24), ' hours, ', MINUTE(TIMEDIFF(received_date, inventory_date_verify)), ' mins') AS receive_verify2, "
//                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(requested_date, inventory_date_verify)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(requested_date, inventory_date_verify)), 24), ' hours, ', MINUTE(TIMEDIFF(requested_date, inventory_date_verify)), ' mins') AS request_verify2, "
                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(inventory_date_verify, shipping_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(inventory_date_verify, shipping_date)), 24), ' hours, ', MINUTE(TIMEDIFF(inventory_date_verify, shipping_date)), ' mins') AS verify2_shipping, "
                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(received_date, shipping_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(received_date, shipping_date)), 24), ' hours, ', MINUTE(TIMEDIFF(received_date, shipping_date)), ' mins') AS receive_shipping, "
                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(requested_date, shipping_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(requested_date, shipping_date)), 24), ' hours, ', MINUTE(TIMEDIFF(requested_date, shipping_date)), ' mins') AS request_shipping "
                    + "FROM hms_wh_log L, hms_wh_request_list R, hms_wh_shipping_list S "
                    + "WHERE L.reference_id = R.request_id AND R.request_id = S.request_id AND S.request_id = '" + whShippingId + "' "
                    + "ORDER BY timestamp DESC";
        List<WhShippingLog> whShippingList = new ArrayList<WhShippingLog>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhShippingLog whShippingLog;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whShippingLog = new WhShippingLog();
                //log
                whShippingLog.setId(rs.getString("L.id"));
                whShippingLog.setReferenceId(rs.getString("reference_id"));
                whShippingLog.setModuleId(rs.getString("module_id"));
                whShippingLog.setModuleName(rs.getString("module_name"));
                whShippingLog.setLogStatus(rs.getString("L.status"));
                whShippingLog.setTimestamp(rs.getString("timestamp_view"));
                whShippingLog.setLogVerifyDate(rs.getString("verified_date_view"));
                whShippingLog.setLogVerifyBy(rs.getString("verified_by"));
                //request
                whShippingLog.setShippingId(whShippingId);
                whShippingLog.setMaterialPassNo(rs.getString("material_pass_no"));
                whShippingLog.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whShippingLog.setEquipmentType(rs.getString("equipment_type"));
                whShippingLog.setEquipmentId(rs.getString("equipment_id"));
                whShippingLog.setPcbA(rs.getString("pcb_a"));
                whShippingLog.setQtyQualA(rs.getString("qty_qual_a"));
                whShippingLog.setPcbB(rs.getString("pcb_b"));
                whShippingLog.setQtyQualB(rs.getString("qty_qual_b"));
                whShippingLog.setPcbC(rs.getString("pcb_c"));
                whShippingLog.setQtyQualC(rs.getString("qty_qual_c"));
                whShippingLog.setPcbControl(rs.getString("pcb_control"));
                whShippingLog.setQtyControl(rs.getString("qty_control"));
                whShippingLog.setQuantity(rs.getString("quantity"));
                whShippingLog.setRequestedBy(rs.getString("requested_by"));
                whShippingLog.setRequestedEmail(rs.getString("requested_email"));
                whShippingLog.setRequestedDate(rs.getString("requested_date_view"));
                whShippingLog.setInventoryLoc(rs.getString("inventory_loc"));
                whShippingLog.setInventoryRack(rs.getString("inventory_rack"));
                whShippingLog.setInventoryShelf(rs.getString("inventory_shelf"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whShippingLog.setRemarks(remarks);
                whShippingLog.setReceivedDate(rs.getString("received_date_view"));
                whShippingLog.setBarcodeVerify(rs.getString("barcode_verify"));
                whShippingLog.setDateVerify(rs.getString("date_verify"));
                whShippingLog.setUserVerify(rs.getString("user_verify"));
                whShippingLog.setStatus(rs.getString("R.status"));
                whShippingLog.setFlag(rs.getString("flag"));
                whShippingLog.setInventoryRackVerify(rs.getString("inventory_rack_verify"));
                whShippingLog.setInventoryShelfVerify(rs.getString("inventory_Shelf_verify"));
                whShippingLog.setInventoryUserVerify(rs.getString("inventory_User_verify"));
                whShippingLog.setInventoryDateVerify(rs.getString("inventory_Date_verify"));
                whShippingLog.setStatus(rs.getString("R.status"));
                whShippingLog.setFlag(rs.getString("R.flag"));
                //shipping
                whShippingLog.setShippingId(rs.getString("S.id"));
                whShippingLog.setShippingDate(rs.getString("shipping_date"));
                whShippingLog.setShippingBy(rs.getString("shipping_by"));
                whShippingLog.setShippingStatus(rs.getString("S.status"));
                whShippingLog.setShippingFlag(rs.getString("S.flag"));
                //duration
                whShippingLog.setRequestReceive(rs.getString("request_receive"));
                whShippingLog.setReceiveVerify1(rs.getString("receive_verify1"));
                whShippingLog.setVerify1Verify2(rs.getString("verify1_verify2"));
//                whShippingLog.setReceiveVerify2(rs.getString("receive_verify2"));
//                whShippingLog.setRequestVerify2(rs.getString("request_verify2"));
                whShippingLog.setVerify2Shipping(rs.getString("verify2_shipping"));
                whShippingLog.setReceiveShipping(rs.getString("receive_shipping"));
                whShippingLog.setRequestShipping(rs.getString("request_shipping"));
                
                whShippingList.add(whShippingLog);
                System.out.println("*********************** LIST ************************" + whShippingList);
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
        return whShippingList;
    }
}