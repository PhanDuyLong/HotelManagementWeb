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
import longpd.dtos.TblRoomsDTO;
import longpd.utils.DBConnection;

/**
 *
 * @author Administrator
 */
public class TblRoomsDAO implements Serializable {

    public List<TblRoomsDTO> getRoomsWithHotelIDAndAmount(int id, int amount) throws NamingException, SQLException {
        List<TblRoomsDTO> list = new ArrayList();
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT roomID, price, amount, hotelID, typeID, status "
                        + "FROM tblRooms "
                        + "WHERE hotelID = ? AND amount >= ? AND status = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, id);
                stm.setInt(2, amount);
                stm.setString(3, "Active");
                rs = stm.executeQuery();
                while (rs.next()) {
                    int roomID = rs.getInt("roomID");
                    float price = rs.getFloat("price");
                    int a = rs.getInt("amount");
                    int hotelID = rs.getInt("hotelID");;
                    String typeID = rs.getString("typeID");
                    String status = rs.getString("status");
                    list.add(new TblRoomsDTO(roomID, price, a, hotelID, typeID, status));
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

    public List<TblRoomsDTO> getRoomsWithHotelIDAndAmountAdmin(int id, int amount) throws NamingException, SQLException {
        List<TblRoomsDTO> list = new ArrayList();
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT roomID, price, amount, hotelID, typeID, status "
                        + "FROM tblRooms "
                        + "WHERE hotelID = ? AND amount >= ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, id);
                stm.setInt(2, amount);
                rs = stm.executeQuery();
                while (rs.next()) {
                    int roomID = rs.getInt("roomID");
                    float price = rs.getFloat("price");
                    int a = rs.getInt("amount");
                    int hotelID = rs.getInt("hotelID");;
                    String typeID = rs.getString("typeID");
                    String status = rs.getString("status");
                    list.add(new TblRoomsDTO(roomID, price, a, hotelID, typeID, status));
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

    public TblRoomsDTO getRoomByID(int id) throws NamingException, SQLException {
        TblRoomsDTO result = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT roomID, price, amount, hotelID, typeID, status "
                        + "FROM tblRooms "
                        + "WHERE roomID = ? AND status = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, id);
                stm.setString(2, "Active");
                rs = stm.executeQuery();
                if (rs.next()) {
                    int roomID = rs.getInt("roomID");
                    float price = rs.getFloat("price");
                    int amount = rs.getInt("amount");
                    int hotelID = rs.getInt("hotelID");;
                    String typeID = rs.getString("typeID");
                    String status = rs.getString("status");
                    result = new TblRoomsDTO(roomID, price, amount, hotelID, typeID, status);
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
    public TblRoomsDTO getRoomByIDAdmin(int id) throws NamingException, SQLException {
        TblRoomsDTO result = null;
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT roomID, price, amount, hotelID, typeID, status "
                        + "FROM tblRooms "
                        + "WHERE roomID = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, id);
                rs = stm.executeQuery();
                if (rs.next()) {
                    int roomID = rs.getInt("roomID");
                    float price = rs.getFloat("price");
                    int amount = rs.getInt("amount");
                    int hotelID = rs.getInt("hotelID");;
                    String typeID = rs.getString("typeID");
                    String status = rs.getString("status");
                    result = new TblRoomsDTO(roomID, price, amount, hotelID, typeID, status);
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
