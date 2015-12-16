package entities;

import entities.AttendantCategory;
import entities.AttendantEvent;
import entities.Event;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-12-16T21:45:49")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-12-16T21:15:58")
>>>>>>> a3754738cc8de3415777d26bbd4f2c7635f05897
@StaticMetamodel(Attendant.class)
public class Attendant_ extends User_ {

    public static volatile ListAttribute<Attendant, AttendantEvent> attendantsPresences;
    public static volatile ListAttribute<Attendant, AttendantCategory> categories;
    public static volatile ListAttribute<Attendant, Event> events;

}