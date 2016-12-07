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
import com.onsemi.hms.model.WhMpList;
import com.onsemi.hms.tools.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhMpListDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhMpListDAO.class);
    private final Connection conn;
    private final DataSource dataSource;

    public WhMpListDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
        this.dataSource = db.getDataSource();
    }

    public QueryResult insertWhMpList(WhMpList whMpList) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO hms_wh_mp_list (request_id, shipping_id, material_pass_no, created_by, created_date, status) VALUES (?,?,?,?,NOW(),?)", Statement.RETURN_GENERATED_KEYS
            );
            
            ps.setString(1, whMpList.getRequestId());
            ps.setString(2, whMpList.getShippingId());
            ps.setString(3, whMpList.getMaterialPassNo());
            ps.setString(4, whMpList.getCreatedBy());
            ps.setString(5, whMpList.getStatus());
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

    public QueryResult updateWhMpList(WhMpList whMpList) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE hms_wh_mp_list SET created_by = ?, created_date = NOW(), status = ? WHERE shipping_id = ?"
            );
            ps.setString(1, whMpList.getCreatedBy());
            ps.setString(2, whMpList.getStatus());
            ps.setString(3, whMpList.getShippingId());
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

    public QueryResult deleteWhMpList(String whMpListId) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM hms_wh_mp_list WHERE shipping_id = '" + whMpListId + "'"
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

    public WhMpList getWhMpListMergeWithShippingAndRequest(String whMpListId) {
        String sql = "SELECT ML.*, RL.*, SL.*, DATE_FORMAT(RL.material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(RL.requested_date,'%d %M %Y') AS requested_date_view "
                   + "FROM hms_wh_mp_list ML, hms_wh_request_list RL, hms_wh_shipping_list SL "
                   + "WHERE RL.request_id = SL.request_id AND SL.id = ML.shipping_id AND ML.request_id = '" + whMpListId + "'";
        WhMpList whMpList = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whMpList = new WhMpList();
                whMpList.setRequestId(rs.getString("ML.request_id"));
                whMpList.setShippingId(rs.getString("ML.shipping_id"));
                whMpList.setMaterialPassNo(rs.getString("ML.material_pass_no"));
                whMpList.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whMpList.setEquipmentType(rs.getString("RL.equipment_type"));
                whMpList.setEquipmentId(rs.getString("RL.equipment_id"));
                whMpList.setQuantity(rs.getString("RL.quantity"));
                whMpList.setRequestedBy(rs.getString("RL.requested_by"));
                whMpList.setRequestedEmail(rs.getString("RL.requested_email"));
                whMpList.setRequestedDate(rs.getString("requested_date_view"));
                whMpList.setRemarks(rs.getString("RL.remarks"));
                whMpList.setUserVerify(rs.getString("RL.user_verify"));
                whMpList.setDateVerify(rs.getString("RL.date_verify"));
                whMpList.setInventoryLoc(rs.getString("RL.inventory_loc"));
                whMpList.setCreatedDate(rs.getString("ML.created_date"));
                whMpList.setCreatedBy(rs.getString("ML.created_by"));
                whMpList.setShippingDate(rs.getString("SL.shipping_date"));
                whMpList.setShippingBy(rs.getString("SL.shipping_by"));
                whMpList.setStatus(rs.getString("SL.status"));                
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
        return whMpList;
    }

    public List<WhMpList> getWhMpListMergeWithShippingAndRequestList() {
        String sql = "SELECT ML.*, RL.*, SL.*, DATE_FORMAT(RL.material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(RL.requested_date,'%d %M %Y') AS requested_date_view, "
                   + "DATE_FORMAT(ML.created_date,'%d %M %Y') AS created_date_view, DATE_FORMAT(SL.shipping_date,'%d %M %Y') AS shipping_date_view "
                   + "FROM hms_wh_mp_list ML, hms_wh_request_list RL, hms_wh_shipping_list SL "
                   + "WHERE RL.request_id = SL.request_id AND SL.request_id = ML.request_id AND (SL.flag = '1' OR SL.flag = '0') " 
                   + "ORDER BY ML.shipping_id ASC ";
        List<WhMpList> whMpListList = new ArrayList<WhMpList>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhMpList whMpList;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whMpList = new WhMpList();
                whMpList.setRequestId(rs.getString("ML.request_id"));
                whMpList.setShippingId(rs.getString("ML.shipping_id"));
                whMpList.setMaterialPassNo(rs.getString("ML.material_pass_no"));
                whMpList.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whMpList.setEquipmentId(rs.getString("RL.equipment_id"));
                whMpList.setEquipmentType(rs.getString("RL.equipment_type"));
                whMpList.setReasonRetrieval(rs.getString("reason_retrieval"));
                whMpList.setQuantity(rs.getString("RL.quantity"));
                whMpList.setInventoryLoc(rs.getString("RL.inventory_loc"));
                whMpList.setRequestedBy(rs.getString("RL.requested_by"));
                whMpList.setRequestedEmail(rs.getString("RL.requested_email"));
                whMpList.setRequestedDate(rs.getString("requested_date_view"));
                whMpList.setCreatedDate(rs.getString("created_date_view"));
                whMpList.setCreatedBy(rs.getString("ML.created_by"));
                whMpList.setShippingDate(rs.getString("shipping_date_view"));
                whMpList.setShippingBy(rs.getString("SL.shipping_by"));
                whMpList.setStatus(rs.getString("SL.status"));
                whMpListList.add(whMpList);
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
        return whMpListList;
    }
    
    public List<WhMpList> getWhMpListEmail() {
        String sql = "SELECT DISTINCT RL.requested_email "
                   + "FROM hms_wh_mp_list ML, hms_wh_request_list RL, hms_wh_shipping_list SL "
                   + "WHERE RL.request_id = SL.request_id AND SL.request_id = ML.shipping_id " 
                   + "ORDER BY ML.shipping_id ASC ";
        List<WhMpList> whMpListList = new ArrayList<WhMpList>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhMpList whMpList;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whMpList = new WhMpList();
                whMpList.setRequestedEmail(rs.getString("RL.requested_email"));
                whMpListList.add(whMpList);
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
        return whMpListList;
    }

    public QueryResult deleteAllWhMpList() {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM hms_wh_mp_list "
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
    
    public Integer getCountMpNo(String mpNo) {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) AS count FROM hms_wh_mp_list WHERE material_pass_no = '" + mpNo + "'"
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
    
    public Integer getCount() {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) AS count FROM hms_wh_mp_list"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("count");
            }
            LOGGER.info("count qty..........." + count.toString());
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