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
public class TblCodesDTO implements Serializable {

    private String codeID;
    private String codeName;
    private int discountPercent;
    private Date createDate;
    private Date expDate;
    private String status;

    public TblCodesDTO() {
    }

    public TblCodesDTO(String codeID, String codeName, int discountPercent, Date createDate, Date expDate, String status) {
        this.codeID = codeID;
        this.codeName = codeName;
        this.discountPercent = discountPercent;
        this.createDate = createDate;
        this.expDate = expDate;
        this.status = status;
    }

    public String getCodeID() {
        return codeID;
    }

    public void setCodeID(String codeID) {
        this.codeID = codeID;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
