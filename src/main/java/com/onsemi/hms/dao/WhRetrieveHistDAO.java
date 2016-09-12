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
import com.onsemi.hms.model.WhRetrieveHist;
import com.onsemi.hms.tools.QueryResult;
import com.onsemi.hms.tools.SpmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhRetrieveHistDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhRetrieveHistDAO.class);
    private final Connection conn;
    private final DataSource dataSource;

    public WhRetrieveHistDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
        this.dataSource = db.getDataSource();
    }

    public QueryResult insertWhRetrieveHist(WhRetrieveHist whRetrieveHist) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hms_wh_retrieval_history (retrieve_id, material_pass_no, material_pass_expiry, equipment_type, equipment_id, quantity, "
                                                      + "requested_by, requested_email, requested_date, shipping_date, remarks, received_date, "
                                                      + "barcode_verify, user_verify, date_verify, status, flag, updated_date, updated_by) "
                            
                  + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?)", Statement.RETURN_GENERATED_KEYS  //17
            );
            ps.setString(1, whRetrieveHist.getRefId());
            ps.setString(2, whRetrieveHist.getMaterialPassNo());
            ps.setString(3, whRetrieveHist.getMaterialPassExpiry());
            ps.setString(4, whRetrieveHist.getEquipmentType());
            ps.setString(5, whRetrieveHist.getEquipmentId());
            ps.setString(6, whRetrieveHist.getQuantity());
            ps.setString(7, whRetrieveHist.getRequestedBy());
            ps.setString(8, whRetrieveHist.getRequestedEmail());
            ps.setString(9, whRetrieveHist.getRequestedDate());
            ps.setString(10, whRetrieveHist.getShippingDate());
            ps.setString(11, whRetrieveHist.getRemarks());
            ps.setString(12, whRetrieveHist.getReceivedDate());
            ps.setString(13, whRetrieveHist.getBarcodeVerify());
            ps.setString(14, whRetrieveHist.getUserVerify());
            ps.setString(15, whRetrieveHist.getDateVerify());
            ps.setString(16, whRetrieveHist.getStatus());
            ps.setString(17, whRetrieveHist.getFlag());
            ps.setString(18, whRetrieveHist.getUpdatedBy());

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
 
    public WhRetrieveHist getWhRetrieveHist(String whRetrieveHistId) {
        String sql  = "SELECT *,DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y') AS requested_date_view, "
                + "             DATE_FORMAT(date_verify,'%d %M %Y') AS date_verify_view, DATE_FORMAT(shipping_date,'%d %M %Y') AS shipping_date_view "
                    + "FROM hms_wh_retrieval_list "
                    + "WHERE ref_id = '" + whRetrieveHistId + "' ";
        WhRetrieveHist whRetrieveHist = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieveHist = new WhRetrieveHist();
                whRetrieveHist.setRefId(rs.getString("ref_id"));
                whRetrieveHist.setMaterialPassNo(rs.getString("material_pass_no"));
                whRetrieveHist.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whRetrieveHist.setEquipmentType(rs.getString("equipment_type"));
                whRetrieveHist.setEquipmentId(rs.getString("equipment_id"));
                whRetrieveHist.setQuantity(rs.getString("quantity"));
                whRetrieveHist.setRequestedBy(rs.getString("requested_by"));
                whRetrieveHist.setRequestedEmail(rs.getString("requested_email"));
                whRetrieveHist.setRequestedDate(rs.getString("requested_date_view"));
                whRetrieveHist.setShippingDate(rs.getString("shipping_date_view"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRetrieveHist.setRemarks(remarks);
                whRetrieveHist.setBarcodeVerify(rs.getString("barcode_verify"));
                whRetrieveHist.setDateVerify(rs.getString("date_verify_view"));
                whRetrieveHist.setUserVerify(rs.getString("user_verify"));
                whRetrieveHist.setStatus(rs.getString("status"));
                whRetrieveHist.setFlag(rs.getString("flag"));
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
        return whRetrieveHist;
    }

    public List<WhRetrieveHist> getWhRetrieveHistList() {
        String sql = "SELECT *, DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y') AS requested_date_view, "
                   + "DATE_FORMAT(date_verify,'%d %M %Y') AS date_verify_view, DATE_FORMAT(shipping_date,'%d %M %Y') AS shipping_date_view "
                   + "FROM hms_wh_retrieval_list "
                   + "WHERE flag = 0 "
                   + "ORDER BY id DESC ";
        List<WhRetrieveHist> whRetrieveHistList = new ArrayList<WhRetrieveHist>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRetrieveHist whRetrieveHist;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieveHist = new WhRetrieveHist();
                whRetrieveHist.setRefId(rs.getString("ref_id"));
                whRetrieveHist.setMaterialPassNo(rs.getString("material_pass_no"));
                whRetrieveHist.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whRetrieveHist.setEquipmentType(rs.getString("equipment_type"));
                whRetrieveHist.setEquipmentId(rs.getString("equipment_id"));
                whRetrieveHist.setQuantity(rs.getString("quantity"));
                whRetrieveHist.setRequestedBy(rs.getString("requested_by"));
                whRetrieveHist.setRequestedEmail(rs.getString("requested_email"));
                whRetrieveHist.setRequestedDate(rs.getString("requested_date_view"));
                whRetrieveHist.setShippingDate(rs.getString("shipping_date_view"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRetrieveHist.setRemarks(remarks);
                whRetrieveHist.setBarcodeVerify(rs.getString("barcode_verify"));
                whRetrieveHist.setDateVerify(rs.getString("date_verify_view"));
                whRetrieveHist.setUserVerify(rs.getString("user_verify"));
                whRetrieveHist.setStatus(rs.getString("status"));
                whRetrieveHist.setFlag(rs.getString("flag"));
                whRetrieveHistList.add(whRetrieveHist);
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
        return whRetrieveHistList;
    }
}