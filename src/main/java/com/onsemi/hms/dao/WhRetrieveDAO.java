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
import com.onsemi.hms.model.WhRetrieve;
import com.onsemi.hms.model.WhRetrieveLog;
import com.onsemi.hms.tools.QueryResult;
import com.onsemi.hms.tools.SpmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhRetrieveDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhRetrieveDAO.class);
    private final Connection conn;
    private final DataSource dataSource;

    public WhRetrieveDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
        this.dataSource = db.getDataSource();
    }

    
    public QueryResult insertWhRetrieve(WhRetrieve whRetrieve) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hms_wh_retrieval_list (retrieve_id, material_pass_no, material_pass_expiry, equipment_type, equipment_id, "
                                                        + "pcb_a, pcb_b, pcb_c, pcb_control,qty_qual_a, qty_qual_b, qty_qual_c, qty_control, quantity, "
                                                        + "requested_by, requested_email, requested_date, remarks, shipping_date, received_date, status, flag) "
                  + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, whRetrieve.getRefId());
            ps.setString(2, whRetrieve.getMaterialPassNo());
            ps.setString(3, whRetrieve.getMaterialPassExpiry());
            ps.setString(4, whRetrieve.getEquipmentType());
            ps.setString(5, whRetrieve.getEquipmentId());
            ps.setString(6, whRetrieve.getPcbA());
            ps.setString(7, whRetrieve.getPcbB());
            ps.setString(8, whRetrieve.getPcbC());
            ps.setString(9, whRetrieve.getPcbControl());
            ps.setString(10, whRetrieve.getQtyQualA());
            ps.setString(11, whRetrieve.getQtyQualB());
            ps.setString(12, whRetrieve.getQtyQualC());
            ps.setString(13, whRetrieve.getQtyControl());
            ps.setString(14, whRetrieve.getQuantity());
            ps.setString(15, whRetrieve.getRequestedBy());
            ps.setString(16, whRetrieve.getRequestedEmail());
            ps.setString(17, whRetrieve.getRequestedDate());
            ps.setString(18, whRetrieve.getRemarks());
            ps.setString(19, whRetrieve.getShippingDate());
            ps.setString(20, whRetrieve.getStatus());
            ps.setString(21, whRetrieve.getFlag());

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
    
    public QueryResult updateWhRetrieveVerification(WhRetrieve whRetrieve) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE hms_wh_retrieval_list SET barcode_verify = ?, date_verify = ?, user_verify = ?, status = ?, flag = ? "
                + "WHERE retrieve_id = ? AND material_pass_no = ? "
            );
            ps.setString(1, whRetrieve.getBarcodeVerify());
            ps.setString(2, whRetrieve.getDateVerify());
            ps.setString(3, whRetrieve.getUserVerify());
            ps.setString(4, whRetrieve.getStatus());
            ps.setString(5, whRetrieve.getFlag());
            ps.setString(6, whRetrieve.getRefId());
            ps.setString(7, whRetrieve.getMaterialPassNo());
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
    
    public QueryResult updateWhRetrieveForInventory(WhRetrieve whRetrieve) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_retrieval_list SET status = ?, flag = ? "
                   + "WHERE retrieve_id = ? AND material_pass_no = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whRetrieve.getStatus());
            ps.setString(2, whRetrieve.getFlag());
            ps.setString(3, whRetrieve.getRefId());
            ps.setString(4, whRetrieve.getMaterialPassNo());
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

    public QueryResult updateWhRetrieveForApproval(WhRetrieve whRetrieve) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_retrieval_list SET status = ?, flag = ?"
                   + "WHERE retrieve_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whRetrieve.getStatus());
            ps.setString(2, whRetrieve.getFlag());
            ps.setString(3, whRetrieve.getRefId());
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
                "SELECT COUNT(*) AS count FROM hms_wh_retrieval_list WHERE retrieve_id = '" + id + "' "
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
    
    public QueryResult deleteWhRetrieve(String whRetrieveId) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM hms_wh_retrieval_list WHERE retrieve_id = '" + whRetrieveId + "'"
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
    
    public WhRetrieve getWhRetrieve(String whRetrieveId) {
        String sql  = "SELECT *,DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                + "             DATE_FORMAT(date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view "
                    + "FROM hms_wh_retrieval_list "
                    + "WHERE retrieve_id = '" + whRetrieveId + "' ";
        WhRetrieve whRetrieve = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhRetrieve();
                whRetrieve.setId(rs.getString("id"));
                whRetrieve.setRefId(rs.getString("retrieve_id"));
                whRetrieve.setMaterialPassNo(rs.getString("material_pass_no"));
                whRetrieve.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whRetrieve.setEquipmentType(rs.getString("equipment_type"));
                whRetrieve.setEquipmentId(rs.getString("equipment_id"));
                whRetrieve.setPcbA(rs.getString("pcb_a"));
                whRetrieve.setQtyQualA(rs.getString("qty_qual_a"));
                whRetrieve.setPcbB(rs.getString("pcb_b"));
                whRetrieve.setQtyQualB(rs.getString("qty_qual_b"));
                whRetrieve.setPcbC(rs.getString("pcb_c"));
                whRetrieve.setQtyQualC(rs.getString("qty_qual_c"));
                whRetrieve.setPcbControl(rs.getString("pcb_control"));
                whRetrieve.setQtyControl(rs.getString("qty_control"));
                whRetrieve.setQuantity(rs.getString("quantity"));
                whRetrieve.setRequestedBy(rs.getString("requested_by"));
                whRetrieve.setRequestedEmail(rs.getString("requested_email"));
                whRetrieve.setRequestedDate(rs.getString("requested_date_view"));
                whRetrieve.setShippingDate(rs.getString("shipping_date_view"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRetrieve.setRemarks(remarks);
                whRetrieve.setBarcodeVerify(rs.getString("barcode_verify"));
                whRetrieve.setDateVerify(rs.getString("date_verify_view"));
                whRetrieve.setUserVerify(rs.getString("user_verify"));
                whRetrieve.setStatus(rs.getString("status"));
                whRetrieve.setFlag(rs.getString("flag"));
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
        return whRetrieve;
    }

    public WhRetrieve getWhRet(String whRetrieveId) {
        String sql  = "SELECT * "
                    + "FROM hms_wh_retrieval_list "
                    + "WHERE retrieve_id = '" + whRetrieveId + "' ";
        WhRetrieve whRetrieve = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhRetrieve();
                whRetrieve.setId(rs.getString("id"));
                whRetrieve.setRefId(rs.getString("retrieve_id"));
                whRetrieve.setMaterialPassNo(rs.getString("material_pass_no"));
                whRetrieve.setMaterialPassExpiry(rs.getString("material_pass_expiry"));
                whRetrieve.setEquipmentType(rs.getString("equipment_type"));
                whRetrieve.setEquipmentId(rs.getString("equipment_id"));
                whRetrieve.setQuantity(rs.getString("quantity"));
                whRetrieve.setRequestedBy(rs.getString("requested_by"));
                whRetrieve.setRequestedEmail(rs.getString("requested_email"));
                whRetrieve.setRequestedDate(rs.getString("requested_date"));
                whRetrieve.setShippingDate(rs.getString("shipping_date"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRetrieve.setRemarks(remarks);
                whRetrieve.setReceivedDate(rs.getString("received_date"));
                whRetrieve.setBarcodeVerify(rs.getString("barcode_verify"));
                whRetrieve.setDateVerify(rs.getString("date_verify"));
                whRetrieve.setUserVerify(rs.getString("user_verify"));
                whRetrieve.setStatus(rs.getString("status"));
                whRetrieve.setFlag(rs.getString("flag"));
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
        return whRetrieve;
    }

    public List<WhRetrieve> getWhRetrieveList() {
        String sql = "SELECT *, DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                   + "DATE_FORMAT(date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view "
                   + "FROM hms_wh_retrieval_list "
                   + "WHERE flag = 0 "
                   + "ORDER BY id DESC ";
        List<WhRetrieve> whRetrieveList = new ArrayList<WhRetrieve>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRetrieve whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhRetrieve();
                whRetrieve.setRefId(rs.getString("retrieve_id"));
                whRetrieve.setMaterialPassNo(rs.getString("material_pass_no"));
                whRetrieve.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whRetrieve.setEquipmentType(rs.getString("equipment_type"));
                whRetrieve.setEquipmentId(rs.getString("equipment_id"));
                whRetrieve.setQuantity(rs.getString("quantity"));
                whRetrieve.setRequestedBy(rs.getString("requested_by"));
                whRetrieve.setRequestedEmail(rs.getString("requested_email"));
                whRetrieve.setRequestedDate(rs.getString("requested_date_view"));
                whRetrieve.setShippingDate(rs.getString("shipping_date_view"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRetrieve.setRemarks(remarks);
                whRetrieve.setBarcodeVerify(rs.getString("barcode_verify"));
                whRetrieve.setDateVerify(rs.getString("date_verify_view"));
                whRetrieve.setUserVerify(rs.getString("user_verify"));
                whRetrieve.setStatus(rs.getString("status"));
                whRetrieve.setFlag(rs.getString("flag"));
                whRetrieveList.add(whRetrieve);
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
        return whRetrieveList;
    }
    
    public List<WhRetrieve> getWhRetrieveReportList() {
        String sql = "SELECT *, CONCAT(FLOOR(HOUR(TIMEDIFF(shipping_date, date_verify)) / 24), ' DAYS, ', MOD(HOUR(TIMEDIFF(shipping_date, date_verify)), 24), ' HOURS, ', MINUTE(TIMEDIFF(shipping_date, date_verify)), ' MINS') AS DURATION "
                   + "FROM hms_wh_retrieval_list "
                   + "WHERE DATE(date_verify) LIKE SUBDATE(DATE(NOW()),1) ";
        List<WhRetrieve> whRetrieveList = new ArrayList<WhRetrieve>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRetrieve whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhRetrieve();
                whRetrieve.setRefId(rs.getString("retrieve_id"));
                whRetrieve.setMaterialPassNo(rs.getString("material_pass_no"));
                whRetrieve.setMaterialPassExpiry(rs.getString("material_pass_expiry"));
                whRetrieve.setEquipmentType(rs.getString("equipment_type"));
                whRetrieve.setEquipmentId(rs.getString("equipment_id"));
                whRetrieve.setQuantity(rs.getString("quantity"));
                whRetrieve.setRequestedBy(rs.getString("requested_by"));
                whRetrieve.setRequestedEmail(rs.getString("requested_email"));
                whRetrieve.setRequestedDate(rs.getString("requested_date"));
                whRetrieve.setShippingDate(rs.getString("shipping_date"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRetrieve.setRemarks(remarks);
                whRetrieve.setReceivedDate(rs.getString("received_date"));
                whRetrieve.setBarcodeVerify(rs.getString("barcode_verify"));
                whRetrieve.setDateVerify(rs.getString("date_verify"));
                whRetrieve.setUserVerify(rs.getString("user_verify"));
                whRetrieve.setStatus(rs.getString("status"));
                whRetrieve.setFlag(rs.getString("flag"));
                whRetrieve.setDuration(rs.getString("duration"));
                whRetrieveList.add(whRetrieve);
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
        return whRetrieveList;
    }
    
    public Integer getCountYesterday() {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(date_verify) AS count " +
                "FROM hms_wh_retrieval_list " +
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
    
    public List<WhRetrieveLog> getWhRetrieveLog(String id) {
        String sql = "SELECT *, "
                   + "FROM hms_wh_log L, hms_wh_retrieval_list R"
                   + "WHERE L.reference_id = R.retrieve_id AND R.retrieve_id = '" + id + "' ";
        List<WhRetrieveLog> whRetrieveList = new ArrayList<WhRetrieveLog>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRetrieveLog whRetrieveLog;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieveLog = new WhRetrieveLog();
                //log
                whRetrieveLog.setId(rs.getString("L.id"));
                whRetrieveLog.setReferenceId(rs.getString("reference_id"));
                whRetrieveLog.setModuleId(rs.getString("module_id"));
                whRetrieveLog.setModuleName(rs.getString("module_name"));
                whRetrieveLog.setLogStatus(rs.getString("L.status"));
                whRetrieveLog.setTimestamp(rs.getString("timestamp"));
                whRetrieveLog.setLogVerifyDate(rs.getString("verified_date"));
                whRetrieveLog.setLogVerifyBy(rs.getString("verified_by"));
                //retrieve
                whRetrieveLog.setRetrieveId(id);
                whRetrieveLog.setMaterialPassNo(rs.getString("material_pass_no"));
                whRetrieveLog.setMaterialPassExpiry(rs.getString("material_pass_expiry"));
                whRetrieveLog.setEquipmentType(rs.getString("equipment_type"));
                whRetrieveLog.setEquipmentId(rs.getString("equipment_id"));
                String pcbA = rs.getString("pcb_A");
                if(pcbA == null || pcbA.equals("null")) {
                    pcbA = SpmlUtil.nullToEmptyString(rs.getString("pcb_A"));
                }
                whRetrieveLog.setPcbA(pcbA);
                whRetrieveLog.setQtyPcbA(rs.getString("qty_pcb_A"));
                String pcbB = rs.getString("pcb_B");
                if(pcbB == null || pcbB.equals("null")) {
                    pcbB = SpmlUtil.nullToEmptyString(rs.getString("pcb_B"));
                }
                whRetrieveLog.setPcbB(pcbB);
                whRetrieveLog.setQtyPcbB(rs.getString("qty_pcb_B"));
                String pcbC = rs.getString("pcb_C");
                if(pcbC == null || pcbC.equals("null")) {
                    pcbC = SpmlUtil.nullToEmptyString(rs.getString("pcb_C"));
                }
                whRetrieveLog.setPcbC(pcbC);
                whRetrieveLog.setQtyPcbC(rs.getString("qty_pcb_C"));
                String pcbControl = rs.getString("pcb_Control");
                if(pcbControl == null || pcbControl.equals("null")) {
                    pcbControl = SpmlUtil.nullToEmptyString(rs.getString("pcb_Control"));
                }
                whRetrieveLog.setPcbControl(pcbControl);
                whRetrieveLog.setQtyPcbControl(rs.getString("qty_pcb_Control"));
                whRetrieveLog.setQuantity(rs.getString("quantity"));
                whRetrieveLog.setRequestedBy(rs.getString("requested_by"));
                whRetrieveLog.setRequestedEmail(rs.getString("requested_email"));
                whRetrieveLog.setRequestedDate(rs.getString("requested_date"));
                whRetrieveLog.setShippingDate(rs.getString("shipping_date"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRetrieveLog.setRemarks(remarks);
                whRetrieveLog.setReceivedDate(rs.getString("received_date"));
                whRetrieveLog.setBarcodeVerify(rs.getString("barcode_verify"));
                whRetrieveLog.setDateVerify(rs.getString("date_verify"));
                whRetrieveLog.setUserVerify(rs.getString("user_verify"));
                whRetrieveLog.setStatus(rs.getString("R.status"));
                whRetrieveLog.setFlag(rs.getString("flag"));
                whRetrieveList.add(whRetrieveLog);
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
        return whRetrieveList;
    }
}