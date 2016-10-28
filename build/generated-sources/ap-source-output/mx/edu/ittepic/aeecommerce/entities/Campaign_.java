package mx.edu.ittepic.aeecommerce.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-27T21:28:09")
@StaticMetamodel(Campaign.class)
public class Campaign_ { 

    public static volatile SingularAttribute<Campaign, Date> enddate;
    public static volatile SingularAttribute<Campaign, Date> frecuency;
    public static volatile SingularAttribute<Campaign, Integer> campaignid;
    public static volatile SingularAttribute<Campaign, Boolean> cellphone;
    public static volatile SingularAttribute<Campaign, Date> startdate;
    public static volatile SingularAttribute<Campaign, String> message;
    public static volatile SingularAttribute<Campaign, Boolean> email;

}