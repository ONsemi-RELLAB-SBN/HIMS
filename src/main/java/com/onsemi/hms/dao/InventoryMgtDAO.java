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
import com.onsemi.hms.model.WhInventoryMgt;
import com.onsemi.hms.tools.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryMgtDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryMgtDAO.class);
    private final Connection conn;
    private final DataSource dataSource;

    public InventoryMgtDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
        this.dataSource = db.getDataSource();
    }

    public QueryResult insertInventoryDetails(WhInventoryMgt whInventoryMgt) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hms_inventory_mgt (rack_id, shelf_id, hardware_id, material_pass_no, modified_date, date_created) VALUES (?,?,?,?,NOW(),NOW())", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, whInventoryMgt.getRackId());
            ps.setString(2, whInventoryMgt.getShelfId());
            ps.setString(3, whInventoryMgt.getHardwareId());
            ps.setString(4, whInventoryMgt.getMaterialPassNo());
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

    public QueryResult updateInventoryDetails(WhInventoryMgt whInventoryMgt) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE hms_inventory_mgt " +
                    "SET hardware_id = ?, material_pass_no = ?, modified_date = NOW() " +
                    "WHERE rack_id = ? AND shelf_id = ? "
            );
            ps.setString(1, whInventoryMgt.getHardwareId());
            ps.setString(2, whInventoryMgt.getMaterialPassNo());
            ps.setString(3, whInventoryMgt.getRackId());
            ps.setString(4, whInventoryMgt.getShelfId());
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
    
    public QueryResult updateInventoryRevert(WhInventoryMgt whInventoryMgt) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE hms_inventory_mgt " +
                    "SET hardware_id = ?, material_pass_no = ? " +
                    "WHERE rack_id = ? AND shelf_id = ? "
            );
            ps.setString(1, whInventoryMgt.getHardwareId());
            ps.setString(2, whInventoryMgt.getMaterialPassNo());
            ps.setString(3, whInventoryMgt.getRackId());
            ps.setString(4, whInventoryMgt.getShelfId());
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

    public QueryResult deleteInventoryDetails(String shelfId) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM hms_inventory_mgt WHERE shelf_id = '" + shelfId + "'"
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

    public WhInventoryMgt getInventoryDetails(String shelfId) {
        String sql = "SELECT *, DATE_FORMAT(modified_date,'%d %M %Y') AS modified_date_view, DATE_FORMAT(date_created,'%d %M %Y') AS date_created_view "
                   + "FROM hms_inventory_mgt "
                   + "WHERE shelf_id = '" + shelfId + "' ";
        WhInventoryMgt whInventoryMgt = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventoryMgt = new WhInventoryMgt();
                whInventoryMgt.setId(rs.getString("id"));
                whInventoryMgt.setRackId(rs.getString("rack_id"));
                whInventoryMgt.setShelfId(rs.getString("shelf_id"));
                whInventoryMgt.setHardwareId(rs.getString("hardware_id"));
                whInventoryMgt.setMaterialPassNo(rs.getString("material_pass_no"));
                whInventoryMgt.setDateCreated(rs.getString("date_created_view"));
                whInventoryMgt.setModifiedDate(rs.getString("modified_date_view"));
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
        return whInventoryMgt;
    }

    public List<WhInventoryMgt> getInventoryDetailsList(String query) {
        String sql = "SELECT *, DATE_FORMAT(modified_date,'%d %M %Y') AS modified_date_view, DATE_FORMAT(date_created,'%d %M %Y') AS date_created_view "
                + " FROM hms_inventory_mgt " + query + " ORDER BY rack_id ASC, shelf_id ";
        List<WhInventoryMgt> whInventoryMgtList = new ArrayList<WhInventoryMgt>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventoryMgt whInventoryMgt;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventoryMgt = new WhInventoryMgt();
                whInventoryMgt.setId(rs.getString("id"));
                whInventoryMgt.setRackId(rs.getString("rack_id"));
                whInventoryMgt.setShelfId(rs.getString("shelf_id"));
                whInventoryMgt.setHardwareId(rs.getString("hardware_id"));
                whInventoryMgt.setMaterialPassNo(rs.getString("material_pass_no"));
                whInventoryMgt.setDateCreated(rs.getString("date_created_view"));
                whInventoryMgt.setModifiedDate(rs.getString("modified_date_view"));
                whInventoryMgtList.add(whInventoryMgt);
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
        return whInventoryMgtList;
    }
    
    public List<WhInventoryMgt> getInventoryDetailsList2() {
        String sql = "SELECT DISTINCT rack_id FROM hms_inventory_mgt ORDER BY rack_id ASC";
        List<WhInventoryMgt> whInventoryMgtList = new ArrayList<WhInventoryMgt>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            WhInventoryMgt whInventoryMgt;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                whInventoryMgt = new WhInventoryMgt();
                whInventoryMgt.setRackId(rs.getString("rack_id"));
                whInventoryMgtList.add(whInventoryMgt);
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
        return whInventoryMgtList;
    }
    
    public Integer getCountShelf(String shelfId) {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) AS count FROM hms_inventory_mgt WHERE shelf_id = '" + shelfId + "' "
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
    
    public Integer getCountRack(String rackId) {
        Integer count = null;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) AS count FROM hms_inventory_mgt WHERE rack_id = '" + rackId + "' "
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