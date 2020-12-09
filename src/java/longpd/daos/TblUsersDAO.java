/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longpd.daos;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.naming.NamingException;
import longpd.dtos.TblUsersDTO;
import longpd.utils.DBConnection;

/**
 *
 * @author Administrator
 */
public class TblUsersDAO implements Serializable {

    public TblUsersDTO checkLogin(String userID, String password) throws NamingException, SQLException {
        TblUsersDTO user = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT userID, name, createDate, address, phone, roleID, status "
                        + "FROM tblUsers "
                        + "WHERE userID = ? AND password = ? AND NOT roleID = ? AND status = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, userID);
                stm.setString(2, password);
                stm.setString(3, "GS");
                stm.setString(4, "Active");
                rs = stm.executeQuery();
                if (rs.next()) {
                    String id = rs.getString("userID");
                    String name = rs.getString("name");
                    String pass = "***";
                    Date createDate = rs.getDate("createDate");
                    String address = rs.getString("address");
                    String phone = rs.getString("phone");
                    String roleID = rs.getString("roleID");
                    String status = rs.getString("status");
                    user = new TblUsersDTO(id, pass, name, createDate, address, phone, roleID, status);
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return user;
    }

    public boolean insertNewUser(TblUsersDTO user) throws NamingException, SQLException {
        boolean result = false;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO tblUsers(userID, password, name, createDate, address, phone, roleID, status) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                stm = con.prepareStatement(sql);
                stm.setString(1, user.getUserID());
                stm.setString(2, user.getPassword());
                stm.setString(3, user.getName());
                stm.setDate(4, new java.sql.Date(new Date().getTime()));
                stm.setString(5, user.getAddress());
                stm.setString(6, user.getPhone());
                stm.setString(7, user.getRoleID());
                stm.setString(8, user.getStatus());
                int row = stm.executeUpdate();
                if (row > 0) {
                    result = true;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }
}
