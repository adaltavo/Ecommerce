package mx.edu.ittepic.aeecommerce.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mx.edu.ittepic.aeecommerce.entities.Salesline;
import mx.edu.ittepic.aeecommerce.entities.Users;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-27T21:28:09")
@StaticMetamodel(Sale.class)
public class Sale_ { 

    public static volatile SingularAttribute<Sale, Date> date;
    public static volatile SingularAttribute<Sale, Double> amount;
    public static volatile SingularAttribute<Sale, Integer> saleid;
    public static volatile ListAttribute<Sale, Salesline> saleslineList;
    public static volatile SingularAttribute<Sale, Users> userid;

}