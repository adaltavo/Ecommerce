package mx.edu.ittepic.aeecommerce.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import mx.edu.ittepic.aeecommerce.entities.Product;
import mx.edu.ittepic.aeecommerce.entities.Sale;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-27T21:28:09")
@StaticMetamodel(Salesline.class)
public class Salesline_ { 

    public static volatile SingularAttribute<Salesline, Integer> saleslineid;
    public static volatile SingularAttribute<Salesline, Integer> quantity;
    public static volatile SingularAttribute<Salesline, Product> productid;
    public static volatile SingularAttribute<Salesline, Sale> saleid;
    public static volatile SingularAttribute<Salesline, Double> saleprice;
    public static volatile SingularAttribute<Salesline, Double> purchprice;

}