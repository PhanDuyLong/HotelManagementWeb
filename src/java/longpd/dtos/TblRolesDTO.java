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
public class TblRolesDTO implements Serializable {

    private String roleID;
    private String roleName;
    private String status;

    public TblRolesDTO() {
    }

    public TblRolesDTO(String roleID, String roleName, String status) {
        this.roleID = roleID;
        this.roleName = roleName;
        this.status = status;
    }

    public String getRoleID() {
        return roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
