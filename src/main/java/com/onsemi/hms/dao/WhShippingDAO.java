//package com.onsemi.hms.dao;
//
//import com.onsemi.hms.db.DB;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//import javax.sql.DataSource;
//import com.onsemi.hms.model.WhShipping;
//import com.onsemi.hms.tools.QueryResult;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//
//public class WhShippingDAO {
//    private static final Logger LOGGER = LoggerFactory.getLogger(WhShippingDAO.class);
//    private final Connection conn;
//    private final DataSource dataSource;
//
//    public WhShippingDAO() {
//        DB db = new DB();
//        this.conn = db.getConnection();
//        this.dataSource = db.getDataSource();
//    }
//
//    public QueryResult insertWhShipping(WhShipping whShipping) {
//        QueryResult queryResult = new QueryResult();
//        try {
//            PreparedStatement ps = conn.prepareStatement(
//                "INSERT INTO hms_wh_shipping_list (request_id, material_pass_no, shipping_date, inventory_rack, inventory_slot, shipping_by, status, flag) "
//              + "VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS
//            );
//            ps.setString(1, whShipping.getRefId());
//            ps.setString(2, whShipping.getMaterialPassNo());
//            ps.setString(3, whShipping.getInventoryDate());
//            ps.setString(4, whShipping.getInventoryRack());
//            ps.setString(5, whShipping.getInventorySlot());
//            ps.setString(6, whShipping.getInventoryBy());
//            ps.setString(7, whShipping.getStatus());
//            ps.setString(8, whShipping.getFlag());
//            queryResult.setResult(ps.executeUpdate());
//            ResultSet rs = ps.getGeneratedKeys();
//            if (rs.next()) {
//                queryResult.setGeneratedKey(Integer.toString(rs.getInt(1)));
//            }
//            rs.close();
//            ps.close();
//        } catch (SQLException e) {
//            queryResult.setErrorMessage(e.getMessage());
//            LOGGER.error(e.getMessage());
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    LOGGER.error(e.getMessage());
//                }
//            }
//        }
//        return queryResult;
//    }
//
//    public QueryResult updateWhShipping(WhShipping whShipping) {
//        QueryResult queryResult = new QueryResult();
//        String sql = "UPDATE hms_wh_shipping_list "
//                   + "SET shipping_date = ?, inventory_rack = ?, inventory_slot = ?, shipping_by = ? "
//                   + "WHERE request_id = ?";
//        try {
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setString(1, whShipping.getInventoryDate());
//            ps.setString(2, whShipping.getInventoryRack());
//            ps.setString(3, whShipping.getInventorySlot());
//            ps.setString(4, whShipping.getInventoryBy());
//            ps.setString(5, whShipping.getRefId());
//            queryResult.setResult(ps.executeUpdate());
//            ps.close();
//        } catch (SQLException e) {
//            queryResult.setErrorMessage(e.getMessage());
//            LOGGER.error(e.getMessage());
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    LOGGER.error(e.getMessage());
//                }
//            }
//        }
//        return queryResult;
//    }
//
//    public QueryResult deleteWhShipping(String whShippingId) {
//        QueryResult queryResult = new QueryResult();
//        try {
//            PreparedStatement ps = conn.prepareStatement(
//                "DELETE FROM hms_wh_shipping_list WHERE request_id = '" + whShippingId + "'"
//            );
//            queryResult.setResult(ps.executeUpdate());
//            ps.close();
//        } catch (SQLException e) {
//            queryResult.setErrorMessage(e.getMessage());
//            LOGGER.error(e.getMessage());
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    LOGGER.error(e.getMessage());
//                }
//            }
//        }
//        return queryResult;
//    }
//
//    public WhShipping getWhShipping(String whShippingId) {
//        String sql = "SELECT * "
//                   + "FROM hms_wh_shipping_list "
//                   + "WHERE request_id = '" + whShippingId + "'";
//        WhShipping whShipping = null;
//        try {
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                whShipping = new WhShipping();
//                whShipping.setRefId(rs.getString("request_id"));
//                whShipping.setMaterialPassNo(rs.getString("material_pass_no"));
//                whShipping.setStatus(rs.getString("status"));
//                whShipping.setFlag(rs.getString("flag"));                
//            }
//            rs.close();
//            ps.close();
//        } catch (SQLException e) {
//            LOGGER.error(e.getMessage());
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    LOGGER.error(e.getMessage());
//                }
//            }
//        }
//        return whShipping;
//    }
//        
//    public WhShipping getWhShippingMergeWithRequest(String whShippingId) {
//        String sql = "SELECT IL.*, RL.*, DATE_FORMAT(RL.material_pass_expiry,'%d %M %Y') AS mp_expiry_view , DATE_FORMAT(RL.requested_date,'%d %M %Y') AS requested_date_view, "
//                   + "DATE_FORMAT(RL.date_verify,'%d %M %Y') AS date_verify_view, DATE_FORMAT(IL.shipping_date,'%d %M %Y') AS shipping_date_view "
//                   + "FROM hms_wh_shipping_list IL, hms_wh_retrieval_list RL "
//                   + "WHERE IL.request_id = RL.ref_id AND IL.request_id = '" + whShippingId + "' ";
//        WhShipping whShipping = null;
//        try {
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                whShipping = new WhShipping();
//                whShipping.setRefId(rs.getString("IL.request_id"));
//                whShipping.setMaterialPassNo(rs.getString("IL.material_pass_no"));
//                whShipping.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
//                whShipping.setEquipmentType(rs.getString("RL.equipment_type"));
//                whShipping.setEquipmentId(rs.getString("RL.equipment_id"));
//                whShipping.setType(rs.getString("RL.type"));
//                whShipping.setQuantity(rs.getString("RL.quantity"));
//                whShipping.setRemarks(rs.getString("RL.remarks"));
//                whShipping.setRequestedBy(rs.getString("RL.requested_by"));
//                whShipping.setRequestedDate(rs.getString("requested_date_view"));
//                whShipping.setBarcodeVerify(rs.getString("RL.barcode_verify"));
//                whShipping.setDateVerify(rs.getString("date_verify_view"));
//                whShipping.setUserVerify(rs.getString("RL.user_verify"));
//                whShipping.setInventoryDate(rs.getString("shipping_date_view"));
//                whShipping.setInventoryRack(rs.getString("IL.inventory_rack"));
//                whShipping.setInventorySlot(rs.getString("IL.inventory_slot"));
//                whShipping.setStatus(rs.getString("IL.status"));
//                whShipping.setFlag(rs.getString("IL.flag"));
//            }
//            rs.close();
//            ps.close();
//        } catch (SQLException e) {
//            LOGGER.error(e.getMessage());
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    LOGGER.error(e.getMessage());
//                }
//            }
//        }
//        return whShipping;
//    }
//    
//    public WhShipping getWhShippingMergeWithRequestPdf(String whShippingId) {
//        String sql = "SELECT IL.*, RL.* "
//                   + "FROM hms_wh_shipping_list IL, hms_wh_retrieval_list RL "
//                   + "WHERE IL.request_id = RL.ref_id AND IL.request_id = '" + whShippingId + "' ";
//        WhShipping whShipping = null;
//        try {
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                whShipping = new WhShipping();
//                whShipping.setRefId(rs.getString("IL.request_id"));
//                whShipping.setMaterialPassNo(rs.getString("IL.material_pass_no"));
//                whShipping.setMaterialPassExpiry(rs.getString("RL.material_pass_expiry"));
//                whShipping.setEquipmentType(rs.getString("RL.equipment_type"));
//                whShipping.setEquipmentId(rs.getString("RL.equipment_id"));
//                whShipping.setType(rs.getString("RL.type"));
//                whShipping.setQuantity(rs.getString("RL.quantity"));
//                whShipping.setRemarks(rs.getString("RL.remarks"));
//                whShipping.setRequestedBy(rs.getString("RL.requested_by"));
//                whShipping.setRequestedDate(rs.getString("RL.requested_date"));
//                whShipping.setBarcodeVerify(rs.getString("RL.barcode_verify"));
//                whShipping.setDateVerify(rs.getString("RL.date_verify"));
//                whShipping.setUserVerify(rs.getString("RL.user_verify"));
//                whShipping.setInventoryDate(rs.getString("IL.shipping_date"));
//                whShipping.setInventoryRack(rs.getString("IL.inventory_rack"));
//                whShipping.setInventorySlot(rs.getString("IL.inventory_slot"));
//                whShipping.setStatus(rs.getString("IL.status"));
//                whShipping.setFlag(rs.getString("IL.flag"));
//            }
//            rs.close();
//            ps.close();
//        } catch (SQLException e) {
//            LOGGER.error(e.getMessage());
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    LOGGER.error(e.getMessage());
//                }
//            }
//        }
//        return whShipping;
//    }
//
//    public List<WhShipping> getWhShippingListMergeRequest() {
//        String sql = "SELECT IL.*, RL.*, DATE_FORMAT(RL.material_pass_expiry,'%d %M %Y') AS mp_expiry_view , DATE_FORMAT(RL.requested_date,'%d %M %Y') AS requested_date_view, "
//                   + "DATE_FORMAT(RL.date_verify,'%d %M %Y') AS date_verify_view, DATE_FORMAT(IL.shipping_date,'%d %M %Y') AS shipping_date_view "
//                   + "FROM hms_wh_shipping_list IL, hms_wh_retrieval_list RL "
//                   + "WHERE IL.request_id = RL.ref_id ";
//        List<WhShipping> whShippingList = new ArrayList<WhShipping>();
//        try {
//            PreparedStatement ps = conn.prepareStatement(sql);
//            WhShipping whShipping;
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                whShipping = new WhShipping();
//                whShipping.setRefId(rs.getString("IL.request_id"));
//                whShipping.setMaterialPassNo(rs.getString("IL.material_pass_no"));
//                whShipping.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
//                whShipping.setEquipmentType(rs.getString("RL.equipment_type"));
//                whShipping.setEquipmentId(rs.getString("RL.equipment_id"));
//                whShipping.setType(rs.getString("RL.type"));
//                whShipping.setQuantity(rs.getString("RL.quantity"));
//                whShipping.setRemarks(rs.getString("RL.remarks"));
//                whShipping.setRequestedBy(rs.getString("RL.requested_by"));
//                whShipping.setRequestedDate(rs.getString("requested_date_view"));
//                whShipping.setBarcodeVerify(rs.getString("RL.barcode_verify"));
//                whShipping.setDateVerify(rs.getString("date_verify_view"));
//                whShipping.setUserVerify(rs.getString("RL.user_verify"));
//                whShipping.setInventoryDate(rs.getString("shipping_date_view"));
//                whShipping.setInventoryRack(rs.getString("IL.inventory_rack"));
//                whShipping.setInventorySlot(rs.getString("IL.inventory_slot"));
//                whShipping.setStatus(rs.getString("IL.status"));
//                whShipping.setFlag(rs.getString("IL.flag"));
//                whShippingList.add(whShipping);
//            }
//            rs.close();
//            ps.close();
//        } catch (SQLException e) {
//            LOGGER.error(e.getMessage());
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    LOGGER.error(e.getMessage());
//                }
//            }
//        }
//        return whShippingList;
//    }
//    
//    public Integer getCountExistingData(String id) {
//        Integer count = null;
//        try {
//            PreparedStatement ps = conn.prepareStatement(
//                "SELECT COUNT(*) AS count FROM hms_wh_shipping_list WHERE request_id = '" + id + "' "
//            );
//
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                count = rs.getInt("count");
//            }
//            rs.close();
//            ps.close();
//        } catch (SQLException e) {
//            LOGGER.error(e.getMessage());
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    LOGGER.error(e.getMessage());
//                }
//            }
//        }
//        return count;
//    }
//}