/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.onsemi.hms.dao;

import com.onsemi.hms.db.DB;
import com.onsemi.hms.model.EmailList;
import com.onsemi.hms.model.User;
import com.onsemi.hms.model.WhMpList;
import com.onsemi.hms.model.WhWip;
import com.onsemi.hms.model.WhWip0;
import com.onsemi.hms.model.WhWipShip;
import com.onsemi.hms.tools.QueryResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private static final String INVENTORY = "0107";
    private static final String REQUEST = "0108";

    private final Connection conn;
    private final DataSource dataSource;

    public WhWipDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
        this.dataSource = db.getDataSource();
    }

    //<editor-fold defaultstate="collapsed" desc="INSERT STATEMENT">
    public QueryResult insertWhMpList(WhMpList whMpList) {
        LOGGER.info("FUNCTION insertWhMpList");
        QueryResult queryResult = new QueryResult();
        try {
            String sql = "INSERT INTO hms_wh_mp_list (request_id, shipping_id, material_pass_no, created_by, created_date, status, box_no) VALUES (?,?,?,?,NOW(),?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
        LOGGER.info("FUNCTION insertWhWip");
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

    public QueryResult insertWhWipShip(WhWipShip wip) {
        LOGGER.info("FUNCTION insertWhWipShip");
        QueryResult queryResult = new QueryResult();
        try {
            String sql = "INSERT INTO hms_wh_wip_ship (wip_id, wip_ship_list, created_date) VALUES (?,?,NOW())";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, wip.getWipId());
            ps.setString(2, wip.getWipShipList());
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
    public QueryResult updateStatusByGts(String date, String by, String gtsNo, String data, String username) {
        LOGGER.info("FUNCTION updateStatusByGts");
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_wip SET " + date + " = NOW(), " + by + " = ?, status = ? WHERE gts_no = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            String status = "";
//            String username = System.getProperty("user.name");
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

    /*
    public QueryResult updateStatus(String date, String by, String gtsNo, String data) {
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_wip SET " + date + " = NOW(), " + by + " = ?, status = ? WHERE gts_no = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            String status = "";
            String username = System.getProperty(user.name);
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
        LOGGER.info("FUNCTION updateStatus01");
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
        LOGGER.info("FUNCTION updateReceive");
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

    public QueryResult updateVerify(String requestId, String username) {
        LOGGER.info("FUNCTION updateVerify");
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_wip "
                + "SET verify_date = NOW(), verify_by = ?, status = ? "
                + "WHERE request_id = ?";
        try {
//            String username = System.getProperty("user.name");
            ParameterDetailsDAO dao = new ParameterDetailsDAO();
            String status = dao.getDetailByCode(VERIFY);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, status);
            ps.setString(3, requestId);
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
        LOGGER.info("FUNCTION updateRegister");
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_wip SET register_date = NOW(), register_by = ?, ship_quantity = ?, status = ? WHERE id = ?";
//        String username = System.getProperty("user.name");
        ParameterDetailsDAO dao = new ParameterDetailsDAO();
        String status = dao.getDetailByCode(READY);
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, wip.getRegisterBy());
            ps.setString(2, wip.getShipQuantity());
            ps.setString(3, status);
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
        LOGGER.info("FUNCTION updateReady");
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
        LOGGER.info("FUNCTION updateShip");
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_wip SET ship_created_date = NOW(), ship_date = ?, ship_by = ?, shipping_list = ?, status = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, wip.getShipDate());
            ps.setString(2, wip.getShipBy());
            ps.setString(3, wip.getShippingList());
            ps.setString(4, wip.getStatus());
            ps.setString(5, wip.getId());
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

    public QueryResult updateProcess(String process, String date, String reqId) {
        LOGGER.info("FUNCTION updateProcess");
        QueryResult queryResult = new QueryResult();
        String sql = "";
        if (process.equalsIgnoreCase("loading")) {
            sql = "UPDATE hms_wh_wip SET load_date = '" + date + "' WHERE request_id = ? ";
        } else if (process.equalsIgnoreCase("unloading")) {
            sql = "UPDATE hms_wh_wip SET unload_date = '" + date + "' WHERE request_id = ? ";
        }
//        switch (process) {
//            case "loading":
//                sql = "UPDATE hms_wh_wip SET load_date = '"+date+"' WHERE request_id = ? ";
//                break;
//            case "unloading":
//                sql = "UPDATE hms_wh_wip SET unload_date = '"+date+"' WHERE request_id = ? ";
//                break;
//            default:
//                break;
//        }
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, reqId);
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
        LOGGER.info("FUNCTION deleteWhWip");
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
        LOGGER.info("FUNCTION getWhWipById");
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
                whShipping.setShipCreatedDate(rs.getString("ship_created_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setShipQuantity(rs.getString("ship_quantity"));
                whShipping.setShippingList(rs.getString("shipping_list"));
                whShipping.setLoadDate(rs.getString("load_date"));
                whShipping.setUnloadDate(rs.getString("unload_date"));
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
        LOGGER.info("FUNCTION getWhWipByRequestId");
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
                whShipping.setShipCreatedDate(rs.getString("ship_created_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setShipQuantity(rs.getString("ship_quantity"));
                whShipping.setShippingList(rs.getString("shipping_list"));
                whShipping.setLoadDate(rs.getString("load_date"));
                whShipping.setUnloadDate(rs.getString("unload_date"));
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
        LOGGER.info("FUNCTION getWhWipByStatus");
        String sql = "SELECT * FROM hms_wh_wip WHERE status IN ('" + status + "')";
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
                whShipping.setShipCreatedDate(rs.getString("ship_created_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setShipQuantity(rs.getString("ship_quantity"));
                whShipping.setShippingList(rs.getString("shipping_list"));
                whShipping.setLoadDate(rs.getString("load_date"));
                whShipping.setUnloadDate(rs.getString("unload_date"));
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

    public List<WhWip> getWhWipByShipment(String shipList) {
        LOGGER.info("FUNCTION getWhWipByShipment");
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(SHIP);
        String sql = "SELECT * FROM hms_wh_wip WHERE shipping_list = '" + shipList + "' AND status = '" + status + "'";
        List<WhWip> whShippingList = new ArrayList<WhWip>();
        LOGGER.info("FUNCTION getWhWipByShipment >>> " + sql);
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
                whShipping.setShipCreatedDate(rs.getString("ship_created_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setShipQuantity(rs.getString("ship_quantity"));
                whShipping.setShippingList(rs.getString("shipping_list"));
                whShipping.setLoadDate(rs.getString("load_date"));
                whShipping.setUnloadDate(rs.getString("unload_date"));
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

    public List<WhWip> getWipShipmentAll() {
        LOGGER.info("FUNCTION getWipShipment");
        String sql = "SELECT *, GROUP_CONCAT(rms_event SEPARATOR ', ') AS sub_data FROM hms_wh_wip WHERE shipping_list IS NOT NULL GROUP BY shipping_list";
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
                whShipping.setShipCreatedDate(rs.getString("ship_created_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setShipQuantity(rs.getString("ship_quantity"));
                whShipping.setShippingList(rs.getString("shipping_list"));
                whShipping.setLoadDate(rs.getString("load_date"));
                whShipping.setUnloadDate(rs.getString("unload_date"));
                whShipping.setDataAll(rs.getString("sub_data"));
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

    public List<WhWip> getWipShipment(String shippingList) {
        LOGGER.info("FUNCTION getWipShipment");
        String sql = "SELECT *, GROUP_CONCAT(rms_event SEPARATOR ', ') AS sub_data FROM hms_wh_wip WHERE shipping_list LIKE '" + shippingList + "%' GROUP BY shipping_list";
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
                whShipping.setShipCreatedDate(rs.getString("ship_created_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setShipQuantity(rs.getString("ship_quantity"));
                whShipping.setShippingList(rs.getString("shipping_list"));
                whShipping.setLoadDate(rs.getString("load_date"));
                whShipping.setUnloadDate(rs.getString("unload_date"));
                whShipping.setDataAll(rs.getString("sub_data"));
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

    public List<WhWip> getAllWipByShipList(String shipList) {
        LOGGER.info("FUNCTION getAllWipByShipList");
        String sql = "SELECT * FROM hms_wh_wip WHERE shipping_list = '" + shipList + "' ";
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
                whShipping.setShipCreatedDate(rs.getString("ship_created_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setShipQuantity(rs.getString("ship_quantity"));
                whShipping.setShippingList(rs.getString("shipping_list"));
                whShipping.setLoadDate(rs.getString("load_date"));
                whShipping.setUnloadDate(rs.getString("unload_date"));
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

    public List<WhWip> getWipByShipDateAll() {
        LOGGER.info("FUNCTION getWipByShipDateAll");
        String sql = "SELECT * FROM hms_wh_wip WHERE shipping_list IS NOT NULL GROUP BY ship_date";
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
                whShipping.setShipCreatedDate(rs.getString("ship_created_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setShipQuantity(rs.getString("ship_quantity"));
                whShipping.setShippingList(rs.getString("shipping_list"));
                whShipping.setLoadDate(rs.getString("load_date"));
                whShipping.setUnloadDate(rs.getString("unload_date"));
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

    public List<WhWip> getWipByShipDate(String shipList) {
        LOGGER.info("FUNCTION getWipByShipDate");
        String sql = "SELECT * FROM hms_wh_wip WHERE shipping_list IS NOT NULL AND shipping_list = '" + shipList + "'";
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
                whShipping.setShipCreatedDate(rs.getString("ship_created_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setShipQuantity(rs.getString("ship_quantity"));
                whShipping.setShippingList(rs.getString("shipping_list"));
                whShipping.setLoadDate(rs.getString("load_date"));
                whShipping.setUnloadDate(rs.getString("unload_date"));
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

    public WhWip getWipByRmsInterval(String rmsEvent, String intervals) {
        LOGGER.info("FUNCTION getWipByRmsInterval");
        String sql = "SELECT * FROM hms_wh_wip WHERE rms_event = '" + rmsEvent + "' AND intervals = '" + intervals + "'";
        WhWip whList = new WhWip();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whList.setId(rs.getString("id"));
                whList.setRequestId(rs.getString("request_id"));
                whList.setGtsNo(rs.getString("gts_no"));
                whList.setRmsEvent(rs.getString("rms_event"));
                whList.setIntervals(rs.getString("intervals"));
                whList.setQuantity(rs.getString("quantity"));
                whList.setShipmentDate(rs.getString("shipment_date"));
                whList.setStatus(rs.getString("status"));
                whList.setCreatedDate(rs.getString("created_date"));
                whList.setReceiveDate(rs.getString("receive_date"));
                whList.setReceiveBy(rs.getString("receive_by"));
                whList.setVerifyDate(rs.getString("verify_date"));
                whList.setVerifyBy(rs.getString("verify_by"));
                whList.setRegisterDate(rs.getString("register_date"));
                whList.setRegisterBy(rs.getString("register_by"));
                whList.setReadyDate(rs.getString("ready_date"));
                whList.setReadyBy(rs.getString("ready_by"));
                whList.setShipDate(rs.getString("ship_date"));
                whList.setShipCreatedDate(rs.getString("ship_created_date"));
                whList.setShipBy(rs.getString("ship_by"));
                whList.setShipQuantity(rs.getString("ship_quantity"));
                whList.setShippingList(rs.getString("shipping_list"));
                whList.setLoadDate(rs.getString("load_date"));
                whList.setUnloadDate(rs.getString("unload_date"));
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
        return whList;
    }

    public List<WhWip> getWipByGtsNo(String gtsNo) {
        LOGGER.info("FUNCTION getWipByGtsNo");
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(VERIFY);
        String sql = "SELECT * FROM hms_wh_wip WHERE gts_no = '" + gtsNo + "' AND status = '" + status + "'";
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
                whShipping.setShipCreatedDate(rs.getString("ship_created_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setShipQuantity(rs.getString("ship_quantity"));
                whShipping.setShippingList(rs.getString("shipping_list"));
                whShipping.setLoadDate(rs.getString("load_date"));
                whShipping.setUnloadDate(rs.getString("unload_date"));
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

    public List<WhWip> getQuery(String query) {
        LOGGER.info("FUNCTION getQuery");
        String sql = "SELECT * FROM hms_wh_wip WHERE gts_no IS NOT NULL" + query;
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
                whShipping.setShipCreatedDate(rs.getString("ship_created_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setShipQuantity(rs.getString("ship_quantity"));
                whShipping.setShippingList(rs.getString("shipping_list"));
                whShipping.setLoadDate(rs.getString("load_date"));
                whShipping.setUnloadDate(rs.getString("unload_date"));
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

    public List<WhWipShip> getWipShippedAll() {
        LOGGER.info("FUNCTION getWipShippedAll");
        String sql = "SELECT * FROM hms_wh_wip_ship";
        List<WhWipShip> wipList = new ArrayList<WhWipShip>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhWipShip whShipping;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whShipping = new WhWipShip();
                whShipping.setId(rs.getString("id"));
                whShipping.setWipId(rs.getString("wip_id"));
                whShipping.setWipShipList(rs.getString("wip_ship_list"));
                whShipping.setCreatedDate(rs.getString("created_date"));
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

    public List<WhWipShip> getLogWhWip() {
        LOGGER.info("FUNCTION getLogWhWip");
        String sql = "SELECT GROUP_CONCAT(wip_id) AS ID, STATUS, GROUP_CONCAT(rms_event) AS RMS, wip_ship_list AS SHIP, shipping_list AS NEW_SHIP, IF(shipping_list=wip_ship_list, \"OK\", \"DATA UPDATED\") AS NEW_STATUS FROM hms_wh_wip_ship A "
                    + " INNER JOIN hms_wh_wip B ON wip_id = B.id"
                    + " GROUP BY wip_ship_list";
        List<WhWipShip> wipList = new ArrayList<WhWipShip>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhWipShip whShipping;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whShipping = new WhWipShip();
                whShipping.setId(rs.getString("ID"));
                whShipping.setData01(rs.getString("STATUS"));
                whShipping.setData02(rs.getString("RMS"));
                whShipping.setWipShipList(rs.getString("SHIP"));
                whShipping.setData03(rs.getString("NEW_SHIP"));
                whShipping.setData04(rs.getString("NEW_STATUS"));
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

    public List<WhWipShip> getLogAll() {
        LOGGER.info("FUNCTION getLogWhWip");
        String sql = "SELECT * FROM hms_wh_wip_ship GROUP BY wip_ship_list;";
        List<WhWipShip> wipList = new ArrayList<WhWipShip>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhWipShip whShipping;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whShipping = new WhWipShip();
                whShipping.setWipShipList(rs.getString("wip_ship_list"));
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

    public List<User> getAllUser() {
        LOGGER.info("FUNCTION GET ALL USER");
        String sql = "SELECT * FROM hms_user_profile ";
        List<User> userList = new ArrayList<User>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            User user;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setEmail(rs.getString("email"));
                user.setFullname(rs.getString("fullname"));
                userList.add(user);
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
        return userList;
    }

    public Integer getCountExistingData(String id) {
        LOGGER.info("FUNCTION getCountExistingData");
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

    public Integer getCountData0hours(String id) {
        LOGGER.info("FUNCTION getCountData0hours");
        Integer count = null;
        try {
            String sql = "SELECT COUNT(*) AS count FROM hms_wh_wip_0 WHERE request_id = '" + id + "' ";
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

    public Integer getCount0hoursRequest(String id) {
        LOGGER.info("FUNCTION getCountData0hours");
        Integer count = null;
        ParameterDetailsDAO dao = new ParameterDetailsDAO();
        String status = dao.getDetailByCode(INVENTORY);
        try {
            String sql = "SELECT COUNT(*) AS count FROM hms_wh_wip_0 WHERE request_id = '" + id + "' AND wip_status = '" + status + "'";
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

    public Integer getCountByStatus(String status) {
        LOGGER.info("FUNCTIOn getCountByStatus");
        Integer count = null;

        try {
            String sql = "SELECT COUNT(*) AS count FROM hms_wh_wip WHERE status = '" + status + "' ";
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

    public String getWipGtsByRequestId(String requestId) {
        LOGGER.info("FUNCTION getWipGtsByRequestId");
        String data = "";
        try {
            String sql = "SELECT gts_no FROM hms_wh_wip WHERE request_id = '" + requestId + "' ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data = rs.getString("gts_no");
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
        return data;
    }

    public String getShipDateByShipList(String shippingList) {
        LOGGER.info("FUNCTION getCountByGtsNo");
        String data = "";
        try {
            String sql = "SELECT ship_date FROM hms_wh_wip WHERE shipping_list = '" + shippingList + "' LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data = rs.getString("ship_date");
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
        return data;
    }

    public Integer getCountByGtsNo(String requestId) {
        LOGGER.info("FUNCTION getCountByGtsNo");
        Integer count = null;
        String data = "";

        try {
            String sql = "SELECT gts_no FROM hms_wh_wip WHERE request_id = '" + requestId + "' ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data = rs.getString("gts_no");
            }
            rs.close();
            ps.close();

            if (data == null) {

            } else {
                sql = "SELECT COUNT(*) AS count FROM hms_wh_wip WHERE gts_no = '" + data + "' ";
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    count = rs.getInt("count");
                }
                rs.close();
                ps.close();
            }
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

    public Integer getCountByGtsNoAndStatus(String requestId, String status) {
        LOGGER.info("FUNCTION getCountByGtsNoAndStatus");
        Integer count = null;
        String data = "";

        try {
            String sql = "SELECT gts_no FROM hms_wh_wip WHERE request_id = '" + requestId + "' ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data = rs.getString("gts_no");
            }
            rs.close();
            ps.close();

            if (data == null) {

            } else {
                sql = "SELECT COUNT(*) AS count FROM hms_wh_wip WHERE gts_no = '" + data + "' AND status = '" + status + "'";
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    count = rs.getInt("count");
                }
                rs.close();
                ps.close();
            }
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

    public List<EmailList> getUserToInform(String task) {
        LOGGER.info("FUNCTION getUserToInform");
        String sql = "SELECT * FROM hms_email_list WHERE task = '" + task + "' ";
        List<EmailList> userList = new ArrayList<EmailList>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            EmailList user;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new EmailList();
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                userList.add(user);
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
        return userList;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FUNCTION">
    public static LocalDate getDateFromString(String string, DateTimeFormatter format) {
        LOGGER.info("FUNCTION getDateFromString");
        LocalDate date = LocalDate.parse(string, format);
        return date;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FUNCTION EMAIL TASK LIST">
    public List<EmailList> getEmailListAll() {
        LOGGER.info("FUNCTION getEmailListAll");
        String sql = "SELECT * FROM hms_email_list ";
        List<EmailList> userList = new ArrayList<EmailList>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            EmailList user;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new EmailList();
                user.setId(rs.getString("id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setTask(rs.getString("task"));
                userList.add(user);
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
        return userList;
    }

    public EmailList getEmailListById(String id) {
        LOGGER.info("FUNCTION getEmailListById");
        String sql = "SELECT * FROM hms_email_list WHERE id = '" + id + "'";
        EmailList emailList = new EmailList();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                emailList.setId(rs.getString("id"));
                emailList.setUsername(rs.getString("username"));
                emailList.setEmail(rs.getString("email"));
                emailList.setTask(rs.getString("task"));
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
        return emailList;
    }

    public QueryResult addEmailList(EmailList list) {
        LOGGER.info("FUNCTION addEmailList");
        QueryResult queryResult = new QueryResult();
        try {
            String sql = "INSERT INTO hms_email_list (username, email, task) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, list.getUsername());
            ps.setString(2, list.getEmail());
            ps.setString(3, list.getTask());
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

    public QueryResult updateEmailList(EmailList list) {
        LOGGER.info("FUNCTION updateEmailList");
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_email_list "
                + "SET username = ?, email = ?, task = ? "
                + "WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, list.getUsername());
            ps.setString(2, list.getEmail());
            ps.setString(3, list.getTask());
            ps.setString(4, list.getId());
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

    public QueryResult removeEmailList(String id) {
        LOGGER.info("FUNCTION removeEmailList");
        QueryResult queryResult = new QueryResult();
        try {
            String sql = "DELETE FROM hms_email_list WHERE id = '" + id + "'";
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

    //<editor-fold defaultstate="collapsed" desc="0 HOUR">
    //<editor-fold defaultstate="collapsed" desc="INSERT">
    public QueryResult insertWhWip0hour(WhWip0 wip) {
        LOGGER.info("FUNCTION insertWhWip0hour");
        QueryResult queryResult = new QueryResult();
        try {
            String sql = "INSERT INTO hms_wh_wip_0 (request_id, gts_no, rms_event, intervals, quantity, shipment_date, created_date, wip_status)"
                        + " VALUES (?,?,?,?,?,?,NOW(),?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, wip.getRequestId());
            ps.setString(2, wip.getGtsNo());
            ps.setString(3, wip.getRmsEvent());
            ps.setString(4, wip.getIntervals());
            ps.setString(5, wip.getQuantity());
            ps.setString(6, wip.getShipmentDate());
            ps.setString(7, wip.getWipStatus());
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
    
    //<editor-fold defaultstate="collapsed" desc="UPDATE">
    public QueryResult updateStatus0hourByGts(String date, String by, String gtsNo, String data, String username) {
        LOGGER.info("FUNCTION updateStatusByGts");
        QueryResult queryResult = new QueryResult();
        String sql = "UPDATE hms_wh_wip_0 SET " + date + " = NOW(), " + by + " = ?, wip_status = ? WHERE gts_no = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            String status = "";
//            String username = System.getProperty("user.name");
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

    public QueryResult registerInventory0hour(String rack, String shelf, String status, String requestId, String username) {
        LOGGER.info("FUNCTION registerInventory0hour");
        QueryResult qr = new QueryResult();
        String sql = "UPDATE hms_wh_wip_0 SET "
                    + " wip_status = ?, "
                    + " rack = ?, "
                    + " shelf = ?, "
                    + " register_by = ?, "
                    + " register_date = NOW() "
                    + " WHERE request_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, rack);
            ps.setString(3, shelf);
            ps.setString(4, username);
            ps.setString(5, requestId);
            qr.setResult(ps.executeUpdate());
            ps.close();
        } catch (SQLException e) {
            qr.setErrorMessage(e.getMessage());
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
        return qr;
    }

    public QueryResult requestWip0hour(String status, String requestBy, String requestId) {
        LOGGER.info("FUNCTION requestWip0hour");
        QueryResult qr = new QueryResult();
        String sql = "UPDATE hms_wh_wip_0 SET "
                    + " wip_status = ?, "
                    + " request_by = ?, "
                    + " request_date = NOW() "
                    + " WHERE request_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, requestBy);
            ps.setString(3, requestId);
            qr.setResult(ps.executeUpdate());
            ps.close();
        } catch (SQLException e) {
            qr.setErrorMessage(e.getMessage());
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
        return qr;
    }
    
    public QueryResult regToShip0hour(String requestId) {
        LOGGER.info("FUNCTION regToShip0hour");
        QueryResult qr = new QueryResult();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(READY);
        String sql = "UPDATE hms_wh_wip_0 SET "
                    + " wip_status = ?, "
                    + " ready_date = NOW() "
                    + " WHERE request_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, requestId);
            qr.setResult(ps.executeUpdate());
            ps.close();
        } catch (SQLException e) {
            qr.setErrorMessage(e.getMessage());
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
        return qr;
    }
    
    public QueryResult deleteShip0hour(String requestId) {
        LOGGER.info("FUNCTION deleteShip0hour");
        QueryResult qr = new QueryResult();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(REQUEST);
        String sql = "UPDATE hms_wh_wip_0 SET "
                    + " wip_status = ?, "
                    + " ready_date = null "
                    + " WHERE request_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, requestId);
            qr.setResult(ps.executeUpdate());
            ps.close();
        } catch (SQLException e) {
            qr.setErrorMessage(e.getMessage());
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
        return qr;
    }
    
    public QueryResult updateShip0hour(String requestId, String shipBy, String shipDate, String shipList) {
        LOGGER.info("FUNCTION updateShip0hour");
        QueryResult qr = new QueryResult();
        ParameterDetailsDAO pdao = new ParameterDetailsDAO();
        String status = pdao.getDetailByCode(SHIP);
        String sql = "UPDATE hms_wh_wip_0 SET "
                    + " wip_status = ?, "
                    + " ship_by = ?, "
                    + " ship_date = ?, "
                    + " ship_list = ? "
                    + " WHERE request_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, shipBy);
            ps.setString(3, shipDate);
            ps.setString(4, shipList);
            ps.setString(5, requestId);
            qr.setResult(ps.executeUpdate());
            ps.close();
        } catch (SQLException e) {
            qr.setErrorMessage(e.getMessage());
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
        return qr;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="SELECT">
    public WhWip0 getWhWip0hourByRequestId(String requestId) {
        LOGGER.info("FUNCTION getWhWip0hourByRequestId");
        String sql = "SELECT * FROM hms_wh_wip_0 WHERE request_id = '" + requestId + "'";
        WhWip0 whShipping = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whShipping = new WhWip0();
                whShipping.setId(rs.getString("id"));
                whShipping.setRequestId(rs.getString("request_id"));
                whShipping.setGtsNo(rs.getString("gts_no"));
                whShipping.setRmsEvent(rs.getString("rms_event"));
                whShipping.setIntervals(rs.getString("intervals"));
                whShipping.setQuantity(rs.getString("quantity"));
                whShipping.setShipmentDate(rs.getString("shipment_date"));
                whShipping.setWipStatus(rs.getString("wip_status"));
                whShipping.setCreatedDate(rs.getString("created_date"));
                whShipping.setVerifyDate(rs.getString("verify_date"));
                whShipping.setVerifyBy(rs.getString("verify_by"));
                whShipping.setRack(rs.getString("rack"));
                whShipping.setShelf(rs.getString("shelf"));
                whShipping.setRegisterDate(rs.getString("register_date"));
                whShipping.setRegisterBy(rs.getString("register_by"));
                whShipping.setRequestDate(rs.getString("request_date"));
                whShipping.setRequestBy(rs.getString("request_by"));
                whShipping.setReadyDate(rs.getString("ready_date"));
                whShipping.setShipDate(rs.getString("ship_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setShipList(rs.getString("ship_list"));
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
    
    public List<WhWip0> getAll0hourWipByShipList(String shipList) {
        String sql = "SELECT * FROM hms_wh_wip_0 WHERE ship_list = '" + shipList + "' ";
        LOGGER.info("FUNCTION getAllWipByShipList >> " + sql);
        List<WhWip0> wipList = new ArrayList<WhWip0>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhWip0 whShipping;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whShipping = new WhWip0();
                whShipping.setId(rs.getString("id"));
                whShipping.setRequestId(rs.getString("request_id"));
                whShipping.setGtsNo(rs.getString("gts_no"));
                whShipping.setRmsEvent(rs.getString("rms_event"));
                whShipping.setIntervals(rs.getString("intervals"));
                whShipping.setQuantity(rs.getString("quantity"));
                whShipping.setShipmentDate(rs.getString("shipment_date"));
                whShipping.setWipStatus(rs.getString("wip_status"));
                whShipping.setCreatedDate(rs.getString("created_date"));
                whShipping.setVerifyDate(rs.getString("verify_date"));
                whShipping.setVerifyBy(rs.getString("verify_by"));
                whShipping.setRack(rs.getString("rack"));
                whShipping.setShelf(rs.getString("shelf"));
                whShipping.setRegisterDate(rs.getString("register_date"));
                whShipping.setRegisterBy(rs.getString("register_by"));
                whShipping.setRequestDate(rs.getString("request_date"));
                whShipping.setRequestBy(rs.getString("request_by"));
                whShipping.setReadyDate(rs.getString("ready_date"));
                whShipping.setShipDate(rs.getString("ship_date"));
                whShipping.setShipBy(rs.getString("ship_by"));
                whShipping.setShipList(rs.getString("ship_list"));
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
    
    public List<WhWip0> getWip0ByStatus(String status) {
        String sql = "SELECT * FROM hms_wh_wip_0 WHERE wip_status IN ('" + status + "')";
        LOGGER.info("FUNCTION getWip0ByStatus : " + sql);
        List<WhWip0> wipList = new ArrayList<WhWip0>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhWip0 wip;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                wip = new WhWip0();
                wip.setId(rs.getString("id"));
                wip.setRequestId(rs.getString("request_id"));
                wip.setGtsNo(rs.getString("gts_no"));
                wip.setRmsEvent(rs.getString("rms_event"));
                wip.setIntervals(rs.getString("intervals"));
                wip.setQuantity(rs.getString("quantity"));
                wip.setShipmentDate(rs.getString("shipment_date"));
                wip.setCreatedDate(rs.getString("created_date"));
                wip.setWipStatus(rs.getString("wip_status"));
                wip.setVerifyBy(rs.getString("verify_date"));
                wip.setVerifyDate(rs.getString("verify_date"));
                wip.setRack(rs.getString("rack"));
                wip.setShelf(rs.getString("shelf"));
                wip.setRegisterBy(rs.getString("register_by"));
                wip.setRegisterDate(rs.getString("register_date"));
                wip.setRequestBy(rs.getString("request_by"));
                wip.setRequestDate(rs.getString("request_date"));
                wip.setShipBy(rs.getString("ship_by"));
                wip.setShipDate(rs.getString("ship_date"));
                wip.setShipList(rs.getString("ship_list"));
                wipList.add(wip);
            }
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
    
    public List<WhWip0> getWip0Shipment() {
        String sql = "SELECT *, GROUP_CONCAT(rms_event SEPARATOR ', ') AS sub_data FROM hms_wh_wip_0 WHERE ship_list IS NOT NULL GROUP BY ship_list";
        LOGGER.info("FUNCTION getWip0ByStatus : " + sql);
        List<WhWip0> wipList = new ArrayList<WhWip0>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhWip0 wip;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                wip = new WhWip0();
                wip.setId(rs.getString("id"));
                wip.setRequestId(rs.getString("request_id"));
                wip.setGtsNo(rs.getString("gts_no"));
                wip.setRmsEvent(rs.getString("rms_event"));
                wip.setIntervals(rs.getString("intervals"));
                wip.setQuantity(rs.getString("quantity"));
                wip.setShipmentDate(rs.getString("shipment_date"));
                wip.setCreatedDate(rs.getString("created_date"));
                wip.setWipStatus(rs.getString("wip_status"));
                wip.setVerifyBy(rs.getString("verify_date"));
                wip.setVerifyDate(rs.getString("verify_date"));
                wip.setRack(rs.getString("rack"));
                wip.setShelf(rs.getString("shelf"));
                wip.setRegisterBy(rs.getString("register_by"));
                wip.setRegisterDate(rs.getString("register_date"));
                wip.setRequestBy(rs.getString("request_by"));
                wip.setRequestDate(rs.getString("request_date"));
                wip.setShipBy(rs.getString("ship_by"));
                wip.setShipDate(rs.getString("ship_date"));
                wip.setShipList(rs.getString("ship_list"));
                wip.setDataShip(rs.getString("sub_data"));
                wipList.add(wip);
            }
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
    
    public String getShipDateByRequestId(String requestId) {
        String data = "";
        String sql = "SELECT ship_date FROM hms_wh_wip WHERE request_id = '" + requestId + "'";
        LOGGER.info("FUNCTION getShipDateByRequestId : " +sql);
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data = rs.getString("ship_date");
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
        return data;
    }
    
    public String getStorageShipDateByShipList(String shippingList) {
        LOGGER.info("FUNCTION getStorageShipDateByShipList");
        String data = "";
        try {
            String sql = "SELECT ship_date FROM hms_wh_wip_0 WHERE ship_list = '" + shippingList + "' LIMIT 1";
            LOGGER.info("LOGGER for getStorageShipDateByShipList : " +sql);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data = rs.getString("ship_date");
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
        return data;
    }
    //</editor-fold>
    
    //</editor-fold>

}