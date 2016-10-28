package mx.edu.ittepic.aeecommerce.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mx.edu.ittepic.aeecommerce.entities.Company;
import mx.edu.ittepic.aeecommerce.entities.Role;
import mx.edu.ittepic.aeecommerce.entities.Sale;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-27T21:28:09")
@StaticMetamodel(Users.class)
public class Users_ { 

    public static volatile SingularAttribute<Users, String> neigborhood;
    public static volatile SingularAttribute<Users, String> country;
    public static volatile SingularAttribute<Users, Character> gender;
    public static volatile SingularAttribute<Users, String> city;
    public static volatile SingularAttribute<Users, Role> roleid;
    public static volatile ListAttribute<Users, Sale> saleList;
    public static volatile SingularAttribute<Users, String> streetnumber;
    public static volatile SingularAttribute<Users, String> photo;
    public static volatile SingularAttribute<Users, Integer> userid;
    public static volatile SingularAttribute<Users, String> zipcode;
    public static volatile SingularAttribute<Users, String> password;
    public static volatile SingularAttribute<Users, Company> companyid;
    public static volatile SingularAttribute<Users, String> phone;
    public static volatile SingularAttribute<Users, String> street;
    public static volatile SingularAttribute<Users, String> cellphone;
    public static volatile SingularAttribute<Users, String> state;
    public static volatile SingularAttribute<Users, String> region;
    public static volatile SingularAttribute<Users, String> email;
    public static volatile SingularAttribute<Users, String> username;

}