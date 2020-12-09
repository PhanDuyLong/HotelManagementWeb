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
import longpd.dtos.TblBookingDetailsDTO;
import longpd.utils.DBConnection;

/**
 *
 * @author Administrator
 */
public class TblBookingDetailsDAO implements Serializable {

    public List<TblBookingDetailsDTO> getDetailsByRoomID(int roomID) throws NamingException, SQLException {
        List<TblBookingDetailsDTO> list = new ArrayList();
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT detailID, bookingID, roomID, amount, price, status "
                        + "FROM tblBookingDetails "
                        + "WHERE roomID = ? AND status = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, roomID);
                stm.setString(2, "Active");
                rs = stm.executeQuery();
                while (rs.next()) {
                    int detailID = rs.getInt("detailID");
                    String orderID = rs.getString("bookingID");
                    int rID = rs.getInt("roomID");
                    int amount = rs.getInt("amount");
                    float price = rs.getFloat("price");
                    String status = rs.getString("status");
                    list.add(new TblBookingDetailsDTO(detailID, orderID, rID, amount, price, status));
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

    public boolean insertBookingDetail(TblBookingDetailsDTO detail) throws SQLException, NamingException {
        boolean result = false;
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO tblBookingDetails(bookingID, roomID, amount, price, status) "
                        + "VALUES(?, ?, ?, ?, ?)";
                stm = con.prepareStatement(sql);
                stm.setString(1, detail.getBookingID());
                stm.setInt(2, detail.getRoomID());
                stm.setInt(3, detail.getAmount());
                stm.setFloat(4, detail.getPrice());
                stm.setString(5, detail.getStatus());
                int row = stm.executeUpdate();
                if (row > 0) {
                    result = true;
                }
            }
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }
    public TblBookingDetailsDTO getDetailByRoomIDAndBookingID(int roomID, String bookingID) throws NamingException, SQLException{
        TblBookingDetailsDTO result = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT detailID, bookingID, roomID, amount, price, status "
                        + "FROM tblBookingDetails "
                        + "WHERE roomID = ? AND bookingID = ? AND status = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, roomID);
                stm.setString(2, bookingID);
                stm.setString(3, "Active");
                rs = stm.executeQuery();
                if (rs.next()) {
                    int detailID = rs.getInt("detailID");
                    String bkID = rs.getString("bookingID");
                    int rID = rs.getInt("roomID");
                    int amount = rs.getInt("amount");
                    float price = rs.getFloat("price");
                    String status = rs.getString("status");
                    result = new TblBookingDetailsDTO(detailID, bkID, rID, amount, price, status);
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
    public List<TblBookingDetailsDTO> getDetailsByBookingID(String bookingID) throws NamingException, SQLException {
        List<TblBookingDetailsDTO> list = new ArrayList();
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT detailID, bookingID, roomID, amount, price, status "
                        + "FROM tblBookingDetails "
                        + "WHERE bookingID = ? AND status = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, bookingID);
                stm.setString(2, "Active");
                rs = stm.executeQuery();
                while (rs.next()) {
                    int detailID = rs.getInt("detailID");
                    String orderID = rs.getString("bookingID");
                    int rID = rs.getInt("roomID");
                    int amount = rs.getInt("amount");
                    float price = rs.getFloat("price");
                    String status = rs.getString("status");
                    list.add(new TblBookingDetailsDTO(detailID, orderID, rID, amount, price, status));
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
