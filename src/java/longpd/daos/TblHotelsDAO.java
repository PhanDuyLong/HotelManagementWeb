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
import longpd.dtos.TblHotelsDTO;
import longpd.utils.DBConnection;

/**
 *
 * @author Administrator
 */
public class TblHotelsDAO implements Serializable {

    public List<TblHotelsDTO> getHotelsByNameAreaAndRoomAmount(String name, String areaID, int amount) throws SQLException, NamingException {
        List<TblHotelsDTO> list = new ArrayList();
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {

                String sql = "SELECT hotelID, hotelName, hotelAddress, description, hotelPhone, amountOfRoom, areaID, image, status "
                        + "FROM tblHotels "
                        + "WHERE hotelName like ? AND areaID like ? AND amountOfRoom >= ? AND status = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, "%" + name + "%");
                stm.setString(2, "%" + areaID + "%");
                stm.setInt(3, amount);
                stm.setString(4, "Active");
                rs = stm.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("hotelID");
                    String hotelName = rs.getString("hotelName");;
                    String hotelAddress = rs.getString("hotelAddress");
                    String description = rs.getString("description");
                    String hotelPhone = rs.getString("hotelPhone");
                    int amountOfRoom = rs.getInt("amountOfRoom");
                    String aID = rs.getString("areaID");
                    String image = rs.getString("image");
                    String status = rs.getString("status");
                    list.add(new TblHotelsDTO(id, hotelName, hotelAddress, description, hotelPhone, amountOfRoom, aID, image, status));
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

    public List<TblHotelsDTO> getHotelsByNameAreaAndRoomAmountAdmin(String name, String areaID, int amount) throws SQLException, NamingException {
        List<TblHotelsDTO> list = new ArrayList();
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {

                String sql = "SELECT hotelID, hotelName, hotelAddress, description, hotelPhone, amountOfRoom, areaID, image, status "
                        + "FROM tblHotels "
                        + "WHERE hotelName like ? AND areaID like ? AND amountOfRoom >= ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, "%" + name + "%");
                stm.setString(2, "%" + areaID + "%");
                stm.setInt(3, amount);
                rs = stm.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("hotelID");
                    String hotelName = rs.getString("hotelName");;
                    String hotelAddress = rs.getString("hotelAddress");
                    String description = rs.getString("description");
                    String hotelPhone = rs.getString("hotelPhone");
                    int amountOfRoom = rs.getInt("amountOfRoom");
                    String aID = rs.getString("areaID");
                    String image = rs.getString("image");
                    String status = rs.getString("status");
                    list.add(new TblHotelsDTO(id, hotelName, hotelAddress, description, hotelPhone, amountOfRoom, aID, image, status));
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

    public String getHotelNameByID(int id) throws NamingException, SQLException {
        String result = "";
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT hotelName "
                        + "FROM tblHotels "
                        + "WHERE hotelID = ? AND status = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, id);
                stm.setString(2, "Active");
                rs = stm.executeQuery();
                if (rs.next()) {
                    result = rs.getString("hotelName");
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

    public String getHotelNameByIDAdmin(int id) throws NamingException, SQLException {
        String result = "";
        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            con = DBConnection.makeConnection();
            if (con != null) {
                String sql = "SELECT hotelName "
                        + "FROM tblHotels "
                        + "WHERE hotelID = ? AND status = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, id);
                stm.setString(2, "Active");
                rs = stm.executeQuery();
                if (rs.next()) {
                    result = rs.getString("hotelName");
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
