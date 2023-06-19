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
import com.onsemi.hms.model.WhInventory;
import com.onsemi.hms.model.WhInventoryLog;
import com.onsemi.hms.tools.QueryResult;
import com.onsemi.hms.tools.SpmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhInventoryDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhInventoryDAO.class);
    private final Connection conn;
    private final DataSource dataSource;

    public WhInventoryDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
        this.dataSource = db.getDataSource();
    }

    public QueryResult insertWhInventory(WhInventory whInventory) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hms_wh_inventory_list (retrieve_id, material_pass_no, inventory_date, inventory_rack, inventory_shelf, inventory_by, status, flag, box_no) "
                    + "VALUES (?,?,NOW(),?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, whInventory.getRefId());
            ps.setString(2, whInventory.getMaterialPassNo());
            ps.setString(3, whInventory.getInventoryRack());
            ps.setString(4, whInventory.getInventoryShelf());
            ps.setString(5, whInventory.getInventoryBy());
            ps.setString(6, whInventory.getStatus());
            ps.setString(7, whInventory.getFlag());
            ps.setString(8, whInventory.getBoxNo());
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

    public QueryResult updateWhInventory(WhInventory whInventory) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_inventory_list "
                + "SET inventory_date = NOW(), inventory_rack = ?, inventory_shelf = ?, inventory_by = ? "
                + "WHERE retrieve_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whInventory.getInventoryRack());
            ps.setString(2, whInventory.getInventoryShelf());
            ps.setString(3, whInventory.getInventoryBy());
            ps.setString(4, whInventory.getRefId());
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

    public QueryResult updateWhInventoryStatus(WhInventory whInventory) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_inventory_list "
                + "SET status = ?, flag = ? "
                + "WHERE box_no = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whInventory.getStatus());
            ps.setString(2, whInventory.getFlag());
            ps.setString(3, whInventory.getBoxNo());
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

    public QueryResult updateWhInventoryMpToBox(WhInventory whInventory) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_inventory_list "
                + "SET mp_to_box = ? "
                + "WHERE retrieve_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whInventory.getMpToBox());
            ps.setString(2, whInventory.getRefId());
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

    public QueryResult updateWhInventoryBoxNo(WhInventory whInventory) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_inventory_list "
                + "SET box_no = ? "
                + "WHERE retrieve_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, whInventory.getBoxNo());
            ps.setString(2, whInventory.getRefId());
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

    public QueryResult deleteWhInventory(String whInventoryId) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM hms_wh_inventory_list WHERE retrieve_id = '" + whInventoryId + "'"
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

    public WhInventory getWhInventory(String whInventoryId) {
        String sql = "SELECT * "
                + "FROM hms_wh_inventory_list "
                + "WHERE retrieve_id = '" + whInventoryId + "'";
        WhInventory whInventory = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventory();
                whInventory.setId(rs.getString("id"));
                whInventory.setRefId(rs.getString("retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("material_pass_no"));
                whInventory.setInventoryDate(rs.getString("inventory_date"));
                whInventory.setInventoryRack(rs.getString("inventory_rack"));
                whInventory.setInventoryShelf(rs.getString("inventory_shelf"));
                whInventory.setInventoryBy(rs.getString("inventory_by"));
                whInventory.setStatus(rs.getString("status"));
                whInventory.setFlag(rs.getString("flag"));
                whInventory.setBoxNo(rs.getString("box_no"));
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
        return whInventory;
    }

    public WhInventory getWhInventoryMergeWithRetrievePdf(String whInventoryId) {
        String sql = "SELECT IL.*, RL.*, DATE_FORMAT(RL.material_pass_expiry,'%d %M %Y') AS mp_expiry_view , DATE_FORMAT(RL.requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                + "DATE_FORMAT(RL.requested_date,'%d %M %Y') AS requested_date_view2, DATE_FORMAT(RL.date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(IL.inventory_date,'%d %M %Y %h:%i %p') AS inventory_date_view, "
                + "DATE_FORMAT(RL.shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view, DATE_FORMAT(RL.received_date,'%d %M %Y %h:%i %p') AS received_date_view, "
                + "DATE_FORMAT(arrival_received_date,'%d %M %Y %h:%i %p') AS arrival_received_date_view "
                + "FROM hms_wh_inventory_list IL, hms_wh_retrieval_list RL "
                + "WHERE IL.retrieve_id = RL.retrieve_id AND IL.retrieve_id = '" + whInventoryId + "' ";
        WhInventory whInventory = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventory();
                whInventory.setRefId(rs.getString("IL.retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("IL.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whInventory.setEquipmentType(rs.getString("RL.equipment_type"));
                whInventory.setEquipmentId(rs.getString("RL.equipment_id"));
                whInventory.setPcbA(rs.getString("RL.pcb_a"));
                whInventory.setQtyQualA(rs.getString("RL.qty_qual_a"));
                whInventory.setPcbB(rs.getString("RL.pcb_b"));
                whInventory.setQtyQualB(rs.getString("RL.qty_qual_b"));
                whInventory.setPcbC(rs.getString("RL.pcb_c"));
                whInventory.setQtyQualC(rs.getString("RL.qty_qual_c"));
                whInventory.setPcbControl(rs.getString("RL.pcb_control"));
                whInventory.setQtyControl(rs.getString("RL.qty_control"));
                whInventory.setQuantity(rs.getString("RL.quantity"));
                whInventory.setRemarks(rs.getString("RL.remarks"));
                whInventory.setRequestedBy(rs.getString("RL.requested_by"));
                whInventory.setRequestedDate(rs.getString("requested_date_view"));
                whInventory.setRequestedDateView2(rs.getString("requested_date_view2"));
                whInventory.setShippingDate(rs.getString("RL.shipping_date"));
                whInventory.setReceivedDate(rs.getString("received_date_view"));
                whInventory.setBarcodeVerify(rs.getString("RL.barcode_verify"));
                whInventory.setDateVerify(rs.getString("date_verify_view"));
                whInventory.setUserVerify(rs.getString("RL.user_verify"));
                whInventory.setInventoryDate(rs.getString("inventory_date_view"));
                whInventory.setInventoryRack(rs.getString("IL.inventory_rack"));
                whInventory.setInventoryShelf(rs.getString("IL.inventory_shelf"));
                whInventory.setInventoryBy(rs.getString("IL.inventory_by"));
                whInventory.setStatus(rs.getString("RL.status"));
                whInventory.setFlag(rs.getString("IL.flag"));
                whInventory.setArrivalReceivedDate(rs.getString("arrival_received_date_view"));
                whInventory.setPairingType(rs.getString("pairing_type"));
                whInventory.setLoadCardId(rs.getString("load_card_id"));
                whInventory.setLoadCardQty(rs.getString("load_card_qty"));
                whInventory.setProgCardId(rs.getString("prog_card_id"));
                whInventory.setProgCardQty(rs.getString("prog_card_qty"));
                whInventory.setBoxNo(rs.getString("RL.box_no"));
                whInventory.setGtsNo(rs.getString("RL.gts_no"));
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
        return whInventory;
    }

    public WhInventory getWhInventoryMergeWithRetrieve(String whInventoryId) {
        String sql = "SELECT IL.*, RL.* "
                + "FROM hms_wh_inventory_list IL, hms_wh_retrieval_list RL  "
                + "WHERE IL.retrieve_id = RL.retrieve_id AND IL.retrieve_id = '" + whInventoryId + "' ";
        WhInventory whInventory = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventory();
                whInventory.setRefId(rs.getString("retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("IL.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("material_pass_expiry"));
                whInventory.setEquipmentType(rs.getString("equipment_type"));
                whInventory.setEquipmentId(rs.getString("equipment_id"));
                whInventory.setPcbA(rs.getString("RL.pcb_a"));
                whInventory.setQtyQualA(rs.getString("RL.qty_qual_a"));
                whInventory.setPcbB(rs.getString("RL.pcb_b"));
                whInventory.setQtyQualB(rs.getString("RL.qty_qual_b"));
                whInventory.setPcbC(rs.getString("RL.pcb_c"));
                whInventory.setQtyQualC(rs.getString("RL.qty_qual_c"));
                whInventory.setPcbControl(rs.getString("RL.pcb_control"));
                whInventory.setQtyControl(rs.getString("RL.qty_control"));
                whInventory.setQuantity(rs.getString("quantity"));
                whInventory.setRemarks(rs.getString("remarks"));
                whInventory.setRequestedBy(rs.getString("requested_by"));
                whInventory.setRequestedEmail(rs.getString("requested_email"));
                whInventory.setRequestedDate(rs.getString("requested_date"));
                whInventory.setShippingDate(rs.getString("RL.shipping_date"));
                whInventory.setReceivedDate(rs.getString("RL.received_date"));
                whInventory.setBarcodeVerify(rs.getString("barcode_verify"));
                whInventory.setDateVerify(rs.getString("date_verify"));
                whInventory.setUserVerify(rs.getString("user_verify"));
                whInventory.setInventoryDate(rs.getString("inventory_date"));
                whInventory.setInventoryRack(rs.getString("inventory_rack"));
                whInventory.setInventoryShelf(rs.getString("inventory_shelf"));
                whInventory.setInventoryBy(rs.getString("inventory_by"));
                whInventory.setStatus(rs.getString("RL.status"));
                whInventory.setFlag(rs.getString("IL.flag"));
                whInventory.setArrivalReceivedDate(rs.getString("arrival_received_date"));
                whInventory.setPairingType(rs.getString("pairing_type"));
                whInventory.setLoadCardId(rs.getString("load_card_id"));
                whInventory.setLoadCardQty(rs.getString("load_card_qty"));
                whInventory.setProgCardId(rs.getString("prog_card_id"));
                whInventory.setProgCardQty(rs.getString("prog_card_qty"));
                whInventory.setBoxNo(rs.getString("RL.box_no"));
                whInventory.setGtsNo(rs.getString("RL.gts_no"));
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
        return whInventory;
    }

    public List<WhInventory> getWhInventoryListMergeRetrieve() {
        String sql = "SELECT IL.*, RL.*, DATE_FORMAT(RL.material_pass_expiry,'%d %M %Y') AS mp_expiry_view , DATE_FORMAT(RL.requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                + "DATE_FORMAT(RL.date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(IL.inventory_date,'%d %M %Y %h:%i %p') AS inventory_date_view, "
                + "DATE_FORMAT(RL.shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view, DATE_FORMAT(RL.received_date,'%d %M %Y %h:%i %p') AS received_date_view, "
                + "DATE_FORMAT(arrival_received_date,'%d %M %Y %h:%i %p') AS arrival_received_date_view "
                + "FROM hms_wh_inventory_list IL, hms_wh_retrieval_list RL "
                + "WHERE IL.retrieve_id = RL.retrieve_id AND IL.flag = '0' "
                + "ORDER BY IL.inventory_date DESC";
        List<WhInventory> whInventoryList = new ArrayList<WhInventory>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventory whInventory;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventory();
                whInventory.setRefId(rs.getString("IL.retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("IL.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whInventory.setEquipmentType(rs.getString("RL.equipment_type"));
                whInventory.setEquipmentId(rs.getString("RL.equipment_id"));
                whInventory.setPcbA(rs.getString("RL.pcb_a"));
                whInventory.setQtyQualA(rs.getString("RL.qty_qual_a"));
                whInventory.setPcbB(rs.getString("RL.pcb_b"));
                whInventory.setQtyQualB(rs.getString("RL.qty_qual_b"));
                whInventory.setPcbC(rs.getString("RL.pcb_c"));
                whInventory.setQtyQualC(rs.getString("RL.qty_qual_c"));
                whInventory.setPcbControl(rs.getString("RL.pcb_control"));
                whInventory.setQtyControl(rs.getString("RL.qty_control"));
                whInventory.setQuantity(rs.getString("RL.quantity"));
                whInventory.setRemarks(rs.getString("RL.remarks"));
                whInventory.setRequestedBy(rs.getString("RL.requested_by"));
                whInventory.setRequestedDate(rs.getString("requested_date_view"));
                whInventory.setShippingDate(rs.getString("shipping_date_view"));
                whInventory.setReceivedDate(rs.getString("received_date_view"));
                whInventory.setBarcodeVerify(rs.getString("RL.barcode_verify"));
                whInventory.setDateVerify(rs.getString("date_verify_view"));
                whInventory.setUserVerify(rs.getString("RL.user_verify"));
                whInventory.setInventoryDate(rs.getString("inventory_date_view"));
                whInventory.setInventoryRack(rs.getString("inventory_rack"));
                whInventory.setInventoryShelf(rs.getString("inventory_shelf"));
                whInventory.setInventoryBy(rs.getString("IL.inventory_by"));
                whInventory.setStatus(rs.getString("IL.status"));
                whInventory.setFlag(rs.getString("IL.flag"));
                whInventory.setArrivalReceivedDate(rs.getString("arrival_received_date_view"));
                whInventory.setPairingType(rs.getString("pairing_type"));
                whInventory.setLoadCardId(rs.getString("load_card_id"));
                whInventory.setLoadCardQty(rs.getString("load_card_qty"));
                whInventory.setProgCardId(rs.getString("prog_card_id"));
                whInventory.setProgCardQty(rs.getString("prog_card_qty"));
                whInventory.setBoxNo(rs.getString("RL.box_no"));
                whInventory.setGtsNo(rs.getString("RL.gts_no"));
                whInventoryList.add(whInventory);
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
        return whInventoryList;
    }

    public List<WhInventory> getWhInventoryListMergeRetrieveForMpToBoxNo(String eqptType) {
        String sql = "SELECT IL.*, RL.*, DATE_FORMAT(RL.material_pass_expiry,'%d %M %Y') AS mp_expiry_view , DATE_FORMAT(RL.requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                + "DATE_FORMAT(RL.date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(IL.inventory_date,'%d %M %Y %h:%i %p') AS inventory_date_view, "
                + "DATE_FORMAT(RL.shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view, DATE_FORMAT(RL.received_date,'%d %M %Y %h:%i %p') AS received_date_view, "
                + "DATE_FORMAT(arrival_received_date,'%d %M %Y %h:%i %p') AS arrival_received_date_view "
                + "FROM hms_wh_inventory_list IL, hms_wh_retrieval_list RL "
                + "WHERE IL.retrieve_id = RL.retrieve_id AND IL.flag = '0' AND IL.mp_to_box = '0' AND RL.material_pass_no IS NOT NULL "
                + "AND RL.equipment_type LIKE '%" + eqptType + "%' "
                + "ORDER BY IL.inventory_date DESC";
        List<WhInventory> whInventoryList = new ArrayList<WhInventory>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventory whInventory;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventory();
                whInventory.setRefId(rs.getString("IL.retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("IL.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whInventory.setEquipmentType(rs.getString("RL.equipment_type"));
                whInventory.setEquipmentId(rs.getString("RL.equipment_id"));
                whInventory.setPcbA(rs.getString("RL.pcb_a"));
                whInventory.setQtyQualA(rs.getString("RL.qty_qual_a"));
                whInventory.setPcbB(rs.getString("RL.pcb_b"));
                whInventory.setQtyQualB(rs.getString("RL.qty_qual_b"));
                whInventory.setPcbC(rs.getString("RL.pcb_c"));
                whInventory.setQtyQualC(rs.getString("RL.qty_qual_c"));
                whInventory.setPcbControl(rs.getString("RL.pcb_control"));
                whInventory.setQtyControl(rs.getString("RL.qty_control"));
                whInventory.setQuantity(rs.getString("RL.quantity"));
                whInventory.setRemarks(rs.getString("RL.remarks"));
                whInventory.setRequestedBy(rs.getString("RL.requested_by"));
                whInventory.setRequestedDate(rs.getString("requested_date_view"));
                whInventory.setShippingDate(rs.getString("shipping_date_view"));
                whInventory.setReceivedDate(rs.getString("received_date_view"));
                whInventory.setBarcodeVerify(rs.getString("RL.barcode_verify"));
                whInventory.setDateVerify(rs.getString("date_verify_view"));
                whInventory.setUserVerify(rs.getString("RL.user_verify"));
                whInventory.setInventoryDate(rs.getString("inventory_date_view"));
                whInventory.setInventoryRack(rs.getString("inventory_rack"));
                whInventory.setInventoryShelf(rs.getString("inventory_shelf"));
                whInventory.setInventoryBy(rs.getString("IL.inventory_by"));
                whInventory.setStatus(rs.getString("IL.status"));
                whInventory.setFlag(rs.getString("IL.flag"));
                whInventory.setArrivalReceivedDate(rs.getString("arrival_received_date_view"));
                whInventory.setPairingType(rs.getString("pairing_type"));
                whInventory.setLoadCardId(rs.getString("load_card_id"));
                whInventory.setLoadCardQty(rs.getString("load_card_qty"));
                whInventory.setProgCardId(rs.getString("prog_card_id"));
                whInventory.setProgCardQty(rs.getString("prog_card_qty"));
                whInventory.setBoxNo(rs.getString("RL.box_no"));
                whInventory.setGtsNo(rs.getString("RL.gts_no"));
                whInventoryList.add(whInventory);
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
        return whInventoryList;
    }

    public Integer getCountExistingData(String id) {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(*) AS count FROM hms_wh_inventory_list WHERE retrieve_id = '" + id + "' "
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

    public List<WhInventory> getWhInventoryMpExpiryList() {
        String sql = "SELECT RL.*, IL.*, DATE_FORMAT(RL.material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(RL.requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                + "DATE_FORMAT(RL.date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(IL.inventory_date,'%d %M %Y %h:%i %p') AS inventory_date_view, "
                + "DATE_FORMAT(RL.shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view, DATE_FORMAT(RL.received_date,'%d %M %Y %h:%i %p') AS received_date_view, "
                + "DATE_FORMAT(arrival_received_date,'%d %M %Y %h:%i %p') AS arrival_received_date_view "
                + "FROM hms_wh_retrieval_list RL, hms_wh_inventory_list IL "
                + "WHERE DATE(RL.material_pass_expiry) >= DATE(NOW()) "
                + "AND DATE(RL.material_pass_expiry) <= ADDDATE(DATE(NOW()),30) "
                + "AND RL.retrieve_id = IL.retrieve_id "
                + "ORDER BY RL.material_pass_expiry ASC ";
        List<WhInventory> whInventoryList = new ArrayList<WhInventory>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventory whInventory;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventory();
                whInventory.setRefId(rs.getString("IL.retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("IL.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whInventory.setEquipmentType(rs.getString("RL.equipment_type"));
                whInventory.setEquipmentId(rs.getString("RL.equipment_id"));
                whInventory.setPcbA(rs.getString("RL.pcb_a"));
                whInventory.setQtyQualA(rs.getString("RL.qty_qual_a"));
                whInventory.setPcbB(rs.getString("RL.pcb_b"));
                whInventory.setQtyQualB(rs.getString("RL.qty_qual_b"));
                whInventory.setPcbC(rs.getString("RL.pcb_c"));
                whInventory.setQtyQualC(rs.getString("RL.qty_qual_c"));
                whInventory.setPcbControl(rs.getString("RL.pcb_control"));
                whInventory.setQtyControl(rs.getString("RL.qty_control"));
                whInventory.setQuantity(rs.getString("RL.quantity"));
                whInventory.setRequestedBy(rs.getString("RL.requested_by"));
                whInventory.setRequestedDate(rs.getString("requested_date_view"));
                whInventory.setShippingDate(rs.getString("shipping_date_view"));
                whInventory.setRemarks(rs.getString("RL.remarks"));
                whInventory.setReceivedDate(rs.getString("received_date_view"));
                whInventory.setBarcodeVerify(rs.getString("RL.barcode_verify"));
                whInventory.setDateVerify(rs.getString("date_verify_view"));
                whInventory.setUserVerify(rs.getString("RL.user_verify"));
                whInventory.setInventoryDate(rs.getString("inventory_date_view"));
                whInventory.setInventoryRack(rs.getString("inventory_rack"));
                whInventory.setInventoryShelf(rs.getString("inventory_shelf"));
                whInventory.setInventoryBy(rs.getString("IL.inventory_by"));
                whInventory.setStatus(rs.getString("IL.status"));
                whInventory.setFlag(rs.getString("IL.flag"));
                whInventory.setArrivalReceivedDate(rs.getString("arrival_received_date_view"));
                whInventory.setPairingType(rs.getString("pairing_type"));
                whInventory.setLoadCardId(rs.getString("load_card_id"));
                whInventory.setLoadCardQty(rs.getString("load_card_qty"));
                whInventory.setProgCardId(rs.getString("prog_card_id"));
                whInventory.setProgCardQty(rs.getString("prog_card_qty"));
                whInventoryList.add(whInventory);
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
        return whInventoryList;
    }

    public Integer getCountMpExpiry() {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(RL.material_pass_expiry) AS count "
                    + "FROM hms_wh_retrieval_list RL, hms_wh_inventory_list IL "
                    + "WHERE DATE(RL.material_pass_expiry) >= DATE(NOW()) "
                    + "AND DATE(RL.material_pass_expiry) <= ADDDATE(DATE(NOW()),30) "
                    + "AND RL.retrieve_id = IL.retrieve_id "
            );

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("count");
            }
            LOGGER.info("count MP expiry..........." + count.toString());
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

    public List<WhInventory> getWhInventoryMpExpiryAlertList() {
        String sql = "SELECT RL.*, IL.*, DATE_FORMAT(RL.material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(RL.requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                + "DATE_FORMAT(RL.date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(IL.inventory_date,'%d %M %Y %h:%i %p') AS inventory_date_view, "
                + "DATE_FORMAT(RL.shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view, DATE_FORMAT(RL.received_date,'%d %M %Y %h:%i %p') AS received_date_view, "
                + "DATE_FORMAT(arrival_received_date,'%d %M %Y %h:%i %p') AS arrival_received_date_view "
                + "FROM hms_wh_retrieval_list RL, hms_wh_inventory_list IL "
                + "WHERE DATE(RL.material_pass_expiry) >= DATE(NOW()) "
                + "AND DATE(RL.material_pass_expiry) <= ADDDATE(DATE(NOW()),3) "
                + "AND RL.retrieve_id = IL.retrieve_id "
                + "ORDER BY RL.material_pass_expiry ASC ";
        List<WhInventory> whInventoryList = new ArrayList<WhInventory>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventory whInventory;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventory();
                whInventory.setRefId(rs.getString("IL.retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("IL.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whInventory.setEquipmentType(rs.getString("RL.equipment_type"));
                whInventory.setEquipmentId(rs.getString("RL.equipment_id"));
                whInventory.setPcbA(rs.getString("RL.pcb_a"));
                whInventory.setQtyQualA(rs.getString("RL.qty_qual_a"));
                whInventory.setPcbB(rs.getString("RL.pcb_b"));
                whInventory.setQtyQualB(rs.getString("RL.qty_qual_b"));
                whInventory.setPcbC(rs.getString("RL.pcb_c"));
                whInventory.setQtyQualC(rs.getString("RL.qty_qual_c"));
                whInventory.setPcbControl(rs.getString("RL.pcb_control"));
                whInventory.setQtyControl(rs.getString("RL.qty_control"));
                whInventory.setQuantity(rs.getString("RL.quantity"));
                whInventory.setRequestedBy(rs.getString("RL.requested_by"));
                whInventory.setRequestedDate(rs.getString("requested_date_view"));
                whInventory.setShippingDate(rs.getString("shipping_date_view"));
                whInventory.setRemarks(rs.getString("RL.remarks"));
                whInventory.setReceivedDate(rs.getString("received_date_view"));
                whInventory.setBarcodeVerify(rs.getString("RL.barcode_verify"));
                whInventory.setDateVerify(rs.getString("date_verify_view"));
                whInventory.setUserVerify(rs.getString("RL.user_verify"));
                whInventory.setInventoryDate(rs.getString("inventory_date_view"));
                whInventory.setInventoryRack(rs.getString("inventory_rack"));
                whInventory.setInventoryShelf(rs.getString("inventory_shelf"));
                whInventory.setInventoryBy(rs.getString("IL.inventory_by"));
                whInventory.setStatus(rs.getString("IL.status"));
                whInventory.setFlag(rs.getString("IL.flag"));
                whInventory.setArrivalReceivedDate(rs.getString("arrival_received_date"));
                whInventory.setPairingType(rs.getString("pairing_type"));
                whInventory.setLoadCardId(rs.getString("load_card_id"));
                whInventory.setLoadCardQty(rs.getString("load_card_qty"));
                whInventory.setProgCardId(rs.getString("prog_card_id"));
                whInventory.setProgCardQty(rs.getString("prog_card_qty"));
                whInventoryList.add(whInventory);
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
        return whInventoryList;
    }

    public Integer getCountMpExpiryAlert() {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(RL.material_pass_expiry) AS count "
                    + "FROM hms_wh_retrieval_list RL, hms_wh_inventory_list IL "
                    + "WHERE DATE(RL.material_pass_expiry) >= DATE(NOW()) "
                    + "AND DATE(RL.material_pass_expiry) <= ADDDATE(DATE(NOW()),3) "
                    + "AND RL.retrieve_id = IL.retrieve_id "
            );

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("count");
            }
            LOGGER.info("count MP expiry..........." + count.toString());
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

    public List<WhInventoryLog> getWhInventoryRetLog(String whInventoryId) {
        String sql = "SELECT *, DATE_FORMAT(timestamp,'%d %M %Y %h:%i %p') AS timestamp_view, DATE_FORMAT(verified_date,'%d %M %Y %h:%i %p') AS verified_date_view, DATE_FORMAT(received_date,'%d %M %Y %h:%i %p') AS received_date_view,"
                + "DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, DATE_FORMAT(shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view, "
                + "DATE_FORMAT(arrival_received_date,'%d %M %Y %h:%i %p') AS arrival_received_date_view, "
                + "CONCAT(FLOOR(HOUR(TIMEDIFF(shipping_date, arrival_received_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(shipping_date, arrival_received_date)), 24), ' hours, ', MINUTE(TIMEDIFF(shipping_date, arrival_received_date)), ' mins, ', SECOND(TIMEDIFF(shipping_date, arrival_received_date)), ' secs') AS ship_arr_rec, "
                + "CONCAT(FLOOR(HOUR(TIMEDIFF(arrival_received_date, inventory_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(arrival_received_date, inventory_date)), 24), ' hours, ', MINUTE(TIMEDIFF(arrival_received_date, inventory_date)), ' mins, ', SECOND(TIMEDIFF(arrival_received_date, inventory_date)), ' secs') AS arr_rec_inv, "
                + "CONCAT(FLOOR(HOUR(TIMEDIFF(shipping_date, inventory_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(shipping_date, inventory_date)), 24), ' hours, ', MINUTE(TIMEDIFF(shipping_date, inventory_date)), ' mins, ', SECOND(TIMEDIFF(shipping_date, inventory_date)), ' secs') AS ship_inv "
                + "FROM hms_wh_log L, hms_wh_retrieval_list R, hms_wh_inventory_list I "
                + "WHERE L.reference_id = R.retrieve_id AND R.retrieve_id = I.retrieve_id AND I.retrieve_id = '" + whInventoryId + "' "
                + "ORDER BY timestamp DESC";

//                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(shipping_date, received_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(shipping_date, received_date)), 24), ' hours, ', MINUTE(TIMEDIFF(shipping_date, received_date)), ' mins') AS ship_receive, "
//                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(received_date, date_verify)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(received_date, date_verify)), 24), ' hours, ', MINUTE(TIMEDIFF(received_date, date_verify)), ' mins') AS receive_verify, "
//                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(date_verify, inventory_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(date_verify, inventory_date)), 24), ' hours, ', MINUTE(TIMEDIFF(date_verify, inventory_date)), ' mins') AS verify_inventory, "
//                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(received_date, inventory_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(received_date, inventory_date)), 24), ' hours, ', MINUTE(TIMEDIFF(received_date, inventory_date)), ' mins') AS receive_inventory, "
//                    + "CONCAT(FLOOR(HOUR(TIMEDIFF(shipping_date, inventory_date)) / 24), ' days, ', MOD(HOUR(TIMEDIFF(shipping_date, inventory_date)), 24), ' hours, ', MINUTE(TIMEDIFF(shipping_date, inventory_date)), ' mins') AS shipping_inventory "
        List<WhInventoryLog> whInventoryList = new ArrayList<WhInventoryLog>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventoryLog whInventoryLog;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventoryLog = new WhInventoryLog();
                //log
                whInventoryLog.setId(rs.getString("L.id"));
                whInventoryLog.setReferenceId(rs.getString("reference_id"));
                whInventoryLog.setModuleId(rs.getString("module_id"));
                whInventoryLog.setModuleName(rs.getString("module_name"));
                whInventoryLog.setLogStatus(rs.getString("L.status"));
                whInventoryLog.setTimestamp(rs.getString("timestamp_view"));
                whInventoryLog.setLogVerifyDate(rs.getString("verified_date_view"));
                whInventoryLog.setLogVerifyBy(rs.getString("verified_by"));
                //retrieve
                whInventoryLog.setRetrieveId(whInventoryId);
                whInventoryLog.setMaterialPassNo(rs.getString("material_pass_no"));
                whInventoryLog.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whInventoryLog.setEquipmentType(rs.getString("equipment_type"));
                whInventoryLog.setEquipmentId(rs.getString("equipment_id"));
                whInventoryLog.setPcbA(rs.getString("pcb_a"));
                whInventoryLog.setQtyQualA(rs.getString("qty_qual_a"));
                whInventoryLog.setPcbB(rs.getString("pcb_b"));
                whInventoryLog.setQtyQualB(rs.getString("qty_qual_b"));
                whInventoryLog.setPcbC(rs.getString("pcb_c"));
                whInventoryLog.setQtyQualC(rs.getString("qty_qual_c"));
                whInventoryLog.setPcbControl(rs.getString("pcb_control"));
                whInventoryLog.setQtyControl(rs.getString("qty_control"));
                whInventoryLog.setQuantity(rs.getString("quantity"));
                whInventoryLog.setRequestedBy(rs.getString("requested_by"));
                whInventoryLog.setRequestedEmail(rs.getString("requested_email"));
                whInventoryLog.setRequestedDate(rs.getString("requested_date_view"));
                whInventoryLog.setShippingDate(rs.getString("shipping_date_view"));
                String remarks = rs.getString("remarks");
                if (remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whInventoryLog.setRemarks(remarks);
                whInventoryLog.setReceivedDate(rs.getString("received_date_view"));
                whInventoryLog.setBarcodeVerify(rs.getString("barcode_verify"));
                whInventoryLog.setDateVerify(rs.getString("date_verify"));
                whInventoryLog.setUserVerify(rs.getString("user_verify"));
                whInventoryLog.setStatus(rs.getString("R.status"));
                whInventoryLog.setFlag(rs.getString("flag"));
                whInventoryLog.setShipArrReceive(rs.getString("ship_arr_rec"));
                whInventoryLog.setArrReceiveInventory(rs.getString("arr_rec_inv"));
                whInventoryLog.setShipInventory(rs.getString("ship_inv"));
//                whInventoryLog.setShipReceive(rs.getString("ship_receive"));
//                whInventoryLog.setReceiveVerify(rs.getString("receive_verify"));
//                whInventoryLog.setVerifyInventory(rs.getString("verify_inventory"));
//                whInventoryLog.setReceiveInventory(rs.getString("receive_inventory"));
//                whInventoryLog.setShippingInventory(rs.getString("shipping_inventory"));
                //inventory
                whInventoryLog.setInventoryLoc(rs.getString("inventory_loc"));
                whInventoryLog.setInventoryRack(rs.getString("inventory_rack"));
                whInventoryLog.setInventoryShelf(rs.getString("inventory_shelf"));
                whInventoryLog.setInventoryBy(rs.getString("inventory_by"));
                whInventoryLog.setInventoryDate(rs.getString("inventory_date"));
                whInventoryLog.setArrivalReceivedDate(rs.getString("arrival_received_date_view"));
                whInventoryLog.setPairingType(rs.getString("pairing_type"));
                whInventoryLog.setLoadCardId(rs.getString("load_card_id"));
                whInventoryLog.setLoadCardQty(rs.getString("load_card_qty"));
                whInventoryLog.setProgCardId(rs.getString("prog_card_id"));
                whInventoryLog.setProgCardQty(rs.getString("prog_card_qty"));
                whInventoryLog.setBoxNo(rs.getString("box_no"));
                whInventoryLog.setGtsNo(rs.getString("gts_no"));
                whInventoryList.add(whInventoryLog);
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
        return whInventoryList;
    }

    public List<WhInventory> getQuery(String query) {
        String sql = "SELECT *, DATE_FORMAT(material_pass_expiry,'%d %M %Y') AS mp_expiry_view, DATE_FORMAT(requested_date,'%d %M %Y %h:%i %p') AS requested_date_view, "
                + "DATE_FORMAT(date_verify,'%d %M %Y %h:%i %p') AS date_verify_view, DATE_FORMAT(inventory_date,'%d %M %Y %h:%i %p') AS inventory_date_view, "
                + "DATE_FORMAT(arrival_received_date,'%d %M %Y %h:%i %p') AS arrival_received_date_view, DATE_FORMAT(shipping_date,'%d %M %Y %h:%i %p') AS shipping_date_view, "
                + "DATE_FORMAT(received_date,'%d %M %Y %h:%i %p') AS received_date_view "
                + "FROM hms_wh_retrieval_list R, hms_wh_inventory_list I "
                + "WHERE R.retrieve_id = I.retrieve_id AND " + query;
        List<WhInventory> whInventoryList = new ArrayList<WhInventory>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventory whInventory;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventory();
                whInventory.setId(rs.getString("I.id"));
                whInventory.setRefId(rs.getString("I.retrieve_id"));
                whInventory.setMaterialPassNo(rs.getString("I.material_pass_no"));
                whInventory.setMaterialPassExpiry(rs.getString("mp_expiry_view"));
                whInventory.setEquipmentType(rs.getString("equipment_type"));
                whInventory.setEquipmentId(rs.getString("equipment_id"));
                whInventory.setPcbA(rs.getString("pcb_a"));
                whInventory.setQtyQualA(rs.getString("qty_qual_a"));
                whInventory.setPcbB(rs.getString("pcb_b"));
                whInventory.setQtyQualB(rs.getString("qty_qual_b"));
                whInventory.setPcbC(rs.getString("pcb_c"));
                whInventory.setQtyQualC(rs.getString("qty_qual_c"));
                whInventory.setPcbControl(rs.getString("pcb_control"));
                whInventory.setQtyControl(rs.getString("qty_control"));
                whInventory.setQuantity(rs.getString("quantity"));
                String remarks = rs.getString("remarks");
                if (remarks == null || remarks.equals("null")) {
                    remarks = SpmlUtil.nullToEmptyString(rs.getString("remarks"));
                }
                whInventory.setRemarks(remarks);
                whInventory.setShippingDate(rs.getString("shipping_date_view"));
                whInventory.setReceivedDate(rs.getString("arrival_received_date_view"));
                whInventory.setRequestedBy(rs.getString("requested_by"));
                whInventory.setRequestedEmail(rs.getString("requested_email"));
                whInventory.setRequestedDate(rs.getString("requested_date_view"));
                whInventory.setShippingDate(rs.getString("shipping_date_view"));
                whInventory.setReceivedDate(rs.getString("received_date_view"));
                whInventory.setBarcodeVerify(rs.getString("barcode_verify"));
                whInventory.setDateVerify(rs.getString("date_verify_view"));
                whInventory.setUserVerify(rs.getString("user_verify"));
                whInventory.setInventoryDate(rs.getString("inventory_date_view"));
                whInventory.setInventoryRack(rs.getString("inventory_rack"));
                whInventory.setInventoryShelf(rs.getString("inventory_shelf"));
                whInventory.setInventoryBy(rs.getString("inventory_by"));
                whInventory.setStatus(rs.getString("I.status"));
                whInventory.setFlag(rs.getString("I.flag"));
                whInventory.setPairingType(rs.getString("pairing_type"));
                whInventory.setLoadCardId(rs.getString("load_card_id"));
                whInventory.setLoadCardQty(rs.getString("load_card_qty"));
                whInventory.setProgCardId(rs.getString("prog_card_id"));
                whInventory.setProgCardQty(rs.getString("prog_card_qty"));
                whInventory.setBoxNo(rs.getString("R.box_no"));
                whInventory.setGtsNo(rs.getString("R.gts_no"));
                whInventoryList.add(whInventory);
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
        return whInventoryList;
    }

    public List<WhInventory> getHardwareId() {
        String sql = "SELECT DISTINCT equipment_id "
                + "FROM hms_wh_retrieval_list R, hms_wh_inventory_list I "
                + "WHERE R.retrieve_id = I.retrieve_id "
                + "ORDER BY equipment_id ";
        List<WhInventory> hardwareIdList = new ArrayList<WhInventory>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventory whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhInventory();
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

    public List<WhInventory> getHardwareType() {
        String sql = "SELECT DISTINCT equipment_type "
                + "FROM hms_wh_retrieval_list R, hms_wh_inventory_list I "
                + "WHERE R.retrieve_id = I.retrieve_id "
                + "ORDER BY equipment_type ";
        List<WhInventory> hardwareTypeList = new ArrayList<WhInventory>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventory whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhInventory();
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

    public List<WhInventory> getRequestedBy() {
        String sql = "SELECT DISTINCT requested_by "
                + "FROM hms_wh_retrieval_list R, hms_wh_inventory_list I "
                + "WHERE R.retrieve_id = I.retrieve_id "
                + "ORDER BY requested_by ";
        List<WhInventory> requestedByList = new ArrayList<WhInventory>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventory whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhInventory();
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

    public List<WhInventory> getStatusR() {
        String sql = "SELECT DISTINCT status "
                + "FROM hms_wh_retrieval_list "
                + "ORDER BY status ";
        List<WhInventory> statusRList = new ArrayList<WhInventory>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventory whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhInventory();
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

    public List<WhInventory> getStatusI() {
        String sql = "SELECT DISTINCT status "
                + "FROM hms_wh_inventory_list "
                + "ORDER BY status ";
        List<WhInventory> statusIList = new ArrayList<WhInventory>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventory whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhInventory();
                whRetrieve.setStatus(rs.getString("status"));
                statusIList.add(whRetrieve);
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
        return statusIList;
    }

    public List<WhInventory> getRack() {
        String sql = "SELECT DISTINCT inventory_rack "
                + "FROM hms_wh_inventory_list "
                + "ORDER BY inventory_rack ";
        List<WhInventory> rackList = new ArrayList<WhInventory>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventory whRetrieve;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whRetrieve = new WhInventory();
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

    public List<WhInventory> getShelf() {
        String sql = "SELECT DISTINCT inventory_shelf "
                + "FROM hms_wh_inventory_list "
                + "ORDER BY inventory_shelf ";
        List<WhInventory> shelfList = new ArrayList<WhInventory>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventory whInventory;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventory = new WhInventory();
                whInventory.setInventoryShelf(rs.getString("inventory_shelf"));
                shelfList.add(whInventory);
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

    public QueryResult insertEqpt(WhInventory whInventory) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hms_wh_inventory_list (retrieve_id, material_pass_no, inventory_rack, inventory_shelf, inventory_by, status, flag, inventory_date)"
                    + "VALUES (?,?,?,?,?,?,?,NOW())", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, whInventory.getRefId());
            ps.setString(2, whInventory.getMaterialPassNo());
            ps.setString(3, whInventory.getInventoryRack());
            ps.setString(4, whInventory.getInventoryShelf());
            ps.setString(5, whInventory.getInventoryBy());
            ps.setString(6, whInventory.getStatus());
            ps.setString(7, whInventory.getFlag());

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
}
