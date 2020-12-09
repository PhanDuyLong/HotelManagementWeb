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
import java.util.HashMap;
import java.util.Map;
import javax.naming.NamingException;
import longpd.utils.DBConnection;

/**
 *
 * @author Administrator
 */
public class TblTypesDAO implements Serializable {

    public Map<String, String> getTypes() throws NamingException, SQLException {
        Map<String, String> typeMap = new HashMap();
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT typeID, typeName "
                        + "FROM tblTypes "
                        + "WHERE status = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, "Active");
                rs = stm.executeQuery();
                while (rs.next()) {
                    String id = rs.getString("typeID");
                    String name = rs.getString("typeName");
                    typeMap.put(id, name);
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
        return typeMap;
    }
}
