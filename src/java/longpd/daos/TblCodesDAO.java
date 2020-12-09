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
import longpd.dtos.TblCodesDTO;
import longpd.utils.DBConnection;

/**
 *
 * @author Administrator
 */
public class TblCodesDAO implements Serializable {

    public TblCodesDTO getCodeByID(String codeID) throws NamingException, SQLException {
        TblCodesDTO result = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT codeID, codeName, discountPercent, createDate, expDate, status "
                        + "FROM tblCodes "
                        + "WHERE codeID = ? AND expDate >= ? AND status = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, codeID);
                stm.setDate(2, new java.sql.Date(new Date().getTime()));
                stm.setString(3, "Active");
                rs = stm.executeQuery();
                if (rs.next()) {
                    String cID = rs.getString("codeID");
                    String codeName = rs.getString("codeName");
                    int discountPercent = rs.getInt("discountPercent");
                    Date createDate = rs.getDate("createDate");
                    Date expDate = rs.getDate("expDate");
                    String status = rs.getString("status");
                    result = new TblCodesDTO(cID, codeName, discountPercent, createDate, expDate, status);
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
    public TblCodesDTO getCodeByIDAdmin(String codeID) throws NamingException, SQLException {
        TblCodesDTO result = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT codeID, codeName, discountPercent, createDate, expDate, status "
                        + "FROM tblCodes "
                        + "WHERE codeID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, codeID);
                rs = stm.executeQuery();
                if (rs.next()) {
                    String cID = rs.getString("codeID");
                    String codeName = rs.getString("codeName");
                    int discountPercent = rs.getInt("discountPercent");
                    Date createDate = rs.getDate("createDate");
                    Date expDate = rs.getDate("expDate");
                    String status = rs.getString("status");
                    result = new TblCodesDTO(cID, codeName, discountPercent, createDate, expDate, status);
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
