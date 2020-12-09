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
import longpd.dtos.TblAreasDTO;
import longpd.utils.DBConnection;

/**
 *
 * @author Administrator
 */
public class TblAreasDAO implements Serializable {

    public List<TblAreasDTO> getAreas() throws SQLException, NamingException {
        List<TblAreasDTO> list = new ArrayList();
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT areaID, areaName, status "
                        + "FROM tblAreas "
                        + "WHERE status = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, "Active");
                rs = stm.executeQuery();
                while (rs.next()) {
                    String id = rs.getString("areaID");
                    String name = rs.getString("areaName");
                    String status = rs.getString("status");
                    list.add(new TblAreasDTO(id, name, status));
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
}
