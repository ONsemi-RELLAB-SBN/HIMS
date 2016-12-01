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
    
    public QueryResult updateStatus(WhShipping whShipping) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_shipping_list "
                   + "SET status = ?, flag = ?, close_date = NOW() "
                   + "WHERE request_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whShipping.getStatus());
            ps.setString(2, whShipping.getFlag());
            ps.setString(3, whShipping.getRequestId());
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
                   + "WHERE SL.request_id = RL.request_id AND SL.flag NOT LIKE '2' "
                   + "ORDER BY RL.date_verify DESC ";
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
                "SELECT COUNT(*) AS count "
                + "FROM hms_wh_shipping_list "
                + "WHERE request_id = '" + id + "' "
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
    
    public Integer getCountDone(String id) {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) AS count "
                + "FROM hms_wh_shipping_list "
                + "WHERE request_id = '" + id + "' AND flag = '1' "
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
    
    public Integer getCountMpNo(String mpno) {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) AS count FROM hms_wh_shipping_list WHERE material_pass_no = '" + mpno + "' AND flag NOT LIKE '2' "
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
                whShipping.setId(rs.getString("SL.id"));
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
                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(requested_date, date_verify)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(requested_date, date_verify)), 24), ' hours, ', MINUTE(TIMEDIFF(requested_date, date_verify)), ' mins, ', SECOND(TIMEDIFF(requested_date, date_verify)), ' secs') AS req_bar_verify, "
                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(date_verify, inventory_date_verify)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(date_verify, inventory_date_verify)), 24), ' hours, ', MINUTE(TIMEDIFF(date_verify, inventory_date_verify)), ' mins, ', SECOND(TIMEDIFF(date_verify, inventory_date_verify)), ' secs') AS bar_inv_verify, "
                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(inventory_date_verify, shipping_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(inventory_date_verify, shipping_date)), 24), ' hours, ', MINUTE(TIMEDIFF(inventory_date_verify, shipping_date)), ' mins, ', SECOND(TIMEDIFF(inventory_date_verify, shipping_date)), ' secs') AS inv_verify_ship, "
                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(shipping_date, close_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(shipping_date, close_date)), 24), ' hours, ', MINUTE(TIMEDIFF(shipping_date, close_date)), ' mins, ', SECOND(TIMEDIFF(shipping_date, close_date)), ' secs') AS ship_close "
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
                whShippingLog.setReasonRetrieval(rs.getString("reason_retrieval"));
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
                whShippingLog.setRequestBarVerify(rs.getString("req_bar_verify"));
                whShippingLog.setBarVerifyInvVerify(rs.getString("bar_inv_verify"));
                whShippingLog.setInvVerifyShip(rs.getString("inv_verify_ship"));
                whShippingLog.setShipClose(rs.getString("ship_close"));
