package com.onsemi.hms.dao;

import com.onsemi.hms.db.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import com.onsemi.hms.model.WhInventoryLog;
import com.onsemi.hms.model.WhUSL;
import com.onsemi.hms.tools.SpmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WhUSLDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhUSLDAO.class);
    private final Connection conn;
    private final DataSource dataSource;

    public WhUSLDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
        this.dataSource = db.getDataSource();
    }

    public Integer getCountUSLRet() {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(id) AS count " +
                "FROM hms_wh_retrieval_list " +
                "WHERE retrieve_id "
            );

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("count");
            }
            LOGGER.info("count ID ..........." + count.toString());
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
    
    public List<WhUSL> getWhUSLLog() {
        String sql  = "SELECT *, DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, DATE_FORMAT(shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view, " +
                    "DATE_FORMAT(arrival_received_date,'%d %M %Y %h:%i %p') AS arrival_received_date_view, DATE_FORMAT(received_date,'%d %M %Y %h:%i %p') AS received_date_view, DATE_FORMAT(date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(temp_date,'%d %M %Y %h:%i %p') AS temp_date_view, " +
                    "CONCAT(FLOOR(HOUR(TIMEDIFF(requested_date, shipping_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(requested_date, shipping_date)), 24), ' hours, ', MINUTE(TIMEDIFF(requested_date, shipping_date)), ' mins, ', SECOND(TIMEDIFF(requested_date, shipping_date)), ' secs') AS req_shp1, " +
                    "CONCAT(FLOOR(HOUR(TIMEDIFF(requested_date, IFNULL(shipping_date,NOW()))) / 24), ' days, ', MOD(HOUR(TIMEDIFF(requested_date, IFNULL(shipping_date,NOW()))), 24), ' hours, ', MINUTE(TIMEDIFF(requested_date, IFNULL(shipping_date,NOW()))), ' mins, ', SECOND(TIMEDIFF(requested_date, IFNULL(shipping_date,NOW()))), ' secs') AS req_shp2, " +
                    "CONCAT(FLOOR(HOUR(TIMEDIFF(shipping_date, arrival_received_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(shipping_date, arrival_received_date)), 24), ' hours, ', MINUTE(TIMEDIFF(shipping_date, arrival_received_date)), ' mins, ', SECOND(TIMEDIFF(shipping_date, arrival_received_date)), ' secs') AS shp_arr1, " +
                    "CONCAT(FLOOR(HOUR(TIMEDIFF(shipping_date, IFNULL(arrival_received_date,NOW()))) / 24), ' days, ', MOD(HOUR(TIMEDIFF(shipping_date, IFNULL(arrival_received_date,NOW()))), 24), ' hours, ', MINUTE(TIMEDIFF(shipping_date, IFNULL(arrival_received_date,NOW()))), ' mins, ', SECOND(TIMEDIFF(shipping_date, IFNULL(arrival_received_date,NOW()))), ' secs') AS shp_arr2, " +
                    "CONCAT(FLOOR(HOUR(TIMEDIFF(arrival_received_date, temp_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(arrival_received_date, temp_date)), 24), ' hours, ', MINUTE(TIMEDIFF(arrival_received_date, temp_date)), ' mins, ', SECOND(TIMEDIFF(arrival_received_date, temp_date)), ' secs') AS arr_inv1, " +
                    "CONCAT(FLOOR(HOUR(TIMEDIFF(arrival_received_date, IFNULL(temp_date,NOW()))) / 24), ' days, ', MOD(HOUR(TIMEDIFF(arrival_received_date, IFNULL(temp_date,NOW()))), 24), ' hours, ', MINUTE(TIMEDIFF(arrival_received_date, IFNULL(temp_date,NOW()))), ' mins, ', SECOND(TIMEDIFF(arrival_received_date, IFNULL(temp_date,NOW()))), ' secs') AS arr_inv2, " +
                    "SUBSTRING(TIMEDIFF(requested_date, IFNULL(shipping_date,NOW())),2,2) AS req_shp, SUBSTRING(TIMEDIFF(shipping_date, IFNULL(arrival_received_date,NOW())),2,2) AS shp_arr, SUBSTRING(TIMEDIFF(arrival_received_date, IFNULL(temp_date,NOW())), 2, 2) AS arr_inv " +
                    "FROM hms_wh_retrieval_list " +
                    "ORDER BY id DESC ";
        
        List<WhUSL> whUSLlist = new ArrayList<WhUSL>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhUSL whUSL;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whUSL = new WhUSL();
                //retrieve
                whUSL.setRetrieveId(rs.getString("id"));
                whUSL.setRefId((rs.getString("retrieve_id")));
                whUSL.setMaterialPassNo(rs.getString("material_pass_no"));
                whUSL.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whUSL.setEquipmentType(rs.getString("equipment_type"));
                whUSL.setEquipmentId(rs.getString("equipment_id"));
                whUSL.setPcbA(rs.getString("pcb_a"));
                whUSL.setQtyQualA(rs.getString("qty_qual_a"));
                whUSL.setPcbB(rs.getString("pcb_b"));
                whUSL.setQtyQualB(rs.getString("qty_qual_b"));
                whUSL.setPcbC(rs.getString("pcb_c"));
                whUSL.setQtyQualC(rs.getString("qty_qual_c"));
                whUSL.setPcbControl(rs.getString("pcb_control"));
                whUSL.setQtyControl(rs.getString("qty_control"));
                whUSL.setQuantity(rs.getString("quantity"));
                whUSL.setRequestedBy(rs.getString("requested_by"));
                whUSL.setRequestedEmail(rs.getString("requested_email"));
                whUSL.setRequestedDate(rs.getString("requested_date_view"));
                whUSL.setShippingDate(rs.getString("shipping_date_view"));
                String remarks = rs.getString("remarks");
                if(remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whUSL.setRemarks(remarks);
                whUSL.setReceivedDate(rs.getString("received_date_view"));
                whUSL.setBarcodeVerify(rs.getString("barcode_verify"));
                whUSL.setDateVerify(rs.getString("date_verify"));
                whUSL.setUserVerify(rs.getString("user_verify"));
                whUSL.setRetStatus(rs.getString("status"));
                whUSL.setRetFlag(rs.getString("flag"));
                whUSL.setArrivalReceivedDate(rs.getString("arrival_received_date"));
                whUSL.setTempRack(rs.getString("temp_rack"));
                whUSL.setTempShelf(rs.getString("temp_shelf"));
                whUSL.setTempDate(rs.getString("temp_date"));
                whUSL.setRetTempCount(rs.getString("temp_count"));
                whUSL.setReqShp(rs.getString("req_shp"));
                whUSL.setReqShp1(rs.getString("req_shp1"));
                whUSL.setReqShp2(rs.getString("req_shp2"));
                whUSL.setShpArr(rs.getString("shp_arr"));
                whUSL.setShpArr1(rs.getString("shp_arr1"));
                whUSL.setShpArr2(rs.getString("shp_arr2"));
                whUSL.setArrInv(rs.getString("arr_inv"));
                whUSL.setArrInv1(rs.getString("arr_inv1"));
                whUSL.setArrInv2(rs.getString("arr_inv2"));
                whUSLlist.add(whUSL);
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
        return whUSLlist;
    }
}