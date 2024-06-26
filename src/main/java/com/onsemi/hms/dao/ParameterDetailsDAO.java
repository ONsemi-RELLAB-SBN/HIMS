/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.onsemi.hms.dao;

import com.onsemi.hms.db.DB;
import com.onsemi.hms.model.ParameterDetails;
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
public class ParameterDetailsDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterDetailsDAO.class);
    private final Connection conn;
    private final DataSource dataSource;

    public ParameterDetailsDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
        this.dataSource = db.getDataSource();
    }

    public QueryResult insertParameterDetails(ParameterDetails parameterDetails) {
        LOGGER.info("FUNCTION insertParameterDetails");
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hms_parameter_details (master_code, detail_code, value, remark, created_by, created_date) VALUES (?,?,?,?,?,NOW())", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, parameterDetails.getMasterCode());
            ps.setString(2, parameterDetails.getDetailCode());
            ps.setString(3, parameterDetails.getValue());
            ps.setString(4, parameterDetails.getRemark());
            ps.setString(5, parameterDetails.getCreatedBy());
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

    /* COMMENT BECAUSE STILL NOT CREATE MASTER TABLE
    public QueryResult updateParameterDetails(ParameterDetails parameterDetails) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE hms_parameter_details SET master_code = ?, detail_code = ?, value = ?, remark = ?, modified_by = ?, modified_date = NOW() WHERE id = ?"
            );
            ps.setString(1, parameterDetails.getMasterCode());
            ps.setString(2, parameterDetails.getDetailCode());
            ps.setString(3, parameterDetails.getValue());
            ps.setString(4, parameterDetails.getRemark());
            ps.setString(5, parameterDetails.getModifiedBy());
            ps.setString(6, parameterDetails.getId());
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

    public QueryResult deleteParameterDetails(String parameterDetailsId) {
        LOGGER.info("FUNCTION deleteParameterDetails");
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM hms_parameter_details WHERE id = '" + parameterDetailsId + "'"
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

    public ParameterDetails getParameterDetails(String parameterDetailsId) {
        LOGGER.info("FUNCTION getParameterDetails");
        String sql = "SELECT * FROM hms_parameter_details WHERE id = '" + parameterDetailsId + "'";
        ParameterDetails parameterDetails = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                parameterDetails = new ParameterDetails();
                parameterDetails.setId(rs.getString("id"));
                parameterDetails.setMasterCode(rs.getString("master_code"));
                parameterDetails.setDetailCode(rs.getString("detail_code"));
                parameterDetails.setValue(rs.getString("value"));
                parameterDetails.setRemark(rs.getString("remark"));
                parameterDetails.setCreatedBy(rs.getString("created_by"));
                parameterDetails.setCreatedDate(rs.getString("created_date"));
                parameterDetails.setModifiedBy(rs.getString("modified_by"));
                parameterDetails.setModifiedDate(rs.getString("modified_date"));
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
        return parameterDetails;
    }

    public List<ParameterDetails> getParameterDetailsList() {
        LOGGER.info("FUNCTION getParameterDetailsList");
        String sql = "SELECT * FROM hms_parameter_details ORDER BY id ASC";
        List<ParameterDetails> parameterDetailsList = new ArrayList<ParameterDetails>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ParameterDetails parameterDetails;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                parameterDetails = new ParameterDetails();
                parameterDetails.setId(rs.getString("id"));
                parameterDetails.setMasterCode(rs.getString("master_code"));
                parameterDetails.setDetailCode(rs.getString("detail_code"));
                parameterDetails.setValue(rs.getString("value"));
                parameterDetails.setRemark(rs.getString("remark"));
                parameterDetails.setCreatedBy(rs.getString("created_by"));
                parameterDetails.setCreatedDate(rs.getString("created_date"));
                parameterDetails.setModifiedBy(rs.getString("modified_by"));
                parameterDetails.setModifiedDate(rs.getString("modified_date"));
                parameterDetailsList.add(parameterDetails);
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
        return parameterDetailsList;
    }

    public String getNextDetailCode(String masterCode) {
        LOGGER.info("FUNCTION getNextDetailCode");
        String sql = "SELECT LPAD(IFNULL(MAX(m.detail_code)+1, CONCAT('" + masterCode + "','001')),6,'0') AS code "
                    + " FROM hms_parameter_details m "
                    + " WHERE m.master_code = '" + masterCode + "'";
        String code = "";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                code = rs.getString("code");
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
        return code;
    }

    public QueryResult removeDetailsByMasterCode(String masterCode) {
        LOGGER.info("FUNCTION removeDetailsByMasterCode");
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM hms_parameter_details WHERE master_code = '" + masterCode + "'"
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

    public Integer getCountByMasterCode(String masterCode) {
        LOGGER.info("FUNCTION getCountByMasterCode");
        Integer count = null;
        String sql = "SELECT count(id) AS count FROM hms_parameter_details WHERE master_code = '" + masterCode + "'";
        try {
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
    
    /* FIX THE CODE BEFORE UNCOMMENT
    public List<ParameterDetails> getGroupParameterDetailList(String name, String masterCode) {
        String sql = "SELECT id, master_code AS masterCode, detail_code AS detailCode, value AS value, IF(name=\"" + name + "\",\"selected=''\",\"\") AS selected FROM hms_parameter_details "
                + "WHERE master_code = '" + masterCode + "' ORDER BY detail_code";
        List<ParameterDetails> parameterDetailList = new ArrayList<ParameterDetails>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ParameterDetails parameterDetails;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                parameterDetails = new ParameterDetails(
                        rs.getString("id"),
                        rs.getString("masterCode"),
                        rs.getString("detailCode"),
                        rs.getString("value"),
                        rs.getString("selected")
                );
                parameterDetailList.add(parameterDetails);
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
        return parameterDetailList;
    }
    */
    
    public List<ParameterDetails> getParameterDetailsListByMasterCode(String masterCode) {
        LOGGER.info("FUNCTION getParameterDetailsListByMasterCode");
        String sql = "SELECT * FROM hms_parameter_details WHERE master_code = '" + masterCode + "' ORDER BY id ASC";
        List<ParameterDetails> parameterDetailsList = new ArrayList<ParameterDetails>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ParameterDetails parameterDetails;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                parameterDetails = new ParameterDetails();
                parameterDetails.setId(rs.getString("id"));
                parameterDetails.setMasterCode(rs.getString("master_code"));
                parameterDetails.setDetailCode(rs.getString("detail_code"));
                parameterDetails.setValue(rs.getString("value"));
                parameterDetails.setRemark(rs.getString("remark"));
                parameterDetails.setCreatedBy(rs.getString("created_by"));
                parameterDetails.setCreatedDate(rs.getString("created_date"));
                parameterDetails.setModifiedBy(rs.getString("modified_by"));
                parameterDetails.setModifiedDate(rs.getString("modified_date"));
                parameterDetailsList.add(parameterDetails);
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
        return parameterDetailsList;
    }
    
    public List<ParameterDetails> getStatusParameter(String masterCode) {
        LOGGER.info("FUNCTION getStatusParameter");
        String sql = "SELECT value, detail_code FROM hms_parameter_details WHERE master_code = '" + masterCode + "' ORDER BY id ASC";
        List<ParameterDetails> parameterDetailsList = new ArrayList<ParameterDetails>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ParameterDetails parameterDetails;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                parameterDetails = new ParameterDetails();
                parameterDetails.setDetailCode(rs.getString("detail_code"));
                parameterDetails.setValue(rs.getString("value"));
                parameterDetailsList.add(parameterDetails);
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
        return parameterDetailsList;
    }
    
    public List<ParameterDetails> getTaskParameter(String masterCode, String name) {
        LOGGER.info("FUNCTION getTaskParameter");
        String sql = "SELECT id, master_code, value, detail_code, IF(value=\"" + name + "\",\"selected=''\",\"\") AS selected FROM hms_parameter_details WHERE master_code = '" + masterCode + "' ORDER BY id ASC";
        List<ParameterDetails> taskList = new ArrayList<ParameterDetails>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ParameterDetails parameterDetails;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                parameterDetails = new ParameterDetails(
                        rs.getString("id"),
                        rs.getString("master_code"),
                        rs.getString("detail_code"),
                        rs.getString("value"),
                        rs.getString("selected")
                );
                taskList.add(parameterDetails);
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
        return taskList;
    }
    
    public String getDetailByCode (String code) {
        LOGGER.info("FUNCTION getDetailByCode");
        String value = "";
        String sql = "SELECT GROUP_CONCAT(value SEPARATOR '\\',\\'') as value FROM hms_parameter_details WHERE detail_code IN ('"+code+"')";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                value = rs.getString("value");
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
        return value;
    }
    
    public String getURLPath (String code) {
        LOGGER.info("FUNCTION getURLPath");
        String value = "";
        String sql = "SELECT VALUE FROM hms_config WHERE NAME = '"+code+"'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                value = rs.getString("value");
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
        return value;
    }
    
    public String getFileName (String code) {
        LOGGER.info("FUNCTION geetFileName");
        String value = "";
        String sql = "SELECT VALUE FROM hms_config WHERE NAME = '"+code+"'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                value = rs.getString("value");
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
        return value;
    }

}