//                whShippingLog.setRequestReceive(rs.getString("request_receive"));
//                whShippingLog.setReceiveVerify1(rs.getString("receive_verify1"));
//                whShippingLog.setVerify1Verify2(rs.getString("verify1_verify2"));
////                whShippingLog.setReceiveVerify2(rs.getString("receive_verify2"));
////                whShippingLog.setRequestVerify2(rs.getString("request_verify2"));
//                whShippingLog.setVerify2Shipping(rs.getString("verify2_shipping"));
//                whShippingLog.setReceiveShipping(rs.getString("receive_shipping"));
//                whShippingLog.setRequestShipping(rs.getString("request_shipping"));
                
                whShippingList.add(whShippingLog);
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
    
    public List<WhShipping> getQuery(String query) {
        String sql = "SELECT *, DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                   + "DATE_FORMAT(date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view "
                   + "FROM hms_wh_request_list R, hms_wh_shipping_list S "
                   + "WHERE R.request_id = S.request_id AND " + query;
        List<WhShipping> whShippingList = new ArrayList<WhShipping>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhShipping whShipping;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whShipping = new WhShipping();
                whShipping.setId(rs.getString("id"));
                whShipping.setRequestId(rs.getString("request_id"));
                whShipping.setMaterialPassNo(rs.getString("S.material_pass_no"));
                whShipping.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whShipping.setEquipmentType(rs.getString("equipment_type"));
                whShipping.setEquipmentId(rs.getString("equipment_id"));
                whShipping.setQuantity(rs.getString("quantity"));
                whShipping.setRequestedBy(rs.getString("requested_by"));
                whShipping.setRequestedEmail(rs.getString("requested_email"));
                whShipping.setRequestedDate(rs.getString("requested_date_view"));
                whShipping.setInventoryRack(rs.getString("inventory_rack"));
                whShipping.setInventoryShelf(rs.getString("inventory_shelf"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whShipping.setRemarks(remarks);
                whShipping.setBarcodeVerify(rs.getString("barcode_verify"));
                whShipping.setDateVerify(rs.getString("date_verify_view"));
                whShipping.setUserVerify(rs.getString("user_verify"));
                whShipping.setShippingBy(rs.getString("shipping_by"));
                whShipping.setShippingDate(rs.getString("shipping_date_view"));
                whShipping.setStatus(rs.getString("S.status"));
                whShipping.setFlag(rs.getString("S.flag"));
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
    
    public List<WhShipping> getHardwareId() {
        String sql = "SELECT DISTINCT equipment_id " +
                    "FROM hms_wh_request_list R, hms_wh_shipping_list S " +
                    "WHERE R.request_id = S.request_id " +
                    "ORDER BY equipment_id ";
        List<WhShipping> hardwareIdList = new ArrayList<WhShipping>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhShipping whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhShipping();
                whRetrieve.setEquipmentId(rs.getString("equipment_id"));
                hardwareIdList.add(whRetrieve);
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
        return hardwareIdList;
    }
    
    public List<WhShipping> getHardwareType() {
        String sql = "SELECT DISTINCT equipment_type " +
                    "FROM hms_wh_request_list R, hms_wh_shipping_list S " +
                    "WHERE R.request_id = S.request_id " +
                    "ORDER BY equipment_type ";
        List<WhShipping> hardwareTypeList = new ArrayList<WhShipping>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhShipping whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhShipping();
                whRetrieve.setEquipmentType(rs.getString("equipment_type"));
                hardwareTypeList.add(whRetrieve);
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
        return hardwareTypeList;
    }
    
    public List<WhShipping> getRequestedBy() {
        String sql = "SELECT DISTINCT requested_by " +
                    "FROM hms_wh_request_list R, hms_wh_shipping_list S " +
                    "WHERE R.request_id = S.request_id " +
                    "ORDER BY requested_by ";
        List<WhShipping> requestedByList = new ArrayList<WhShipping>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhShipping whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhShipping();
                whRetrieve.setRequestedBy(rs.getString("requested_by"));
                requestedByList.add(whRetrieve);
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
        return requestedByList;
    }
    
    public List<WhShipping> getStatusR() {
        String sql = "SELECT DISTINCT status " +
                    "FROM hms_wh_request_list " +
                    "ORDER BY status ";
        List<WhShipping> statusRList = new ArrayList<WhShipping>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhShipping whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhShipping();
                whRetrieve.setStatus(rs.getString("status"));
                statusRList.add(whRetrieve);
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
        return statusRList;
    }
    
    public List<WhShipping> getStatusS() {
        String sql = "SELECT DISTINCT status " +
                    "FROM hms_wh_shipping_list " +
                    "ORDER BY status ";
        List<WhShipping> statusSList = new ArrayList<WhShipping>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhShipping whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhShipping();
                whRetrieve.setStatus(rs.getString("status"));
                statusSList.add(whRetrieve);
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
        return statusSList;
    }
    
    public List<WhShipping> getRack() {
        String sql = "SELECT DISTINCT inventory_rack " +
                    "FROM hms_wh_request_list R, hms_wh_shipping_list S " +
                    "WHERE R.request_id = S.request_id " +
                    "ORDER BY inventory_rack ";
        List<WhShipping> rackList = new ArrayList<WhShipping>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhShipping whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhShipping();
                whRetrieve.setInventoryRack(rs.getString("inventory_rack"));
                rackList.add(whRetrieve);
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
        return rackList;
    }
    
    public List<WhShipping> getShelf() {
        String sql = "SELECT DISTINCT inventory_shelf " +
                    "FROM hms_wh_request_list R, hms_wh_shipping_list S " +
                    "WHERE R.request_id = S.request_id " +
                    "ORDER BY inventory_shelf ";
        List<WhShipping> shelfList = new ArrayList<WhShipping>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhShipping whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhShipping();
                whRetrieve.setInventoryShelf(rs.getString("inventory_shelf"));
                shelfList.add(whRetrieve);
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
        return shelfList;
    }
    
    public List<WhShipping> getShippingBy() {
        String sql = "SELECT DISTINCT shipping_by " +
                    "FROM hms_wh_request_list R, hms_wh_shipping_list S " +
                    "WHERE R.request_id = S.request_id " +
                    "ORDER BY shipping_by ";
        List<WhShipping> shelfList = new ArrayList<WhShipping>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhShipping whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhShipping();
                whRetrieve.setShippingBy(rs.getString("shipping_by"));
                shelfList.add(whRetrieve);
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
        return shelfList;
    }
}