/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gustavo
 */
@Entity
@Table(name = "campaign")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Campaign.findAll", query = "SELECT c FROM Campaign c"),
    @NamedQuery(name = "Campaign.findByCampaignid", query = "SELECT c FROM Campaign c WHERE c.campaignid = :campaignid"),
    @NamedQuery(name = "Campaign.findByStartdate", query = "SELECT c FROM Campaign c WHERE c.startdate = :startdate"),
    @NamedQuery(name = "Campaign.findByEnddate", query = "SELECT c FROM Campaign c WHERE c.enddate = :enddate"),
    @NamedQuery(name = "Campaign.findByMessage", query = "SELECT c FROM Campaign c WHERE c.message = :message"),
    @NamedQuery(name = "Campaign.findByEmail", query = "SELECT c FROM Campaign c WHERE c.email = :email"),
    @NamedQuery(name = "Campaign.findByCellphone", query = "SELECT c FROM Campaign c WHERE c.cellphone = :cellphone"),
    @NamedQuery(name = "Campaign.findByFrecuency", query = "SELECT c FROM Campaign c WHERE c.frecuency = :frecuency")})
public class Campaign implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "campaignid")
    private Integer campaignid;
    @Column(name = "startdate")
    @Temporal(TemporalType.DATE)
    private Date startdate;
    @Column(name = "enddate")
    @Temporal(TemporalType.DATE)
    private Date enddate;
    @Size(max = 255)
    @Column(name = "message")
    private String message;
    @Column(name = "email")
    private Boolean email;
    @Column(name = "cellphone")
    private Boolean cellphone;
    @Column(name = "frecuency")
    @Temporal(TemporalType.TIME)
    private Date frecuency;

    public Campaign() {
    }

    public Campaign(Integer campaignid) {
        this.campaignid = campaignid;
    }

    public Integer getCampaignid() {
        return campaignid;
    }

    public void setCampaignid(Integer campaignid) {
        this.campaignid = campaignid;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public Boolean getCellphone() {
        return cellphone;
    }

    public void setCellphone(Boolean cellphone) {
        this.cellphone = cellphone;
    }

    public Date getFrecuency() {
        return frecuency;
    }

    public void setFrecuency(Date frecuency) {
        this.frecuency = frecuency;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (campaignid != null ? campaignid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Campaign)) {
            return false;
        }
        Campaign other = (Campaign) object;
        if ((this.campaignid == null && other.campaignid != null) || (this.campaignid != null && !this.campaignid.equals(other.campaignid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.ittepic.aeecommerce.entities.Campaign[ campaignid=" + campaignid + " ]";
    }
    
}
