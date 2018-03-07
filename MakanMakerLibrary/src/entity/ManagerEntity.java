/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Ismahfaris
 */
@Entity
public class ManagerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long managerId;
    
    @Column(unique = true, nullable = false)
    private String userName;
    
    @Column(nullable = false)
    private String password;
    
    public ManagerEntity(){
        
    }
    
    public ManagerEntity(String userName, String password){
        this();
        this.userName = userName;
        this.password = password;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getManagerId() != null ? getManagerId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ManagerEntity)) {
            return false;
        }
        ManagerEntity other = (ManagerEntity) object;
        if ((this.getManagerId() == null && other.getManagerId() != null) || (this.getManagerId() != null && !this.managerId.equals(other.managerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ManagerEntity[ id=" + getManagerId() + " ]";
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
}
