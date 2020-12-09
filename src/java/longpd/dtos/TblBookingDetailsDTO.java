/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longpd.dtos;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class TblBookingDetailsDTO implements Serializable {

    private int detailID;
    private String bookingID;
    private int roomID;
    private int amount;
    private float price;
    private String status;

    public TblBookingDetailsDTO() {
    }

    public TblBookingDetailsDTO(int detailID, String bookingID, int roomID, int amount, float price, String status) {
        this.detailID = detailID;
        this.bookingID = bookingID;
        this.roomID = roomID;
        this.amount = amount;
        this.price = price;
        this.status = status;
    }

    public int getDetailID() {
        return detailID;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
