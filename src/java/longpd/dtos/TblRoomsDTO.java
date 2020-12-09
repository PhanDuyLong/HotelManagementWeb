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
public class TblRoomsDTO implements Serializable {

    private int roomID;
    private float price;
    private int amount;
    private int hotelID;
    private String typeID;
    private String status;

    public TblRoomsDTO() {
    }

    public TblRoomsDTO(int roomID, float price, int amount, int hotelID, String typeID, String status) {
        this.roomID = roomID;
        this.price = price;
        this.amount = amount;
        this.hotelID = hotelID;
        this.typeID = typeID;
        this.status = status;
    }

    public int getRoomID() {
        return roomID;
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

    public int getHotelID() {
        return hotelID;
    }

    public void setHotelID(int hotelID) {
        this.hotelID = hotelID;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
