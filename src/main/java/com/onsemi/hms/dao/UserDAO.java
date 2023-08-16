package com.onsemi.hms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.onsemi.hms.db.DB;
import com.onsemi.hms.tools.QueryResult;
import com.onsemi.hms.model.User;
import com.onsemi.hms.model.UserAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);
    private final Connection conn;

    public UserDAO() {
        DB db = new DB();
        this.conn = db.getConnection();
    }

    public QueryResult insertUser(User user) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO hms_user (login_id, password, group_id, is_active, created_by, created_time) VALUES (?,?,?,'1',?,NOW())", Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getLoginId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getGroupId());
            ps.setString(4, user.getCreatedBy());
            queryResult.setResult(ps.executeUpdate());
            if (queryResult.getResult() == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    queryResult.setGeneratedKey(Integer.toString(rs.getInt(1)));
                }
                ps.close();
                ps = conn.prepareStatement(
                        "INSERT INTO hms_user_profile (user_id, fullname, email) VALUES (?,?,?)"
                );
                ps.setString(1, queryResult.getGeneratedKey());
                ps.setString(2, user.getFullname());
                ps.setString(3, user.getEmail());
                ps.executeUpdate();
                ps.close();
            }
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

    public QueryResult updateUser(User user) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE hms_user SET group_id = ?, is_active = ?, modified_by = ?, modified_time = NOW() WHERE id = ?"
            );
            ps.setString(1, user.getGroupId());
            ps.setString(2, user.getIsActive());
            ps.setString(3, user.getModifiedBy());
            ps.setString(4, user.getId());
            queryResult.setResult(ps.executeUpdate());
            ps.close();
            ps = conn.prepareStatement(
                    "UPDATE hms_user_profile SET fullname = ?, email = ?, modified_by = ?, modified_time = NOW() WHERE user_id = ?"
            );
            ps.setString(1, user.getFullname());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getModifiedBy());
            ps.setString(4, user.getId());
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

    public QueryResult updateGroup(String userId, String groupId) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE hms_user SET group_id = ? WHERE id = ?"
            );
            ps.setString(1, groupId);
            ps.setString(2, userId);
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

    public QueryResult deleteUser(String userId) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM hms_user WHERE id = '" + userId + "'"
            );
            queryResult.setResult(ps.executeUpdate());
            ps.close();
            if (queryResult.getResult() == 1) {
                ps = conn.prepareStatement(
                        "DELETE FROM hms_user_profile WHERE user_id = '" + userId + "'"
                );
                queryResult.setResult(ps.executeUpdate());
                ps.close();
            }
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

    public User getUser(String userId) {
        String sql = "SELECT u.*, ug.code AS group_code, ug.name AS group_name, up.fullname, up.email FROM hms_user u "
                + "LEFT JOIN hms_user_group ug ON (u.group_id = ug.id) "
                + "LEFT JOIN hms_user_profile up ON (u.id = up.user_id) "
                + "WHERE u.id = '" + userId + "' ORDER BY up.fullname";
        User user = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new User(
                        rs.getString("id"),
                        rs.getString("login_id"),
                        rs.getString("password"),
                        rs.getString("group_id"),
                        rs.getString("is_active"),
                        rs.getString("created_by"),
                        rs.getString("created_time"),
                        rs.getString("modified_by"),
                        rs.getString("modified_time"),
                        rs.getString("group_code"),
                        rs.getString("group_name"),
                        rs.getString("fullname"),
                        rs.getString("email")
                );
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
        return user;
    }

    public List<User> getUserList(String groupId) {
        String filterGroupId = "";
        if (!groupId.equals("")) {
            filterGroupId = "WHERE u.group_id = '" + groupId + "' ";
        }
        String sql = "SELECT u.*, ug.code AS group_code, ug.name AS group_name, up.fullname, up.email FROM hms_user u "
                + "LEFT JOIN hms_user_group ug ON (u.group_id = ug.id) "
                + "LEFT JOIN hms_user_profile up ON (u.id = up.user_id) "
                + filterGroupId
                + "ORDER BY up.fullname";
        List<User> userList = new ArrayList<User>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            User user;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new User(
                        rs.getString("id"),
                        rs.getString("login_id"),
                        rs.getString("password"),
                        rs.getString("group_id"),
                        rs.getString("is_active"),
                        rs.getString("created_by"),
                        rs.getString("created_time"),
                        rs.getString("modified_by"),
                        rs.getString("modified_time"),
                        rs.getString("group_code"),
                        rs.getString("group_name"),
                        rs.getString("fullname"),
                        rs.getString("email")
                );
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

    public Integer getCountByGroupId(String groupId) {
        Integer count = null;
        String sql = "SELECT count(id) AS count FROM hms_user WHERE group_id = '" + groupId + "'";
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

    //profile use
    public User getUserByLoginId(String loginId) {
        String sql = "SELECT u.*, IFNULL(ug.code, '') AS group_code, IFNULL(ug.name, '') AS group_name, up.fullname, up.email FROM hms_user u "
                + "LEFT JOIN hms_user_group ug ON (u.group_id = ug.id) LEFT JOIN hms_user_profile up ON (u.id = up.user_id) "
                + "WHERE u.login_id = '" + loginId + "' ORDER BY up.fullname";
        User user = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new User(
                        rs.getString("id"),
                        rs.getString("login_id"),
                        rs.getString("password"),
                        rs.getString("group_id"),
                        rs.getString("is_active"),
                        rs.getString("created_by"),
                        rs.getString("created_time"),
                        rs.getString("modified_by"),
                        rs.getString("modified_time"),
                        rs.getString("group_code"),
                        rs.getString("group_name"),
                        rs.getString("fullname"),
                        rs.getString("email")
                );
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
        return user;
    }

    public QueryResult updatePassword(User user) {
        QueryResult queryResult = new QueryResult();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE hms_user SET password = ?, modified_by = ?, modified_time = NOW() WHERE id = ?"
            );
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getModifiedBy());
            ps.setString(3, user.getId());
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

    public UserAccess getUserDetails(String userId) {
        String sql = "SELECT * "
                + "FROM hms_user u, hms_user_group g, hms_user_profile p "
                + "WHERE u.id = p.user_id AND u.group_id = g.id AND u.id = '" + userId + "' "
                + "ORDER BY u.id ASC ";
        UserAccess userAccess = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userAccess = new UserAccess();
                userAccess.setId(rs.getString("u.id"));
                userAccess.setLoginId(rs.getString("u.login_id"));
                userAccess.setPassword(rs.getString("u.password"));
                userAccess.setIsActive(rs.getString("u.is_active"));
                userAccess.setUserCreatedBy(rs.getString("created_by"));
                userAccess.setUserCreatedTime(rs.getString("u.created_time"));
                userAccess.setUserModifiedBy(rs.getString("u.modified_by"));
                userAccess.setUserModifiedTime(rs.getString("u.modified_time"));
                userAccess.setGroupId(rs.getString("g.id"));
                userAccess.setGroupCode(rs.getString("g.code"));
                userAccess.setGroupName(rs.getString("g.name"));
                userAccess.setGroupCreatedBy(rs.getString("g.created_by"));
                userAccess.setGroupCreatedTime(rs.getString("g.created_time"));
                userAccess.setGroupModifiedBy(rs.getString("g.modified_by"));
                userAccess.setGroupModifiedTime(rs.getString("g.modified_time"));
                userAccess.setProfileId(rs.getString("p.id"));
                userAccess.setFullname(rs.getString("p.fullname"));
                userAccess.setProfileModifiedBy(rs.getString("p.modified_by"));
                userAccess.setProfileModifiedTime(rs.getString("p.modified_time"));
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
        return userAccess;
    }

}