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
import java.util.Date;
import java.util.List;
import javax.naming.NamingException;
import longpd.dtos.TblBookingsDTO;
import longpd.utils.DBConnection;

/**
 *
 * @author Administrator
 */
public class TblBookingsDAO implements Serializable {

    public boolean checkBookingByIDAndDates(String bookingID, Date checkInDate, Date checkOutDate) throws NamingException, SQLException {
        boolean result = false;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT bookingID "
                        + "FROM tblBookings "
                        + "WHERE bookingID = ? AND (checkInDate = ? OR checkInDate = ? OR checkOutDate = ? OR checkOutDate = ? "
                        + "OR (checkInDate < ? AND checkOutDate > ?) OR (checkInDate < ? AND checkOutDate > ?) OR (checkInDate > ? AND checkOutDate < ?)) AND NOT status = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, bookingID);
                stm.setDate(2, new java.sql.Date(checkInDate.getTime()));
                stm.setDate(3, new java.sql.Date(checkOutDate.getTime()));
                stm.setDate(4, new java.sql.Date(checkInDate.getTime()));
                stm.setDate(5, new java.sql.Date(checkOutDate.getTime()));
                stm.setDate(6, new java.sql.Date(checkInDate.getTime()));
                stm.setDate(7, new java.sql.Date(checkInDate.getTime()));
                stm.setDate(8, new java.sql.Date(checkOutDate.getTime()));
                stm.setDate(9, new java.sql.Date(checkOutDate.getTime()));
                stm.setDate(10, new java.sql.Date(checkInDate.getTime()));
                stm.setDate(11, new java.sql.Date(checkOutDate.getTime()));
                stm.setString(12, "Cancelled");
                rs = stm.executeQuery();
                if (rs.next()) {
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

    public boolean checkExistedBookingID(String id) throws SQLException, NamingException {
        boolean result = false;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT bookingID "
                        + "FROM tblBookings "
                        + "WHERE bookingID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, id);
                rs = stm.executeQuery();
                if (rs.next()) {
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

    public boolean insertBooking(TblBookingsDTO booking) throws SQLException, NamingException {
        boolean result = false;
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "INSERT INTO tblBookings(bookingID, userID, bookingDate, phone, email, totalPrice, checkIndate, checkOutDate, codeID, status, historyStatus) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                stm = con.prepareStatement(sql);
                stm.setString(1, booking.getBookingID());
                stm.setString(2, booking.getUserID());
                stm.setTimestamp(3, new java.sql.Timestamp(booking.getBookingDate().getTime()));
                stm.setString(4, booking.getPhone());
                stm.setString(5, booking.getEmail());
                stm.setFloat(6, booking.getTotalPrice());
                stm.setDate(7, new java.sql.Date(booking.getCheckInDate().getTime()));
                stm.setDate(8, new java.sql.Date(booking.getCheckOutDate().getTime()));
                stm.setString(9, booking.getCodeID());
                stm.setString(10, "Unconfirmed");
                stm.setString(11, "Active");
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

    public TblBookingsDTO getBookingByID(String bookingID) throws NamingException, SQLException {
        TblBookingsDTO result = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT bookingID, userID, bookingDate, phone, email, totalPrice, checkInDate, checkOutDate, codeID, status, historyStatus "
                        + "FROM tblBookings "
                        + "WHERE bookingID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, bookingID);
                rs = stm.executeQuery();
                if (rs.next()) {
                    String bkID = rs.getString("bookingID");
                    String userID = rs.getString("bookingID");
                    Date bookingDate = rs.getTimestamp("bookingDate");
                    String phone = rs.getString("phone");
                    String email = rs.getString("email");
                    float totalPrice = rs.getFloat("totalPrice");
                    Date checkInDate = rs.getTimestamp("checkInDate");
                    Date checkOutDate = rs.getTimestamp("checkOutDate");
                    String codeID = rs.getString("codeID");
                    String status = rs.getString("status");
                    String historyStatus = rs.getString("historyStatus");
                    result = new TblBookingsDTO(bkID, userID, bookingDate, phone, email, totalPrice, checkInDate, checkOutDate, codeID, status, historyStatus);
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
    public boolean updateBooking(TblBookingsDTO booking) throws NamingException, SQLException{
        boolean result = false;
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "UPDATE tblBookings " + 
                        "SET status = ? " +
                        "WHERE bookingID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, booking.getStatus());
                stm.setString(2, booking.getBookingID());
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
    public List<TblBookingsDTO> getBookingsByBookingDateAndUserID(Date bookingDate, String userID) throws NamingException, SQLException{
        List<TblBookingsDTO> list = new ArrayList();
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT bookingID, userID, bookingDate, phone, email, totalPrice, checkInDate, checkOutDate, codeID, status, historyStatus "
                        + "FROM tblBookings "
                        + "WHERE bookingDate < DATEADD(day, 1, ?) AND bookingDate > DATEADD(day, -1, ?) AND userID = ? AND historyStatus = ?";
                stm = con.prepareStatement(sql);
                stm.setDate(1, new java.sql.Date(bookingDate.getTime()));
                stm.setDate(2, new java.sql.Date(bookingDate.getTime()));
                stm.setString(3, userID);
                stm.setString(4, "Active");
                rs = stm.executeQuery();
                while (rs.next()) {
                    String bkID = rs.getString("bookingID");
                    String uID = rs.getString("bookingID");
                    Date bkDate = rs.getTimestamp("bookingDate");
                    String phone = rs.getString("phone");
                    String email = rs.getString("email");
                    float totalPrice = rs.getFloat("totalPrice");
                    Date checkInDate = rs.getTimestamp("checkInDate");
                    Date checkOutDate = rs.getTimestamp("checkOutDate");
                    String codeID = rs.getString("codeID");
                    String status = rs.getString("status");
                    String historyStatus = rs.getString("historyStatus");
                    list.add(new TblBookingsDTO(bkID, uID, bkDate, phone, email, totalPrice, checkInDate, checkOutDate, codeID, status, historyStatus));
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
    public List<TblBookingsDTO> getBookingsByUserID(String userID) throws NamingException, SQLException{
        List<TblBookingsDTO> list = new ArrayList();
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT bookingID, userID, bookingDate, phone, email, totalPrice, checkInDate, checkOutDate, codeID, status, historyStatus "
                        + "FROM tblBookings "
                        + "WHERE userID = ? AND historyStatus = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, userID);
                stm.setString(2, "Active");
                rs = stm.executeQuery();
                while (rs.next()) {
                    String bkID = rs.getString("bookingID");
                    String uID = rs.getString("bookingID");
                    Date bkDate = rs.getTimestamp("bookingDate");
                    String phone = rs.getString("phone");
                    String email = rs.getString("email");
                    float totalPrice = rs.getFloat("totalPrice");
                    Date checkInDate = rs.getTimestamp("checkInDate");
                    Date checkOutDate = rs.getTimestamp("checkOutDate");
                    String codeID = rs.getString("codeID");
                    String status = rs.getString("status");
                    String historyStatus = rs.getString("historyStatus");
                    list.add(new TblBookingsDTO(bkID, uID, bkDate, phone, email, totalPrice, checkInDate, checkOutDate, codeID, status, historyStatus));
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
    public boolean updateBookingHistoryStatus(TblBookingsDTO booking) throws NamingException, SQLException{
        boolean result = false;
        Connection con = null;
        PreparedStatement stm = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "UPDATE tblBookings " + 
                        "SET historyStatus = ? " +
                        "WHERE bookingID = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, booking.getHistoryStatus());
                stm.setString(2, booking.getBookingID());
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
}
