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
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import longpd.dtos.TblRolesDTO;
import longpd.utils.DBConnection;

/**
 *
 * @author Administrator
 */
public class TblRolesDAO implements Serializable {

    public List<TblRolesDTO> getRoles() throws NamingException, SQLException {
        List<TblRolesDTO> list = new ArrayList();
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT roleID, roleName, status "
                        + "FROM tblRoles "
                        + "WHERE status = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, "Active");
                rs = stm.executeQuery();
                while (rs.next()) {
                    String id = rs.getString("roleID");
                    String name = rs.getString("roleName");
                    String status = rs.getString("status");
                    list.add(new TblRolesDTO(id, name, status));
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
        return list;
    }

    public String getRoleNameByID(String id) throws NamingException, SQLException {
        String roleName = "";
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT roleName, status "
                        + "FROM tblRoles "
                        + "WHERE roleID = ? AND status = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, id);
                stm.setString(2, "Active");
                rs = stm.executeQuery();
                if (rs.next()) {
                    roleName = rs.getString("roleName");
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
        return roleName;
    }
}
