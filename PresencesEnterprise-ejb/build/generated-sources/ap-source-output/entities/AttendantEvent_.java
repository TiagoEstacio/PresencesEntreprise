package entities;

import entities.Attendant;
import entities.Event;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-12-16T21:45:49")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-12-16T21:15:58")
>>>>>>> a3754738cc8de3415777d26bbd4f2c7635f05897
@StaticMetamodel(AttendantEvent.class)
public class AttendantEvent_ { 

    public static volatile SingularAttribute<AttendantEvent, Long> eventId;
    public static volatile SingularAttribute<AttendantEvent, Attendant> attendant;
    public static volatile SingularAttribute<AttendantEvent, Boolean> isAttending;
    public static volatile SingularAttribute<AttendantEvent, Long> attendantId;
    public static volatile SingularAttribute<AttendantEvent, Event> event;

}