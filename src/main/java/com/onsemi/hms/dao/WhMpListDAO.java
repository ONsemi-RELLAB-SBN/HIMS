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
                    "INSERT INTO hms_mp_list (wh_ship_id, mp_no, mp_expiry_date, hardware_id, hardware_type, quantity, requested_by, requested_date, created_date, created_by) VALUES (?,?,?,?,?,?,?,?,NOW(),?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, whMpList.getWhShipId());
            ps.setString(2, whMpList.getMaterialPassNo());
            ps.setString(3, whMpList.getMaterialPassExpiry());
            ps.setString(4, whMpList.getEquipmentId());
            ps.setString(5, whMpList.getEquipmentType());
            ps.setString(6, whMpList.getQuantity());
            ps.setString(7, whMpList.getRequestedBy());
            ps.setString(8, whMpList.getRequestedDate());
            ps.setString(9, whMpList.getCreatedBy());
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
                    "UPDATE hms_mp_list SET wh_ship_id = ?, mp_no = ?, mp_expiry_date = ?, hardware_id = ?, hardware_type = ?, quantity = ?, requested_by = ?, requested_date = ? WHERE id = ?"
            );
            ps.setString(1, whMpList.getWhShipId());
            ps.setString(2, whMpList.getMaterialPassNo());
            ps.setString(3, whMpList.getMaterialPassExpiry());
            ps.setString(4, whMpList.getEquipmentId());
            ps.setString(5, whMpList.getEquipmentType());
            ps.setString(6, whMpList.getQuantity());
            ps.setString(7, whMpList.getRequestedBy());
            ps.setString(8, whMpList.getRequestedDate());
            ps.setString(9, whMpList.getRefId());
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
                    "DELETE FROM hms_mp_list WHERE id = '" + whMpListId + "'"
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

    public WhMpList getWhMpList(String whMpListId) {
        String sql = "SELECT * FROM hms_mp_list WHERE id = '" + whMpListId + "'";
        WhMpList whMpList = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whMpList = new WhMpList();
                whMpList.setRefId(rs.getString("id"));
                whMpList.setWhShipId(rs.getString("wh_ship_id"));
                whMpList.setMaterialPassNo(rs.getString("mp_no"));
                whMpList.setMaterialPassExpiry(rs.getString("mp_expiry_date"));
                whMpList.setEquipmentId(rs.getString("hardware_id"));
                whMpList.setEquipmentType(rs.getString("hardware_type"));
                whMpList.setQuantity(rs.getString("quantity"));
                whMpList.setRequestedBy(rs.getString("requested_by"));
                whMpList.setRequestedDate(rs.getString("requested_date"));
                whMpList.setCreatedDate(rs.getString("created_date"));
                whMpList.setCreatedBy(rs.getString("created_by"));
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

    public List<WhMpList> getWhMpListList() {
        String sql = "SELECT * FROM hms_wh_mp_list ORDER BY id ASC";
        List<WhMpList> whMpListList = new ArrayList<WhMpList>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhMpList whMpList;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whMpList = new WhMpList();
                whMpList.setRefId(rs.getString("id"));
                whMpList.setWhShipId(rs.getString("wh_ship_id"));
                whMpList.setMaterialPassNo(rs.getString("mp_no"));
                whMpList.setMaterialPassExpiry(rs.getString("mp_expiry_date"));
                whMpList.setEquipmentId(rs.getString("hardware_id"));
                whMpList.setEquipmentType(rs.getString("hardware_type"));
                whMpList.setQuantity(rs.getString("quantity"));
                whMpList.setRequestedBy(rs.getString("requested_by"));
                whMpList.setRequestedDate(rs.getString("requested_date"));
                whMpList.setCreatedDate(rs.getString("created_date"));
                whMpList.setCreatedBy(rs.getString("created_by"));
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
                    "DELETE FROM hms_wh_mp_list"
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
//        QueryResult queryResult = new QueryResult();
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(*) AS count FROM hms_wh_mp_list WHERE mp_no = '" + mpNo + "'"
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
}
