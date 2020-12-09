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
public class TblHotelsDTO implements Serializable {

    private int hotelID;
    private String hotelName;
    private String hotelAddress;
    private String description;
    private String hotelPhone;
    private int amountOfRoom;
    private String areaID;
    private String image;
    private String status;

    public TblHotelsDTO() {
    }

    public TblHotelsDTO(int hotelID, String hotelName, String hotelAddress, String description, String hotelPhone, int amountOfRoom, String areaID, String image, String status) {
        this.hotelID = hotelID;
        this.hotelName = hotelName;
        this.hotelAddress = hotelAddress;
        this.description = description;
        this.hotelPhone = hotelPhone;
        this.amountOfRoom = amountOfRoom;
        this.areaID = areaID;
        this.image = image;
        this.status = status;
    }

    public int getHotelID() {
        return hotelID;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHotelPhone() {
        return hotelPhone;
    }

    public void setHotelPhone(String hotelPhone) {
        this.hotelPhone = hotelPhone;
    }

    public int getAmountOfRoom() {
        return amountOfRoom;
    }

    public void setAmountOfRoom(int amountOfRoom) {
        this.amountOfRoom = amountOfRoom;
    }

    public String getAreaID() {
        return areaID;
    }

    public void setAreaID(String areaID) {
        this.areaID = areaID;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
