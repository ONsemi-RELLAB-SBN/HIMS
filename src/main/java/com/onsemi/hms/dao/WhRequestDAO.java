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
                    "INSERT INTO hms_wh_request_list (ref_id, material_pass_no, material_pass_expiry, equipment_type, equipment_id, quantity, "
                                                    + "requested_by, requested_email, requested_date, inventory_loc, remarks, received_date, status, flag) "
                  + "VALUES (?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, whRequest.getRefId());
            ps.setString(2, whRequest.getMaterialPassNo());
            ps.setString(3, whRequest.getMaterialPassExpiry());
            ps.setString(4, whRequest.getEquipmentType());
            ps.setString(5, whRequest.getEquipmentId());
            ps.setString(6, whRequest.getQuantity());
            ps.setString(7, whRequest.getRequestedBy());
            ps.setString(8, whRequest.getRequestedEmail());
            ps.setString(9, whRequest.getRequestedDate());
            ps.setString(10, whRequest.getInventoryLoc());
            ps.setString(11, whRequest.getRemarks());
            ps.setString(12, whRequest.getStatus());
            ps.setString(13, whRequest.getFlag());

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
        try {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE hms_wh_request_list SET barcode_verify = ?, date_verify = ?, user_verify = ?, status = ?, flag = ? "
             + "WHERE ref_id = ? AND material_pass_no = ? "
            );
            ps.setString(1, whRequest.getBarcodeVerify());
            ps.setString(2, whRequest.getDateVerify());
            ps.setString(3, whRequest.getUserVerify());
            ps.setString(4, whRequest.getStatus());
            ps.setString(5, whRequest.getFlag());
            ps.setString(6, whRequest.getRefId());
            ps.setString(7, whRequest.getMaterialPassNo());
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
        String sql = "UPDATE hms_wh_request_list SET barcode_verify = ?, user_verify = ?, date_verify = ?, status = ?, flag = ? "
                   + "WHERE ref_id = ? AND material_pass_no = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whRequest.getBarcodeVerify());
            ps.setString(2, whRequest.getUserVerify());
            ps.setString(3, whRequest.getDateVerify());
            ps.setString(4, whRequest.getStatus());
            ps.setString(5, whRequest.getFlag());
            ps.setString(6, whRequest.getRefId());
            ps.setString(7, whRequest.getMaterialPassNo());
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
                   + "WHERE ref_id = ?";
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
        String sql = "UPDATE hms_wh_request_list SET status = ?"
                   + "WHERE ref_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whRequest.getStatus());
            ps.setString(2, whRequest.getRefId());
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
                "SELECT COUNT(*) AS count FROM hms_wh_request_list WHERE ref_id = '" + id + "' "
            );

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("count");
            }
//            LOGGER.info("count id..........." + count.toString());
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
                    "DELETE FROM hms_wh_request_list WHERE ref_id = '" + whRequestId + "'"
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

    public WhRequest getWhRequest(String whRequestId) {
        String sql  = "SELECT *,DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y') AS requested_date_view, DATE_FORMAT(date_verify,'%d %M %Y') AS date_verify_view "
                    + "FROM hms_wh_request_list "
                    + "WHERE ref_id = '" + whRequestId + "' ";
        WhRequest whRequest = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRequest = new WhRequest();
                whRequest.setRefId(rs.getString("ref_id"));
                whRequest.setMaterialPassNo(rs.getString("material_pass_no"));
                whRequest.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whRequest.setEquipmentType(rs.getString("equipment_type"));
                whRequest.setEquipmentId(rs.getString("equipment_id"));
                whRequest.setQuantity(rs.getString("quantity"));
                whRequest.setRequestedBy(rs.getString("requested_by"));
                whRequest.setRequestedEmail(rs.getString("requested_email"));
                whRequest.setRequestedDate(rs.getString("requested_date_view"));
                whRequest.setInventoryLoc(rs.getString("inventory_loc"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRequest.setRemarks(remarks);
                whRequest.setBarcodeVerify(rs.getString("barcode_verify"));
                whRequest.setDateVerify(rs.getString("date_verify_view"));
                whRequest.setUserVerify(rs.getString("user_verify"));
                whRequest.setStatus(rs.getString("status"));
                whRequest.setFlag(rs.getString("flag"));
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
        String sql = "SELECT *, DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y') AS requested_date_view, DATE_FORMAT(date_verify,'%d %M %Y') AS date_verify_view "
                   + "FROM hms_wh_request_list "
                   + "WHERE status NOT LIKE 'Queue for Shipping' and status NOT LIKE 'Ship' "
                   + "ORDER BY id DESC";
        List<WhRequest> whRequestList = new ArrayList<WhRequest>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRequest whRequest;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRequest = new WhRequest();
                whRequest.setRefId(rs.getString("ref_id"));
                whRequest.setMaterialPassNo(rs.getString("material_pass_no"));
                whRequest.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whRequest.setEquipmentType(rs.getString("equipment_type"));
                whRequest.setEquipmentId(rs.getString("equipment_id"));
                whRequest.setQuantity(rs.getString("quantity"));
                whRequest.setRequestedBy(rs.getString("requested_by"));
                whRequest.setRequestedEmail(rs.getString("requested_email"));
                whRequest.setRequestedDate(rs.getString("requested_date_view"));
                whRequest.setInventoryLoc(rs.getString("inventory_loc"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRequest.setRemarks(remarks);
                whRequest.setBarcodeVerify(rs.getString("barcode_verify"));
                whRequest.setDateVerify(rs.getString("date_verify_view"));
                whRequest.setUserVerify(rs.getString("user_verify"));
                whRequest.setStatus(rs.getString("status"));
                whRequest.setFlag(rs.getString("flag"));
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
                whRequest.setRefId(rs.getString("ref_id"));
                whRequest.setMaterialPassNo(rs.getString("material_pass_no"));
                whRequest.setMaterialPassExpiry(rs.getString("material_pass_expiry"));
                whRequest.setEquipmentType(rs.getString("equipment_type"));
                whRequest.setEquipmentId(rs.getString("equipment_id"));
                whRequest.setQuantity(rs.getString("quantity"));
                whRequest.setRequestedBy(rs.getString("requested_by"));
                whRequest.setRequestedEmail(rs.getString("requested_email"));
                whRequest.setRequestedDate(rs.getString("requested_date"));
                whRequest.setInventoryLoc(rs.getString("inventory_loc"));
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
}