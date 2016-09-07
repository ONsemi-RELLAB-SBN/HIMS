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
                    "INSERT INTO hms_wh_retrieval_list (ref_id, material_pass_no, material_pass_expiry, equipment_type, equipment_id, quantity, "
                                                        + "requested_by, requested_email, requested_date, remarks, shipping_date, received_date, status, flag) "
                  + "VALUES (?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, whRetrieve.getRefId());
            ps.setString(2, whRetrieve.getMaterialPassNo());
            ps.setString(3, whRetrieve.getMaterialPassExpiry());
            ps.setString(4, whRetrieve.getEquipmentType());
            ps.setString(5, whRetrieve.getEquipmentId());
            ps.setString(6, whRetrieve.getQuantity());
            ps.setString(7, whRetrieve.getRequestedBy());
            ps.setString(8, whRetrieve.getRequestedEmail());
            ps.setString(9, whRetrieve.getRequestedDate());
            ps.setString(10, whRetrieve.getRemarks());
            ps.setString(11, whRetrieve.getShippingDate());
            ps.setString(12, whRetrieve.getStatus());
            ps.setString(13, whRetrieve.getFlag());

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
                + "WHERE ref_id = ? AND material_pass_no = ? "
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
                   + "WHERE ref_id = ? AND material_pass_no = ? ";
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
                   + "WHERE ref_id = ?";
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
                "SELECT COUNT(*) AS count FROM hms_wh_retrieval_list WHERE ref_id = '" + id + "' "
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
                    "DELETE FROM hms_wh_retrieval_list WHERE ref_id = '" + whRetrieveId + "'"
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
        String sql  = "SELECT *,DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y') AS requested_date_view, "
                + "             DATE_FORMAT(date_verify,'%d %M %Y') AS date_verify_view, DATE_FORMAT(shipping_date,'%d %M %Y') AS shipping_date_view "
                    + "FROM hms_wh_retrieval_list "
                    + "WHERE ref_id = '" + whRetrieveId + "' ";
        WhRetrieve whRetrieve = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhRetrieve();
                whRetrieve.setRefId(rs.getString("ref_id"));
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
        String sql = "SELECT *, DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y') AS requested_date_view, "
                   + "DATE_FORMAT(date_verify,'%d %M %Y') AS date_verify_view, DATE_FORMAT(shipping_date,'%d %M %Y') AS shipping_date_view "
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
                whRetrieve.setRefId(rs.getString("ref_id"));
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
        String sql = "SELECT * "
                   + "FROM hms_wh_retrieval_list "
                   + "WHERE DATE(date_verify) LIKE SUBDATE(DATE(NOW()),1) ";
        List<WhRetrieve> whRetrieveList = new ArrayList<WhRetrieve>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRetrieve whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhRetrieve();
                whRetrieve.setRefId(rs.getString("ref_id"));
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
}