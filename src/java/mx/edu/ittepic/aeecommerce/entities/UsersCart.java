/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.aeecommerce.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gustavo
 */
@Entity
@Table(name = "users_cart")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsersCart.findAll", query = "SELECT u FROM UsersCart u"),
    @NamedQuery(name = "UsersCart.findById", query = "SELECT u FROM UsersCart u WHERE u.id = :id"),
    @NamedQuery(name = "UsersCart.findByUserid", query = "SELECT u FROM UsersCart u WHERE u.userid = :userid"),
    @NamedQuery(name = "UsersCart.findByUseridPurchase", query = 
            "SELECT us.username AS user, p.productname AS product, COUNT(uc.productid) AS quantity, p.salepricemin AS priceperitem FROM UsersCart uc, Users us, Product p WHERE uc.userid=us.userid AND uc.productid=p.productid AND uc.userid= :userid AND uc.purchased = :purchased group by p.productname, us.username, p.salepricemin"
    ),
    @NamedQuery(name = "UsersCart.findByUseridPurchid", query = 
            "SELECT u FROM UsersCart u WHERE u.userid = :userid AND u.purchased= :purchased"
    ),
    @NamedQuery(name = "UsersCart.findByProductid", query = "SELECT u FROM UsersCart u WHERE u.productid = :productid"),
    @NamedQuery(name = "UsersCart.findByPurchased", query = "SELECT u FROM UsersCart u WHERE u.purchased = :purchased")})
public class UsersCart implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "userid")
    private int userid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "productid")
    private int productid;
    @Column(name = "purchased")
    private Boolean purchased;

    public UsersCart() {
    }

    public UsersCart(Integer id) {
        this.id = id;
    }

    public UsersCart(Integer id, int userid, int productid) {
        this.id = id;
        this.userid = userid;
        this.productid = productid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public Boolean getPurchased() {
        return purchased;
    }

    public void setPurchased(Boolean purchased) {
        this.purchased = purchased;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsersCart)) {
            return false;
        }
        UsersCart other = (UsersCart) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.ittepic.aeecommerce.entities.UsersCart[ id=" + id + " ]";
    }
    
}
