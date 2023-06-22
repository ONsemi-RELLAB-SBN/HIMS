/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.onsemi.hms.dao;

import com.onsemi.hms.db.DB;
import com.onsemi.hms.model.WhMpList;
import com.onsemi.hms.model.WhWip;
import com.onsemi.hms.tools.QueryResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zbqb9x
 */
public class WhWipDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhWipDAO.class);
    private static final String NEW = "0101";
    private static final String RECEIVE = "0102";
    private static final String VERIFY = "0103";
    private static final String REGISTER = "0104";
    private static final String READY = "0105";
    private static final String SHIP = "0106";

    private final Connection conn;
    private final DataSource dataSource;

    public WhWipDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
        this.dataSource = db.getDataSource();
    }

    //<editor-fold defaultstate="collapsed" desc="INSERT STATEMENT">
    public QueryResult insertWhMpList(WhMpList whMpList) {
        QueryResult queryResult = new QueryResult();
        try {
            LOGGER.info("MASUK INSERT STATEMENT >>> ");
            String sql = "INSERT INTO hms_wh_mp_list (request_id, shipping_id, material_pass_no, created_by, created_date, status, box_no) VALUES (?,?,?,?,NOW(),?,?)";
            LOGGER.info("DAH BACA SQL QUERY");
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            LOGGER.info("TRY TO PREPARE THE STATEMENT");
            ps.setString(1, whMpList.getRequestId());
            ps.setString(2, whMpList.getShippingId());
            ps.setString(3, whMpList.getMaterialPassNo());
            ps.setString(4, whMpList.getCreatedBy());
            ps.setString(5, whMpList.getStatus());
            ps.setString(6, whMpList.getBoxNo());
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

    public QueryResult insertWhWip(WhWip wip) {
        QueryResult queryResult = new QueryResult();
        try {
            String sql = "INSERT INTO hms_wh_wip (request_id, gts_no, rms_event, intervals, quantity, shipment_date,created_date, status)"
                    + " VALUES (?,?,?,?,?,?,NOW(),?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, wip.getRequestId());
            ps.setString(2, wip.getGtsNo());
            ps.setString(3, wip.getRmsEvent());
            ps.setString(4, wip.getIntervals());
            ps.setString(5, wip.getQuantity());
            ps.setString(6, wip.getShipmentDate());
            ps.setString(7, wip.getStatus());
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UPDATE STATEMENT">
    public QueryResult updateStatus(String date, String by, String gtsNo, String data) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_wip SET " + date + " = NOW(), " + by + " = ?, status = ? WHERE gts_no = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            String status = "";
            String username = System.getProperty("user.name");
            LOGGER.info("LOGGER for xxx : " +username);
            ParameterDetailsDAO dao = new ParameterDetailsDAO();
            if (data.equalsIgnoreCase("receive")) {
                status = dao.getDetailByCode(RECEIVE);
            } else if (data.equalsIgnoreCase("verify")) {
                status = dao.getDetailByCode(VERIFY);
            } else if (data.equalsIgnoreCase("register")) {
                status = dao.getDetailByCode(REGISTER);
            } else if (data.equalsIgnoreCase("ready")) {
                status = dao.getDetailByCode(READY);
            } else if (data.equalsIgnoreCase("ship")) {
                status = dao.getDetailByCode(SHIP);
            }
            ps.setString(1, username);
            ps.setString(2, status);
            ps.setString(3, gtsNo);
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
//        try {
//            // do something here
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
        return queryResult;
    }

    /*
    public QueryResult updateStatus(String date, String by, String gtsNo, String data) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_wip SET " + date + " = NOW(), " + by + " = ?, status = ? WHERE gts_no = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            String status = "";
            String username = System.getProperty(user.name);
            LOGGER.info("LOGGER for xxx : " +username);
            ParameterDetailsDAO dao = new ParameterDetailsDAO();
            if (data.equalsIgnoreCase("receive")) {
                status = dao.getDetailByCode(RECEIVE);
            } else if (data.equalsIgnoreCase("verify")) {
                status = dao.getDetailByCode(VERIFY);
            } else if (data.equalsIgnoreCase("register")) {
                status = dao.getDetailByCode(REGISTER);
            } else if (data.equalsIgnoreCase("ready")) {
                status = dao.getDetailByCode(READY);
            } else if (data.equalsIgnoreCase("ship")) {
                status = dao.getDetailByCode(SHIP);
            }
            ps.setString(1, username);
            ps.setString(2, status);
            ps.setString(3, gtsNo);
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
     */
    public QueryResult updateStatus01(WhWip wip, String date, String by, String data) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_wip SET " + date + " = NOW(), " + by + " = ?, status = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            String maklumat = "";
            if (data.equalsIgnoreCase("reveice")) {
                maklumat = wip.getReceiveBy();
            } else if (data.equalsIgnoreCase("verify")) {
                maklumat = wip.getVerifyBy();
            } else if (data.equalsIgnoreCase("register")) {
                maklumat = wip.getRegisterBy();
            } else if (data.equalsIgnoreCase("ready")) {
                maklumat = wip.getReadyBy();
            } else if (data.equalsIgnoreCase("ship")) {
                maklumat = wip.getShipBy();
            }
            ps.setString(1, maklumat);
            ps.setString(2, wip.getStatus());
            ps.setString(3, wip.getId());
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

    public QueryResult updateReceive(WhWip wip) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_wip "
                + "SET receive_date = ?, receive_by = ?, status = ? "
                + "WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, wip.getReceiveDate());
            ps.setString(2, wip.getReceiveBy());
            ps.setString(3, wip.getStatus());
            ps.setString(4, wip.getId());
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

    public QueryResult updateVerify(WhWip wip) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_wip "
                + "SET verify_date = ?, verify_by = ?, status = ? "
                + "WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, wip.getVerifyDate());
            ps.setString(2, wip.getVerifyBy());
            ps.setString(3, wip.getStatus());
            ps.setString(4, wip.getId());
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

    public QueryResult updateRegister(WhWip wip) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_wip "
                + "SET register_date = ?, register_by = ?, status = ? "
                + "WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, wip.getRegisterDate());
            ps.setString(2, wip.getRegisterBy());
            ps.setString(3, wip.getStatus());
            ps.setString(4, wip.getId());
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

    public QueryResult updateReady(WhWip wip) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_wip SET ready_date = ?, ready_by = ?, status = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, wip.getReadyDate());
            ps.setString(2, wip.getReadyBy());
            ps.setString(3, wip.getStatus());
            ps.setString(4, wip.getId());
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

    public QueryResult updateShip(WhWip wip) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_wip SET ship_date = ?, ship_by = ?, status = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, wip.getShipDate());
            ps.setString(2, wip.getShipBy());
            ps.setString(3, wip.getStatus());
            ps.setString(4, wip.getId());
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DELETE STATEMENT">
    public QueryResult deleteWhWip(String whWipId) {
        QueryResult queryResult = new QueryResult();
        try {
            String sql = "DELETE FROM hms_wh_wip WHERE id = '" + whWipId + "'";
            PreparedStatement ps = conn.prepareStatement(sql);
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SELECT STATEMENT">
    public WhWip getWhWipById(String whWipId) {
        String sql = "SELECT * FROM hms_wh_wip WHERE id = '" + whWipId + "'";
        WhWip whShipping = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whShipping = new WhWip();
                whShipping.setId(rs.getString("id"));
                whShipping.setRequestId(rs.getString("request_id"));
                whShipping.setGtsNo(rs.getString("gts_no"));
                whShipping.setRmsEvent(rs.getString("rms_event"));
                whShipping.setIntervals(rs.getString("intervals"));
                whShipping.setQuantity(rs.getString("quantity"));
                whShipping.setShipmentDate(rs.getString("shipment_date"));
                whShipping.setStatus(rs.getString("status"));
                whShipping.setCreatedDate(rs.getString("created_date"));
                whShipping.setReceiveDate(rs.getString("receive_date"));
                whShipping.setReceiveBy(rs.getString("receive_by"));
                whShipping.setVerifyDate(rs.getString("verify_date"));
                whShipping.setVerifyBy(rs.getString("verify_by"));
                whShipping.setRegisterDate(rs.getString("register_date"));
                whShipping.setRegisterBy(rs.getString("register_by"));
                whShipping.setReadyDate(rs.getString("ready_date"));
                whShipping.setReadyBy(rs.getString("ready_by"));
                whShipping.setShipDate(rs.getString("ship_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setWipBox(rs.getString("wip_box"));
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
        return whShipping;
    }

    public WhWip getWhWipByRequestId(String requestId) {
        String sql = "SELECT * FROM hms_wh_wip WHERE request_id = '" + requestId + "'";
        WhWip whShipping = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whShipping = new WhWip();
                whShipping.setId(rs.getString("id"));
                whShipping.setRequestId(rs.getString("request_id"));
                whShipping.setGtsNo(rs.getString("gts_no"));
                whShipping.setRmsEvent(rs.getString("rms_event"));
                whShipping.setIntervals(rs.getString("intervals"));
                whShipping.setQuantity(rs.getString("quantity"));
                whShipping.setShipmentDate(rs.getString("shipment_date"));
                whShipping.setStatus(rs.getString("status"));
                whShipping.setCreatedDate(rs.getString("created_date"));
                whShipping.setReceiveDate(rs.getString("receive_date"));
                whShipping.setReceiveBy(rs.getString("receive_by"));
                whShipping.setVerifyDate(rs.getString("verify_date"));
                whShipping.setVerifyBy(rs.getString("verify_by"));
                whShipping.setRegisterDate(rs.getString("register_date"));
                whShipping.setRegisterBy(rs.getString("register_by"));
                whShipping.setReadyDate(rs.getString("ready_date"));
                whShipping.setReadyBy(rs.getString("ready_by"));
                whShipping.setShipDate(rs.getString("ship_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setWipBox(rs.getString("wip_box"));
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
        return whShipping;
    }

    public List<WhWip> getWhWipByStatus(String status) {
        String sql = "SELECT * FROM hms_wh_wip WHERE status IN ('" + status + "')";
        LOGGER.info("DATA LATEST : " + sql);
        List<WhWip> wipList = new ArrayList<WhWip>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhWip whShipping;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whShipping = new WhWip();
                whShipping.setId(rs.getString("id"));
                whShipping.setRequestId(rs.getString("request_id"));
                whShipping.setGtsNo(rs.getString("gts_no"));
                whShipping.setRmsEvent(rs.getString("rms_event"));
                whShipping.setIntervals(rs.getString("intervals"));
                whShipping.setQuantity(rs.getString("quantity"));
                whShipping.setShipmentDate(rs.getString("shipment_date"));
                whShipping.setStatus(rs.getString("status"));
                whShipping.setCreatedDate(rs.getString("created_date"));
                whShipping.setReceiveDate(rs.getString("receive_date"));
                whShipping.setReceiveBy(rs.getString("receive_by"));
                whShipping.setVerifyDate(rs.getString("verify_date"));
                whShipping.setVerifyBy(rs.getString("verify_by"));
                whShipping.setRegisterDate(rs.getString("register_date"));
                whShipping.setRegisterBy(rs.getString("register_by"));
                whShipping.setReadyDate(rs.getString("ready_date"));
                whShipping.setReadyBy(rs.getString("ready_by"));
                whShipping.setShipDate(rs.getString("ship_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setWipBox(rs.getString("wip_box"));
                wipList.add(whShipping);
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
        return wipList;
    }

    public List<WhWip> getWhWipByShipment() {
        String sql = "SELECT * FROM hms_wh_wip WHERE wip_box = ? ";
        List<WhWip> whShippingList = new ArrayList<WhWip>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhWip whShipping;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whShipping = new WhWip();
                whShipping.setId(rs.getString("id"));
                whShipping.setRequestId(rs.getString("request_id"));
                whShipping.setGtsNo(rs.getString("gts_no"));
                whShipping.setRmsEvent(rs.getString("rms_event"));
                whShipping.setIntervals(rs.getString("intervals"));
                whShipping.setQuantity(rs.getString("quantity"));
                whShipping.setShipmentDate(rs.getString("shipment_date"));
                whShipping.setStatus(rs.getString("status"));
                whShipping.setCreatedDate(rs.getString("created_date"));
                whShipping.setReceiveDate(rs.getString("receive_date"));
                whShipping.setReceiveBy(rs.getString("receive_by"));
                whShipping.setVerifyDate(rs.getString("verify_date"));
                whShipping.setVerifyBy(rs.getString("verify_by"));
                whShipping.setRegisterDate(rs.getString("register_date"));
                whShipping.setRegisterBy(rs.getString("register_by"));
                whShipping.setReadyDate(rs.getString("ready_date"));
                whShipping.setReadyBy(rs.getString("ready_by"));
                whShipping.setShipDate(rs.getString("ship_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setWipBox(rs.getString("wip_box"));
                whShippingList.add(whShipping);
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
        return whShippingList;
    }

    public Integer getCountExistingData(String id) {
        Integer count = null;
        try {
            String sql = "SELECT COUNT(*) AS count FROM hms_wh_wip WHERE request_id = '" + id + "' ";
            PreparedStatement ps = conn.prepareStatement(sql);
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
    //</editor-fold>

}
