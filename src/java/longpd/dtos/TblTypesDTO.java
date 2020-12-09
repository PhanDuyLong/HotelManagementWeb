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
public class TblTypesDTO implements Serializable {

    private String typeID;
    private String typeName;
    private String status;

    public TblTypesDTO() {
    }

    public TblTypesDTO(String typeID, String typeName, String status) {
        this.typeID = typeID;
        this.typeName = typeName;
        this.status = status;
    }

    public String getTypeID() {
        return typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
