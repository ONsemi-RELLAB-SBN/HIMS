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
                    + "requested_by, requested_email, requested_date, remarks, shipping_date, received_date, status, "
                    + "flag, pairing_type, load_card_id, load_card_qty, prog_card_id, prog_card_qty) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS
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
            ps.setString(22, whRetrieve.getPairingType());
            ps.setString(23, whRetrieve.getLoadCardId());
            ps.setString(24, whRetrieve.getLoadCardQty());
            ps.setString(25, whRetrieve.getProgCardId());
            ps.setString(26, whRetrieve.getProgCardQty());

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

    public QueryResult insertWhRetrieveNew(WhRetrieve whRetrieve) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hms_wh_retrieval_list (retrieve_id, material_pass_no, material_pass_expiry, equipment_type, equipment_id, "
                    + "pcb_a, pcb_b, pcb_c, pcb_control,qty_qual_a, qty_qual_b, qty_qual_c, qty_control, quantity, "
                    + "requested_by, requested_email, requested_date, remarks, shipping_date, received_date, status, "
                    + "flag, pairing_type, load_card_id, load_card_qty, prog_card_id, prog_card_qty,box_no,gts_no) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS
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
            ps.setString(22, whRetrieve.getPairingType());
            ps.setString(23, whRetrieve.getLoadCardId());
            ps.setString(24, whRetrieve.getLoadCardQty());
            ps.setString(25, whRetrieve.getProgCardId());
            ps.setString(26, whRetrieve.getProgCardQty());
            ps.setString(27, whRetrieve.getBoxNo());
            ps.setString(28, whRetrieve.getGtsNo());

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
                    "UPDATE hms_wh_retrieval_list SET barcode_verify = ?, date_verify = ?, user_verify = ?, status = ?, flag = ?, temp_count = ? "
                    + "WHERE retrieve_id = ? AND box_no = ? "
            );
            ps.setString(1, whRetrieve.getBarcodeVerify());
            ps.setString(2, whRetrieve.getDateVerify());
            ps.setString(3, whRetrieve.getUserVerify());
            ps.setString(4, whRetrieve.getStatus());
            ps.setString(5, whRetrieve.getFlag());
            ps.setString(6, whRetrieve.getTempCount());
            ps.setString(7, whRetrieve.getRefId());
            ps.setString(8, whRetrieve.getBoxNo());
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
//        String sql = "UPDATE hms_wh_retrieval_list SET status = ?, flag = ?, temp_rack = ?, temp_shelf = ?, temp_date = now() "
//                + "WHERE retrieve_id = ? AND material_pass_no = ? ";
        String sql = "UPDATE hms_wh_retrieval_list SET status = ?, flag = ?, temp_rack = ?, temp_shelf = ?, temp_date = now() "
                + "WHERE retrieve_id = ? AND box_no = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whRetrieve.getStatus());
            ps.setString(2, whRetrieve.getFlag());
            ps.setString(3, whRetrieve.getTempRack());
            ps.setString(4, whRetrieve.getTempShelf());
            ps.setString(5, whRetrieve.getRefId());
            ps.setString(6, whRetrieve.getBoxNo());
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

    public QueryResult updateWhRetrieveReceivalTime(WhRetrieve whRetrieve) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_retrieval_list SET arrival_received_date = ? "
                + "WHERE retrieve_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whRetrieve.getArrivalReceivedDate());
            ps.setString(2, whRetrieve.getRefId());
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
        String sql = "SELECT *,DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                + "DATE_FORMAT(date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view, "
                + "DATE_FORMAT(received_date,'%d %M %Y %h:%i %p') AS received_date_view, DATE_FORMAT(arrival_received_date,'%d %M %Y %h:%i %p') AS arrival_received_date_view "
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
                if (remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRetrieve.setRemarks(remarks);
                whRetrieve.setReceivedDate(rs.getString("received_date_view"));
                whRetrieve.setBarcodeVerify(rs.getString("barcode_verify"));
                whRetrieve.setDateVerify(rs.getString("date_verify_view"));
                whRetrieve.setUserVerify(rs.getString("user_verify"));
                whRetrieve.setStatus(rs.getString("status"));
                whRetrieve.setFlag(rs.getString("flag"));
                whRetrieve.setArrivalReceivedDate(rs.getString("arrival_received_date_view"));
                whRetrieve.setTempRack(rs.getString("temp_rack"));
                whRetrieve.setTempShelf(rs.getString("temp_shelf"));
                whRetrieve.setTempCount(rs.getString("temp_count"));
                whRetrieve.setPairingType(rs.getString("pairing_type"));
                whRetrieve.setLoadCardId(rs.getString("load_card_id"));
                whRetrieve.setLoadCardQty(rs.getString("load_card_qty"));
                whRetrieve.setProgCardId(rs.getString("prog_card_id"));
                whRetrieve.setProgCardQty(rs.getString("prog_card_qty"));
                whRetrieve.setBoxNo(rs.getString("box_no"));
                whRetrieve.setGtsNo(rs.getString("gts_no"));
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
        String sql = "SELECT * "
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
                if (remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRetrieve.setRemarks(remarks);
                whRetrieve.setReceivedDate(rs.getString("received_date"));
                whRetrieve.setBarcodeVerify(rs.getString("barcode_verify"));
                whRetrieve.setDateVerify(rs.getString("date_verify"));
                whRetrieve.setUserVerify(rs.getString("user_verify"));
                whRetrieve.setStatus(rs.getString("status"));
                whRetrieve.setFlag(rs.getString("flag"));
                whRetrieve.setArrivalReceivedDate(rs.getString("arrival_received_date"));
                whRetrieve.setTempRack(rs.getString("temp_rack"));
                whRetrieve.setTempShelf(rs.getString("temp_shelf"));
                whRetrieve.setPairingType(rs.getString("pairing_type"));
                whRetrieve.setLoadCardId(rs.getString("load_card_id"));
                whRetrieve.setLoadCardQty(rs.getString("load_card_qty"));
                whRetrieve.setProgCardId(rs.getString("prog_card_id"));
                whRetrieve.setProgCardQty(rs.getString("prog_card_qty"));
                whRetrieve.setBoxNo(rs.getString("box_no"));
                whRetrieve.setGtsNo(rs.getString("gts_no"));
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
                + "DATE_FORMAT(date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view , "
                + "DATE_FORMAT(received_date,'%d %M %Y %h:%i %p') AS received_date_view, DATE_FORMAT(arrival_received_date,'%d %M %Y %h:%i %p') AS arrival_received_date_view "
                + "FROM hms_wh_retrieval_list "
                + "WHERE flag = 0 "
                + "ORDER BY requested_date DESC ";
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
                if (remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRetrieve.setRemarks(remarks);
                whRetrieve.setReceivedDate(rs.getString("received_date_view"));
                whRetrieve.setBarcodeVerify(rs.getString("barcode_verify"));
                whRetrieve.setDateVerify(rs.getString("date_verify_view"));
                whRetrieve.setUserVerify(rs.getString("user_verify"));
                whRetrieve.setStatus(rs.getString("status"));
                whRetrieve.setFlag(rs.getString("flag"));
                whRetrieve.setArrivalReceivedDate(rs.getString("arrival_received_date_view"));
                whRetrieve.setTempRack(rs.getString("temp_rack"));
                whRetrieve.setTempShelf(rs.getString("temp_shelf"));
                whRetrieve.setPairingType(rs.getString("pairing_type"));
                whRetrieve.setLoadCardId(rs.getString("load_card_id"));
                whRetrieve.setLoadCardQty(rs.getString("load_card_qty"));
                whRetrieve.setProgCardId(rs.getString("prog_card_id"));
                whRetrieve.setProgCardQty(rs.getString("prog_card_qty"));
                whRetrieve.setBoxNo(rs.getString("box_no"));
                whRetrieve.setGtsNo(rs.getString("gts_no"));
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

    //yesterday
    public List<WhRetrieve> getWhRetrieveReportList() {
        String sql = "SELECT *, CONCAT(FLOOR(HOUR(TIMEDIFF(shipping_date, date_verify)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(shipping_date, date_verify)), 24), ' hours, ', MINUTE(TIMEDIFF(shipping_date, date_verify)), ' mins') AS DURATION "
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
                if (remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRetrieve.setRemarks(remarks);
                whRetrieve.setReceivedDate(rs.getString("received_date"));
                whRetrieve.setBarcodeVerify(rs.getString("barcode_verify"));
                whRetrieve.setDateVerify(rs.getString("date_verify"));
                whRetrieve.setUserVerify(rs.getString("user_verify"));
                whRetrieve.setStatus(rs.getString("status"));
                whRetrieve.setFlag(rs.getString("flag"));
                whRetrieve.setArrivalReceivedDate(rs.getString("arrival_received_date"));
                whRetrieve.setDuration(rs.getString("duration"));
                whRetrieve.setTempRack(rs.getString("temp_rack"));
                whRetrieve.setTempShelf(rs.getString("temp_shelf"));
                whRetrieve.setPairingType(rs.getString("pairing_type"));
                whRetrieve.setLoadCardId(rs.getString("load_card_id"));
                whRetrieve.setLoadCardQty(rs.getString("load_card_qty"));
                whRetrieve.setProgCardId(rs.getString("prog_card_id"));
                whRetrieve.setProgCardQty(rs.getString("prog_card_qty"));
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
                    "SELECT COUNT(date_verify) AS count "
                    + "FROM hms_wh_retrieval_list "
                    + "WHERE DATE(date_verify) LIKE SUBDATE(DATE(NOW()),1) "
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
                whRetrieveLog.setPcbA(rs.getString("pcb_a"));
                whRetrieveLog.setQtyQualA(rs.getString("qty_qual_a"));
                whRetrieveLog.setPcbB(rs.getString("pcb_b"));
                whRetrieveLog.setQtyQualB(rs.getString("qty_qual_b"));
                whRetrieveLog.setPcbC(rs.getString("pcb_c"));
                whRetrieveLog.setQtyQualC(rs.getString("qty_qual_c"));
                whRetrieveLog.setPcbControl(rs.getString("pcb_control"));
                whRetrieveLog.setQtyControl(rs.getString("qty_control"));
                whRetrieveLog.setQuantity(rs.getString("quantity"));
                whRetrieveLog.setRequestedBy(rs.getString("requested_by"));
                whRetrieveLog.setRequestedEmail(rs.getString("requested_email"));
                whRetrieveLog.setRequestedDate(rs.getString("requested_date"));
                whRetrieveLog.setShippingDate(rs.getString("shipping_date"));
                String remarks = rs.getString("remarks");
                if (remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRetrieveLog.setRemarks(remarks);
                whRetrieveLog.setReceivedDate(rs.getString("received_date"));
                whRetrieveLog.setBarcodeVerify(rs.getString("barcode_verify"));
                whRetrieveLog.setDateVerify(rs.getString("date_verify"));
                whRetrieveLog.setUserVerify(rs.getString("user_verify"));
                whRetrieveLog.setStatus(rs.getString("R.status"));
                whRetrieveLog.setFlag(rs.getString("flag"));
                whRetrieveLog.setArrivalReceivedDate(rs.getString("arrival_received_date"));
                whRetrieveLog.setPairingType(rs.getString("pairing_type"));
                whRetrieveLog.setLoadCardId(rs.getString("load_card_id"));
                whRetrieveLog.setLoadCardQty(rs.getString("load_card_qty"));
                whRetrieveLog.setProgCardId(rs.getString("prog_card_id"));
                whRetrieveLog.setProgCardQty(rs.getString("prog_card_qty"));
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

    public List<WhRetrieveLog> getWhRetLog(String whRetrieveId) {
        String sql = "SELECT *, DATE_FORMAT(timestamp,'%d %M %Y %h:%i %p') AS timestamp_view, DATE_FORMAT(verified_date,'%d %M %Y %h:%i %p') AS verified_date_view, DATE_FORMAT(received_date,'%d %M %Y %h:%i %p') AS received_date_view,"
                + "DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, DATE_FORMAT(shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view, "
                + "DATE_FORMAT(arrival_received_date,'%d %M %Y %h:%i %p') AS arrival_received_date_view, "
                + "CONCAT(FLOOR(HOUR(TIMEDIFF(shipping_date, arrival_received_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(shipping_date, arrival_received_date)), 24), ' hours, ', MINUTE(TIMEDIFF(shipping_date, arrival_received_date)), ' mins, ', SECOND(TIMEDIFF(shipping_date, arrival_received_date)), ' secs') AS ship_arr_rec, "
                + "CONCAT(FLOOR(HOUR(TIMEDIFF(arrival_received_date, temp_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(arrival_received_date, temp_date)), 24), ' hours, ', MINUTE(TIMEDIFF(arrival_received_date, temp_date)), ' mins, ', SECOND(TIMEDIFF(arrival_received_date, temp_date)), ' secs') AS arr_rec_inv "
                + "FROM hms_wh_log L, hms_wh_retrieval_list R "
                + "WHERE L.reference_id = R.retrieve_id AND R.retrieve_id = '" + whRetrieveId + "' "
                + "ORDER BY timestamp DESC";
//                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(shipping_date, received_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(shipping_date, received_date)), 24), ' hours, ', MINUTE(TIMEDIFF(shipping_date, received_date)), ' mins') AS ship_receive, "
//                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(received_date, date_verify)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(received_date, date_verify)), 24), ' hours, ', MINUTE(TIMEDIFF(received_date, date_verify)), ' mins') AS receive_verify, "
//                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(date_verify, temp_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(date_verify, temp_date)), 24), ' hours, ', MINUTE(TIMEDIFF(date_verify, temp_date)), ' mins') AS verify_inventory, "
//                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(received_date, temp_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(received_date, temp_date)), 24), ' hours, ', MINUTE(TIMEDIFF(received_date, temp_date)), ' mins') AS receive_inventory, "
//                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(shipping_date, temp_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(shipping_date, temp_date)), 24), ' hours, ', MINUTE(TIMEDIFF(shipping_date, temp_date)), ' mins') AS shipping_inventory "

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
                whRetrieveLog.setTimestamp(rs.getString("timestamp_view"));
                whRetrieveLog.setLogVerifyDate(rs.getString("verified_date_view"));
                whRetrieveLog.setLogVerifyBy(rs.getString("verified_by"));
                //retrieve
                whRetrieveLog.setRetrieveId(whRetrieveId);
                whRetrieveLog.setMaterialPassNo(rs.getString("material_pass_no"));
                whRetrieveLog.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whRetrieveLog.setEquipmentType(rs.getString("equipment_type"));
                whRetrieveLog.setEquipmentId(rs.getString("equipment_id"));
                whRetrieveLog.setPcbA(rs.getString("pcb_a"));
                whRetrieveLog.setQtyQualA(rs.getString("qty_qual_a"));
                whRetrieveLog.setPcbB(rs.getString("pcb_b"));
                whRetrieveLog.setQtyQualB(rs.getString("qty_qual_b"));
                whRetrieveLog.setPcbC(rs.getString("pcb_c"));
                whRetrieveLog.setQtyQualC(rs.getString("qty_qual_c"));
                whRetrieveLog.setPcbControl(rs.getString("pcb_control"));
                whRetrieveLog.setQtyControl(rs.getString("qty_control"));
                whRetrieveLog.setQuantity(rs.getString("quantity"));
                whRetrieveLog.setRequestedBy(rs.getString("requested_by"));
                whRetrieveLog.setRequestedEmail(rs.getString("requested_email"));
                whRetrieveLog.setRequestedDate(rs.getString("requested_date_view"));
                whRetrieveLog.setShippingDate(rs.getString("shipping_date_view"));
                String remarks = rs.getString("remarks");
                if (remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRetrieveLog.setRemarks(remarks);
                whRetrieveLog.setReceivedDate(rs.getString("received_date_view"));
                whRetrieveLog.setBarcodeVerify(rs.getString("barcode_verify"));
                whRetrieveLog.setDateVerify(rs.getString("date_verify"));
                whRetrieveLog.setUserVerify(rs.getString("user_verify"));
                whRetrieveLog.setStatus(rs.getString("R.status"));
                whRetrieveLog.setFlag(rs.getString("flag"));
                whRetrieveLog.setArrivalReceivedDate(rs.getString("arrival_received_date_view"));
                whRetrieveLog.setTempRack(rs.getString("temp_rack"));
                whRetrieveLog.setTempShelf(rs.getString("temp_shelf"));
                whRetrieveLog.setTempDate(rs.getString("temp_date"));
                whRetrieveLog.setPairingType(rs.getString("pairing_type"));
                whRetrieveLog.setLoadCardId(rs.getString("load_card_id"));
                whRetrieveLog.setLoadCardQty(rs.getString("load_card_qty"));
                whRetrieveLog.setProgCardId(rs.getString("prog_card_id"));
                whRetrieveLog.setProgCardQty(rs.getString("prog_card_qty"));
                whRetrieveLog.setShipArrReceive(rs.getString("ship_arr_rec"));
                whRetrieveLog.setArrReceiveInventory(rs.getString("arr_rec_inv"));
                whRetrieveLog.setBoxNo(rs.getString("box_no"));
                whRetrieveLog.setGtsNo(rs.getString("gts_no"));
//                whRetrieveLog.setShipReceive(rs.getString("ship_receive"));
//                whRetrieveLog.setReceiveVerify(rs.getString("receive_verify"));
//                whRetrieveLog.setVerifyInventory(rs.getString("verify_inventory"));
//                whRetrieveLog.setReceiveInventory(rs.getString("receive_inventory"));
//                whRetrieveLog.setShippingInventory(rs.getString("shipping_inventory"));
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

    public List<WhRetrieve> getQuery(String query) {
        String sql = "SELECT *, DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                + "DATE_FORMAT(date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view , "
                + "DATE_FORMAT(received_date,'%d %M %Y %h:%i %p') AS received_date_view, DATE_FORMAT(arrival_received_date,'%d %M %Y %h:%i %p') AS arrival_received_date_view "
                + "FROM hms_wh_retrieval_list "
                + "WHERE " + query;

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
                if (remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whRetrieve.setRemarks(remarks);
                whRetrieve.setReceivedDate(rs.getString("received_date_view"));
                whRetrieve.setBarcodeVerify(rs.getString("barcode_verify"));
                whRetrieve.setDateVerify(rs.getString("date_verify_view"));
                whRetrieve.setUserVerify(rs.getString("user_verify"));
                whRetrieve.setStatus(rs.getString("status"));
                whRetrieve.setFlag(rs.getString("flag"));
                whRetrieve.setArrivalReceivedDate(rs.getString("arrival_received_date_view"));
                whRetrieve.setTempRack(rs.getString("temp_rack"));
                whRetrieve.setTempShelf(rs.getString("temp_shelf"));
                whRetrieve.setPairingType(rs.getString("pairing_type"));
                whRetrieve.setLoadCardId(rs.getString("load_card_id"));
                whRetrieve.setLoadCardQty(rs.getString("load_card_qty"));
                whRetrieve.setProgCardId(rs.getString("prog_card_id"));
                whRetrieve.setProgCardQty(rs.getString("prog_card_qty"));
                whRetrieve.setBoxNo(rs.getString("box_no"));
                whRetrieve.setGtsNo(rs.getString("gts_no"));
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

    public List<WhRetrieve> getHardwareId() {
        String sql = "SELECT DISTINCT equipment_id "
                + "FROM hms_wh_retrieval_list "
                + "ORDER BY equipment_id ";
        List<WhRetrieve> hardwareIdList = new ArrayList<WhRetrieve>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRetrieve whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhRetrieve();
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

    public List<WhRetrieve> getHardwareType() {
        String sql = "SELECT DISTINCT equipment_type "
                + "FROM hms_wh_retrieval_list "
                + "ORDER BY equipment_type ";
        List<WhRetrieve> hardwareTypeList = new ArrayList<WhRetrieve>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRetrieve whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhRetrieve();
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

    public List<WhRetrieve> getRequestedBy() {
        String sql = "SELECT DISTINCT requested_by "
                + "FROM hms_wh_retrieval_list "
                + "ORDER BY requested_by ";
        List<WhRetrieve> requestedByList = new ArrayList<WhRetrieve>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRetrieve whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhRetrieve();
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

    public List<WhRetrieve> getStatus() {
        String sql = "SELECT DISTINCT status "
                + "FROM hms_wh_retrieval_list "
                + "ORDER BY status ";
        List<WhRetrieve> statusList = new ArrayList<WhRetrieve>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhRetrieve whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhRetrieve();
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

    public QueryResult insertEqpt(WhRetrieve whRetrieve) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hms_wh_retrieval_list (retrieve_id, equipment_type, equipment_id, quantity, material_pass_no, material_pass_expiry, "
                    + "requested_by, requested_email, user_verify, barcode_verify, temp_rack, temp_shelf, temp_count, status, flag, "
                    + "requested_date, shipping_date, received_date, date_verify, arrival_received_date, temp_date )"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),NOW(),NOW(),NOW(),NOW(),NOW())", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, whRetrieve.getRefId());
            ps.setString(2, whRetrieve.getEquipmentType());
            ps.setString(3, whRetrieve.getEquipmentId());
            ps.setString(4, whRetrieve.getQuantity());
            ps.setString(5, whRetrieve.getMaterialPassNo());
            ps.setString(6, whRetrieve.getMaterialPassExpiry());
            ps.setString(7, whRetrieve.getRequestedBy());
            ps.setString(8, whRetrieve.getRequestedEmail());
            ps.setString(9, whRetrieve.getUserVerify());
            ps.setString(10, whRetrieve.getBarcodeVerify());
            ps.setString(11, whRetrieve.getTempRack());
            ps.setString(12, whRetrieve.getTempShelf());
            ps.setString(13, whRetrieve.getTempCount());
            ps.setString(14, whRetrieve.getStatus());
            ps.setString(15, whRetrieve.getFlag());

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

    public QueryResult insertBib(WhRetrieve whRetrieve) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hms_wh_retrieval_list (retrieve_id, equipment_type, equipment_id, pairing_type, load_card_id, load_card_qty, prog_card_id, prog_card_qty, quantity, material_pass_no, material_pass_expiry, "
                    + "requested_by, requested_email, user_verify, barcode_verify, temp_rack, temp_shelf, temp_count, status, flag, "
                    + "requested_date, shipping_date, received_date, date_verify, arrival_received_date, temp_date )"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),NOW(),NOW(),NOW(),NOW(),NOW())", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, whRetrieve.getRefId());
            ps.setString(2, whRetrieve.getEquipmentType());
            ps.setString(3, whRetrieve.getEquipmentId());
            ps.setString(4, whRetrieve.getPairingType());
            ps.setString(5, whRetrieve.getLoadCardId());
            ps.setString(6, whRetrieve.getLoadCardQty());
            ps.setString(7, whRetrieve.getProgCardId());
            ps.setString(8, whRetrieve.getProgCardQty());
            ps.setString(9, whRetrieve.getQuantity());
            ps.setString(10, whRetrieve.getMaterialPassNo());
            ps.setString(11, whRetrieve.getMaterialPassExpiry());
            ps.setString(12, whRetrieve.getRequestedBy());
            ps.setString(13, whRetrieve.getRequestedEmail());
            ps.setString(14, whRetrieve.getUserVerify());
            ps.setString(15, whRetrieve.getBarcodeVerify());
            ps.setString(16, whRetrieve.getTempRack());
            ps.setString(17, whRetrieve.getTempShelf());
            ps.setString(18, whRetrieve.getTempCount());
            ps.setString(19, whRetrieve.getStatus());
            ps.setString(20, whRetrieve.getFlag());

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

    public QueryResult updateBib(WhRetrieve whRetrieve) {
        QueryResult queryResult = new QueryResult();
        String sql
                = "UPDATE hms_wh_retrieval_list SET pairing_type = ?, load_card_id = ?, load_card_qty = ?, prog_card_id = ?, prog_card_qty = ?, quantity = ? "
                + "WHERE retrieve_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whRetrieve.getPairingType());
            ps.setString(2, whRetrieve.getLoadCardId());
            ps.setString(3, whRetrieve.getLoadCardQty());
            ps.setString(4, whRetrieve.getProgCardId());
            ps.setString(5, whRetrieve.getProgCardQty());
            ps.setString(6, whRetrieve.getQuantity());
            ps.setString(7, whRetrieve.getRefId());
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

    public QueryResult updateBoxNoGtsNo(WhRetrieve whRetrieve) {
        QueryResult queryResult = new QueryResult();
        String sql
                = "UPDATE hms_wh_retrieval_list SET box_no = ?, gts_no = ? "
                + "WHERE retrieve_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whRetrieve.getBoxNo());
            ps.setString(2, whRetrieve.getGtsNo());
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

    public QueryResult updateLcPc(WhRetrieve whRetrieve) {
        QueryResult queryResult = new QueryResult();
        String sql
                = "UPDATE hms_wh_retrieval_list SET load_card_id = ?, load_card_qty = ?, prog_card_id = ?, prog_card_qty = ? "
                + "WHERE retrieve_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whRetrieve.getLoadCardId());
            ps.setString(2, whRetrieve.getLoadCardQty());
            ps.setString(3, whRetrieve.getProgCardId());
            ps.setString(4, whRetrieve.getProgCardQty());
            ps.setString(5, whRetrieve.getRefId());
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
}
