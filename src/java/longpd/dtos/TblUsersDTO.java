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
public class TblUsersDTO implements Serializable {

    private String userID;
    private String password;
    private String name;
    private Date createDate;
    private String address;
    private String phone;
    private String roleID;
    private String status;

    public TblUsersDTO() {
    }

    public TblUsersDTO(String userID, String password, String name, Date createDate, String address, String phone, String roleID, String status) {
        this.userID = userID;
        this.password = password;
        this.name = name;
        this.createDate = createDate;
        this.address = address;
        this.phone = phone;
        this.roleID = roleID;
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
