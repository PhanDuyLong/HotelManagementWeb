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
public class TblAreasDTO implements Serializable {

    private String areaID;
    private String areaName;
    private String status;

    public TblAreasDTO() {
    }

    public TblAreasDTO(String areaID, String areaName, String status) {
        this.areaID = areaID;
        this.areaName = areaName;
        this.status = status;
    }

    public String getAreaID() {
        return areaID;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
