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
import com.onsemi.hms.model.WhRequest;
import com.onsemi.hms.model.WhRequestLog;
import com.onsemi.hms.tools.QueryResult;
import com.onsemi.hms.tools.SpmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhRequestDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhRequestDAO.class);
    private final Connection conn;
    private final DataSource dataSource;

    public WhRequestDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
        this.dataSource = db.getDataSource();
    }

    
    public QueryResult insertWhRequest(WhRequest whRequest) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hms_wh_request_list (request_id, material_pass_no, material_pass_expiry, equipment_type, equipment_id, reason_retrieval, "
                                                    + "pcb_a, qty_qual_a, pcb_b, qty_qual_b, pcb_c, qty_qual_c, pcb_control, qty_control, quantity, "
                                                    + "requested_by, requested_email, requested_date, inventory_rack, inventory_shelf, remarks, received_date, status, flag) "
                  + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, whRequest.getRefId());
            ps.setString(2, whRequest.getMaterialPassNo());
            ps.setString(3, whRequest.getMaterialPassExpiry());
            ps.setString(4, whRequest.getEquipmentType());
            ps.setString(5, whRequest.getEquipmentId());
            ps.setString(6, whRequest.getReasonRetrieval());
            ps.setString(7, whRequest.getPcbA());
            ps.setString(8, whRequest.getQtyQualA());
            ps.setString(9, whRequest.getPcbB());
            ps.setString(10, whRequest.getQtyQualB());
            ps.setString(11, whRequest.getPcbC());
            ps.setString(12, whRequest.getQtyQualC());
            ps.setString(13, whRequest.getPcbControl());
            ps.setString(14, whRequest.getQtyControl());
            ps.setString(15, whRequest.getQuantity());
            ps.setString(16, whRequest.getRequestedBy());
            ps.setString(17, whRequest.getRequestedEmail());
            ps.setString(18, whRequest.getRequestedDate());
            ps.setString(19, whRequest.getInventoryRack());
            ps.setString(20, whRequest.getInventoryShelf());
            ps.setString(21, whRequest.getRemarks());
            ps.setString(22, whRequest.getStatus());
            ps.setString(23, whRequest.getFlag());

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
    
    public QueryResult updateWhRequestVerification(WhRequest whRequest) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_request_list SET barcode_verify = ?, user_verify = ?, date_verify = ?, status = ?, flag = ?, temp_count = ? "
                   + "WHERE request_id = ? AND material_pass_no = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whRequest.getBarcodeVerify());
            ps.setString(2, whRequest.getUserVerify());
            ps.setString(3, whRequest.getDateVerify());
            ps.setString(4, whRequest.getStatus());
            ps.setString(5, whRequest.getFlag());
            ps.setString(6, whRequest.getTempCount());
            ps.setString(7, whRequest.getRefId());
            ps.setString(8, whRequest.getMaterialPassNo());
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
    
    public QueryResult updateWhRequestForShipping(WhRequest whRequest) {
        QueryResult queryResult = new QueryResult();
        String sql = 
               "UPDATE hms_wh_request_list SET inventory_rack_verify = ?, inventory_shelf_verify = ?, inventory_date_verify = ?, inventory_user_verify = ?, status = ?, flag = ? "
             + "WHERE request_id = ? AND material_pass_no = ? ";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whRequest.getInventoryRackVerify());
            ps.setString(2, whRequest.getInventoryShelfVerify());
            ps.setString(3, whRequest.getInventoryDateVerify());
            ps.setString(4, whRequest.getInventoryUserVerify());
            ps.setString(5, whRequest.getStatus());
            ps.setString(6, whRequest.getFlag());
            ps.setString(7, whRequest.getRefId());
            ps.setString(8, whRequest.getMaterialPassNo());
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

    public QueryResult updateWhRequestForApproval(WhRequest whRequest) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_request_list SET status = ?, flag = ?"
                   + "WHERE request_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whRequest.getStatus());
            ps.setString(2, whRequest.getFlag());
            ps.setString(3, whRequest.getRefId());
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
    
    public QueryResult updateWhRequestStatus(WhRequest whRequest) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_request_list SET status = ?, flag = ?"
                   + "WHERE request_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whRequest.getStatus());
            ps.setString(2, whRequest.getFlag());
            ps.setString(3, whRequest.getRefId());
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
    
    public Integer getCountExistingData(String id) {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) AS count FROM hms_wh_request_list WHERE request_id = '" + id + "' "
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
                "SELECT COUNT(*) AS count FROM hms_wh_request_list WHERE request_id = '" + id + "' AND flag = '1' "
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
    
    public QueryResult deleteWhRequest(String whRequestId) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM hms_wh_request_list WHERE request_id = '" + whRequestId + "'"
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

    public WhRequest getWhReq(String whRequestId) {
        String sql  = "SELECT * "
                    + "FROM hms_wh_request_list "
                    + "WHERE request_id = '" + whRequestId + "' ";
        WhRequest whRequest = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRequest = new WhRequest();
                whRequest.setId(rs.getString("id"));
                whRequest.setRefId(rs.getString("request_id"));
                whRequest.setMaterialPassNo(rs.getString("material_pass_no"));
                whRequest.setMaterialPassExpiry(rs.getString("material_pass_expiry"));
                whRequest.setEquipmentType(rs.getString("equipment_type"));
                whRequest.setEquipmentId(rs.getString("equipment_id"));
                whRequest.setReasonRetrieval(rs.getString("reason_retrieval"));
                whRequest.setQuantity(rs.getString("quantity"));
                whRequest.setRequestedBy(rs.getString("requested_by"));
                whRequest.setRequestedEmail(rs.getString("requested_email"));
                whRequest.setRequestedDate(rs.getString("requested_date"));
                whRequest.setInventoryRack(rs.getString("inventory_rack"));
                whRequest.setInventoryShelf(rs.getString("inventory_shelf"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRequest.setRemarks(remarks);
                whRequest.setBarcodeVerify(rs.getString("barcode_verify"));
                whRequest.setDateVerify(rs.getString("date_verify"));
                whRequest.setUserVerify(rs.getString("user_verify"));
                whRequest.setInventoryRackVerify(rs.getString("inventory_rack_verify"));
                whRequest.setInventoryShelfVerify(rs.getString("inventory_shelf_verify"));
                whRequest.setInventoryDateVerify(rs.getString("inventory_date_verify"));
                whRequest.setInventoryUserVerify(rs.getString("inventory_user_verify"));
                whRequest.setStatus(rs.getString("status"));
                whRequest.setFlag(rs.getString("flag"));
                whRequest.setTempCount(rs.getString("temp_count"));
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
        return whRequest;
    }
    
    public WhRequest getWhRequest(String whRequestId) {
        String sql  = "SELECT *,DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                    + "DATE_FORMAT(date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(inventory_date_verify,'%d %M %Y %h:%i %p') AS inventory_date_verify_view "
                    + "FROM hms_wh_request_list "
                    + "WHERE request_id = '" + whRequestId + "' ";
        WhRequest whRequest = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRequest = new WhRequest();
                whRequest.setId(rs.getString("id"));
                whRequest.setRefId(rs.getString("request_id"));
                whRequest.setMaterialPassNo(rs.getString("material_pass_no"));
                whRequest.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whRequest.setEquipmentType(rs.getString("equipment_type"));
                whRequest.setEquipmentId(rs.getString("equipment_id"));
                whRequest.setReasonRetrieval(rs.getString("reason_retrieval"));
                whRequest.setQuantity(rs.getString("quantity"));
                whRequest.setRequestedBy(rs.getString("requested_by"));
                whRequest.setRequestedEmail(rs.getString("requested_email"));
                whRequest.setRequestedDate(rs.getString("requested_date_view"));
                whRequest.setInventoryRack(rs.getString("inventory_rack"));
                whRequest.setInventoryShelf(rs.getString("inventory_shelf"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRequest.setRemarks(remarks);
                whRequest.setBarcodeVerify(rs.getString("barcode_verify"));
                whRequest.setDateVerify(rs.getString("date_verify_view"));
                whRequest.setUserVerify(rs.getString("user_verify"));
                whRequest.setInventoryRackVerify(rs.getString("inventory_rack_verify"));
                whRequest.setInventoryShelfVerify(rs.getString("inventory_shelf_verify"));
                whRequest.setInventoryDateVerify(rs.getString("inventory_date_verify_view"));
                whRequest.setInventoryUserVerify(rs.getString("inventory_user_verify"));
                whRequest.setStatus(rs.getString("status"));
                whRequest.setFlag(rs.getString("flag"));
                whRequest.setTempCount(rs.getString("temp_count"));
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
        return whRequest;
    }

    public List<WhRequest> getWhRequestList() {
        String sql = "SELECT *, DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                   + "DATE_FORMAT(date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(inventory_date_verify,'%d %M %Y %h:%i %p') AS inventory_date_verify_view "
                   + "FROM hms_wh_request_list "
                   + "WHERE flag = '0' "
                   + "ORDER BY requested_date DESC";
        List<WhRequest> whRequestList = new ArrayList<WhRequest>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRequest whRequest;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRequest = new WhRequest();
                whRequest.setId(rs.getString("id"));
                whRequest.setRefId(rs.getString("request_id"));
                whRequest.setMaterialPassNo(rs.getString("material_pass_no"));
                whRequest.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whRequest.setEquipmentType(rs.getString("equipment_type"));
                whRequest.setEquipmentId(rs.getString("equipment_id"));
                whRequest.setReasonRetrieval(rs.getString("reason_retrieval"));
                whRequest.setQuantity(rs.getString("quantity"));
                whRequest.setRequestedBy(rs.getString("requested_by"));
                whRequest.setRequestedEmail(rs.getString("requested_email"));
                whRequest.setRequestedDate(rs.getString("requested_date_view"));
                whRequest.setInventoryRack(rs.getString("inventory_rack"));
                whRequest.setInventoryShelf(rs.getString("inventory_shelf"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRequest.setRemarks(remarks);
                whRequest.setBarcodeVerify(rs.getString("barcode_verify"));
                whRequest.setDateVerify(rs.getString("date_verify_view"));
                whRequest.setUserVerify(rs.getString("user_verify"));
                whRequest.setInventoryRackVerify(rs.getString("inventory_rack_verify"));
                whRequest.setInventoryShelfVerify(rs.getString("inventory_shelf_verify"));
                whRequest.setInventoryDateVerify(rs.getString("inventory_date_verify_view"));
                whRequest.setInventoryUserVerify(rs.getString("inventory_user_verify"));
                whRequest.setStatus(rs.getString("status"));
                whRequest.setFlag(rs.getString("flag"));
                whRequest.setTempCount(rs.getString("temp_count"));
                whRequestList.add(whRequest);
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
        return whRequestList;
    }
    
    //yesterday report
    public List<WhRequest> getWhRequestReportList() {
        String sql = "SELECT *, CONCAT(FLOOR(HOUR(TIMEDIFF(received_date, date_verify)) / 24), ' DAYS, ', MOD(HOUR(TIMEDIFF(received_date, date_verify)), 24), ' HOURS, ', MINUTE(TIMEDIFF(received_date, date_verify)), ' MINS') AS DURATION "
                   + "FROM hms_wh_request_list "
                   + "WHERE DATE(date_verify) LIKE SUBDATE(DATE(NOW()),1) ";
        List<WhRequest> whRequestList = new ArrayList<WhRequest>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRequest whRequest;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRequest = new WhRequest();
                whRequest.setRefId(rs.getString("request_id"));
                whRequest.setMaterialPassNo(rs.getString("material_pass_no"));
                whRequest.setMaterialPassExpiry(rs.getString("material_pass_expiry"));
                whRequest.setEquipmentType(rs.getString("equipment_type"));
                whRequest.setEquipmentId(rs.getString("equipment_id"));
                whRequest.setReasonRetrieval(rs.getString("reason_retrieval"));
                whRequest.setQuantity(rs.getString("quantity"));
                whRequest.setRequestedBy(rs.getString("requested_by"));
                whRequest.setRequestedEmail(rs.getString("requested_email"));
                whRequest.setRequestedDate(rs.getString("requested_date"));
                whRequest.setInventoryRack(rs.getString("inventory_rack"));
                whRequest.setInventoryShelf(rs.getString("inventory_shelf"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRequest.setRemarks(remarks);
                whRequest.setReceivedDate(rs.getString("received_date"));
                whRequest.setBarcodeVerify(rs.getString("barcode_verify"));
                whRequest.setDateVerify(rs.getString("date_verify"));
                whRequest.setUserVerify(rs.getString("user_verify"));
                whRequest.setStatus(rs.getString("status"));
                whRequest.setFlag(rs.getString("flag"));
                whRequest.setDuration(rs.getString("duration"));
                whRequest.setTempCount(rs.getString("temp_count"));
                whRequestList.add(whRequest);
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
        return whRequestList;
    }
    
    public Integer getCountYesterday() {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(date_verify) AS count " +
                "FROM hms_wh_request_list " +
                "WHERE DATE(date_verify) LIKE SUBDATE(DATE(NOW()),1) "
            );

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("count");
            }
            LOGGER.info("count date..........." + count.toString());
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
    
    public List<WhRequestLog> getWhReqLog(String whRequestId) {
        String sql  = "SELECT *, DATE_FORMAT(timestamp,'%d %M %Y %h:%i %p') AS timestamp_view, DATE_FORMAT(verified_date,'%d %M %Y %h:%i %p') AS verified_date_view, "
                    + "DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, DATE_FORMAT(received_date,'%d %M %Y %h:%i %p') AS received_date_view, "
                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(requested_date, date_verify)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(requested_date, date_verify)), 24), ' hours, ', MINUTE(TIMEDIFF(requested_date, date_verify)), ' mins, ', SECOND(TIMEDIFF(requested_date, date_verify)), ' secs') AS req_bar_verify, "
                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(date_verify, inventory_date_verify)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(date_verify, inventory_date_verify)), 24), ' hours, ', MINUTE(TIMEDIFF(date_verify, inventory_date_verify)), ' mins, ', SECOND(TIMEDIFF(date_verify, inventory_date_verify)), ' secs') AS bar_inv_verify "
                    + "FROM hms_wh_log L, hms_wh_request_list R "
                    + "WHERE L.reference_id = R.request_id AND R.request_id = '" + whRequestId + "' "
                    + "ORDER BY timestamp DESC";
        
//        + "CONCAT(FLOOR(HOUR(TIMEDIFF(requested_date, received_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(requested_date, received_date)), 24), ' hours, ', MINUTE(TIMEDIFF(requested_date, received_date)), ' mins') AS request_receive, "
//                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(received_date, date_verify)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(received_date, date_verify)), 24), ' hours, ', MINUTE(TIMEDIFF(received_date, date_verify)), ' mins') AS receive_verify1, "
//                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(date_verify, inventory_date_verify)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(date_verify, inventory_date_verify)), 24), ' hours, ', MINUTE(TIMEDIFF(date_verify, inventory_date_verify)), ' mins') AS verify1_verify2, "
//                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(received_date, inventory_date_verify)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(received_date, inventory_date_verify)), 24), ' hours, ', MINUTE(TIMEDIFF(received_date, inventory_date_verify)), ' mins') AS receive_verify2, "
//                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(requested_date, inventory_date_verify)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(requested_date, inventory_date_verify)), 24), ' hours, ', MINUTE(TIMEDIFF(requested_date, inventory_date_verify)), ' mins') AS request_verify2 "
                
        List<WhRequestLog> whRequestList = new ArrayList<WhRequestLog>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRequestLog whRequestLog;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRequestLog = new WhRequestLog();
                //log
                whRequestLog.setId(rs.getString("L.id"));
                whRequestLog.setReferenceId(rs.getString("reference_id"));
                whRequestLog.setModuleId(rs.getString("module_id"));
                whRequestLog.setModuleName(rs.getString("module_name"));
                whRequestLog.setLogStatus(rs.getString("L.status"));
                whRequestLog.setTimestamp(rs.getString("timestamp_view"));
                whRequestLog.setLogVerifyDate(rs.getString("verified_date_view"));
                whRequestLog.setLogVerifyBy(rs.getString("verified_by"));
                //request
                whRequestLog.setRequestId(whRequestId);
                whRequestLog.setMaterialPassNo(rs.getString("material_pass_no"));
                whRequestLog.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whRequestLog.setEquipmentType(rs.getString("equipment_type"));
                whRequestLog.setEquipmentId(rs.getString("equipment_id"));
                whRequestLog.setReasonRetrieval(rs.getString("reason_retrieval"));
                whRequestLog.setPcbA(rs.getString("pcb_a"));
                whRequestLog.setQtyQualA(rs.getString("qty_qual_a"));
                whRequestLog.setPcbB(rs.getString("pcb_b"));
                whRequestLog.setQtyQualB(rs.getString("qty_qual_b"));
                whRequestLog.setPcbC(rs.getString("pcb_c"));
                whRequestLog.setQtyQualC(rs.getString("qty_qual_c"));
                whRequestLog.setPcbControl(rs.getString("pcb_control"));
                whRequestLog.setQtyControl(rs.getString("qty_control"));
                whRequestLog.setQuantity(rs.getString("quantity"));
                whRequestLog.setRequestedBy(rs.getString("requested_by"));
                whRequestLog.setRequestedEmail(rs.getString("requested_email"));
                whRequestLog.setRequestedDate(rs.getString("requested_date_view"));
                whRequestLog.setInventoryLoc(rs.getString("inventory_loc"));
                whRequestLog.setInventoryRack(rs.getString("inventory_rack"));
                whRequestLog.setInventoryShelf(rs.getString("inventory_shelf"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRequestLog.setRemarks(remarks);
                whRequestLog.setReceivedDate(rs.getString("received_date_view"));
                whRequestLog.setBarcodeVerify(rs.getString("barcode_verify"));
                whRequestLog.setDateVerify(rs.getString("date_verify"));
                whRequestLog.setUserVerify(rs.getString("user_verify"));
                whRequestLog.setStatus(rs.getString("R.status"));
                whRequestLog.setFlag(rs.getString("flag"));
                whRequestLog.setInventoryRackVerify(rs.getString("inventory_rack_verify"));
                whRequestLog.setInventoryShelfVerify(rs.getString("inventory_Shelf_verify"));
                whRequestLog.setInventoryUserVerify(rs.getString("inventory_User_verify"));
                whRequestLog.setInventoryDateVerify(rs.getString("inventory_Date_verify"));
                whRequestLog.setStatus(rs.getString("R.status"));
                whRequestLog.setFlag(rs.getString("R.flag"));
                whRequestLog.setRequestBarVerify(rs.getString("req_bar_verify"));
                whRequestLog.setBarVerifyInvVerify(rs.getString("bar_inv_verify"));
//                whRequestLog.setRequestReceive(rs.getString("request_receive"));
//                whRequestLog.setReceiveVerify1(rs.getString("receive_verify1"));
//                whRequestLog.setVerify1Verify2(rs.getString("verify1_verify2"));
//                whRequestLog.setReceiveVerify2(rs.getString("receive_verify2"));
//                whRequestLog.setRequestVerify2(rs.getString("request_verify2"));
                whRequestLog.setTempCount(rs.getString("temp_count"));
                whRequestList.add(whRequestLog);
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
        return whRequestList;
    }
    
    public List<WhRequest> getQuery(String query) {
        String sql = "SELECT *, DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                   + "DATE_FORMAT(date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(inventory_date_verify,'%d %M %Y %h:%i %p') AS inventory_date_verify_view "
                   + "FROM hms_wh_request_list "
                   + "WHERE " + query;
        List<WhRequest> whRequestList = new ArrayList<WhRequest>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRequest whRequest;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRequest = new WhRequest();
                whRequest.setId(rs.getString("id"));
                whRequest.setRefId(rs.getString("request_id"));
                whRequest.setMaterialPassNo(rs.getString("material_pass_no"));
                whRequest.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whRequest.setEquipmentType(rs.getString("equipment_type"));
                whRequest.setEquipmentId(rs.getString("equipment_id"));
                whRequest.setReasonRetrieval(rs.getString("reason_retrieval"));
                whRequest.setQuantity(rs.getString("quantity"));
                whRequest.setRequestedBy(rs.getString("requested_by"));
                whRequest.setRequestedEmail(rs.getString("requested_email"));
                whRequest.setRequestedDate(rs.getString("requested_date_view"));
                whRequest.setInventoryRack(rs.getString("inventory_rack"));
                whRequest.setInventoryShelf(rs.getString("inventory_shelf"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRequest.setRemarks(remarks);
                whRequest.setBarcodeVerify(rs.getString("barcode_verify"));
                whRequest.setDateVerify(rs.getString("date_verify_view"));
                whRequest.setUserVerify(rs.getString("user_verify"));
                whRequest.setInventoryRackVerify(rs.getString("inventory_rack_verify"));
                whRequest.setInventoryShelfVerify(rs.getString("inventory_shelf_verify"));
                whRequest.setInventoryDateVerify(rs.getString("inventory_date_verify_view"));
                whRequest.setInventoryUserVerify(rs.getString("inventory_user_verify"));
                whRequest.setStatus(rs.getString("status"));
                whRequest.setFlag(rs.getString("flag"));
                whRequest.setTempCount(rs.getString("temp_count"));
                whRequestList.add(whRequest);
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
        return whRequestList;
    }
    
    public List<WhRequest> getHardwareId() {
        String sql = "SELECT DISTINCT equipment_id " +
                    "FROM hms_wh_request_list " +
                    "ORDER BY equipment_id ";
        List<WhRequest> hardwareIdList = new ArrayList<WhRequest>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRequest whRequest;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRequest = new WhRequest();
                whRequest.setEquipmentId(rs.getString("equipment_id"));
                hardwareIdList.add(whRequest);
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
    
    public List<WhRequest> getHardwareType() {
        String sql = "SELECT DISTINCT equipment_type " +
                    "FROM hms_wh_request_list " +
                    "ORDER BY equipment_type ";
        List<WhRequest> hardwareTypeList = new ArrayList<WhRequest>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRequest whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhRequest();
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
    
    public List<WhRequest> getRequestedBy() {
        String sql = "SELECT DISTINCT requested_by " +
                    "FROM hms_wh_request_list " +
                    "ORDER BY requested_by ";
        List<WhRequest> requestedByList = new ArrayList<WhRequest>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRequest whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhRequest();
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
    
    public List<WhRequest> getStatus() {
        String sql = "SELECT DISTINCT status " +
                    "FROM hms_wh_request_list " +
                    "ORDER BY status ";
        List<WhRequest> statusList = new ArrayList<WhRequest>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRequest whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhRequest();
                whRetrieve.setStatus(rs.getString("status"));
                statusList.add(whRetrieve);
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
        return statusList;
    }
    
    public List<WhRequest> getRack() {
        String sql = "SELECT DISTINCT inventory_rack " +
                    "FROM hms_wh_request_list " +
                    "ORDER BY inventory_rack ";
        List<WhRequest> rackList = new ArrayList<WhRequest>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRequest whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhRequest();
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
    
    public List<WhRequest> getShelf() {
        String sql = "SELECT DISTINCT inventory_shelf " +
                    "FROM hms_wh_request_list R " +
                    "ORDER BY inventory_shelf ";
        List<WhRequest> shelfList = new ArrayList<WhRequest>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRequest whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhRequest();
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
    
}