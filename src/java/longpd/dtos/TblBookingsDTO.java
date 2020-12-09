/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longpd.dtos;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class TblBookingsDTO implements Serializable, Comparable<TblBookingsDTO> {

    private String bookingID;
    private String userID;
    private Date bookingDate;
    private String phone;
    private String email;
    private float totalPrice;
    private Date checkInDate;
    private Date checkOutDate;
    private String codeID;
    private String status;
    private String historyStatus;

    public TblBookingsDTO() {
    }

    public TblBookingsDTO(String bookingID, String userID, Date bookingDate, String phone, String email, float totalPrice, Date checkInDate, Date checkOutDate, String codeID, String status, String historyStatus) {
        this.bookingID = bookingID;
        this.userID = userID;
        this.bookingDate = bookingDate;
        this.phone = phone;
        this.email = email;
        this.totalPrice = totalPrice;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.codeID = codeID;
        this.status = status;
        this.historyStatus = historyStatus;
    }

    public String getBookingID() {
        return bookingID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getCodeID() {
        return codeID;
    }

    public void setCodeID(String codeID) {
        this.codeID = codeID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHistoryStatus() {
        return historyStatus;
    }

    public void setHistoryStatus(String historyStatus) {
        this.historyStatus = historyStatus;
    }

    @Override
    public int compareTo(TblBookingsDTO o) {
        return o.bookingDate.compareTo(this.bookingDate);
    }

}
