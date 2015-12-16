package entities;

import entities.Attendant;
import entities.Event;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-12-16T19:04:02")
@StaticMetamodel(AttendantEvent.class)
public class AttendantEvent_ { 

    public static volatile SingularAttribute<AttendantEvent, Long> eventId;
    public static volatile SingularAttribute<AttendantEvent, Attendant> attendant;
    public static volatile SingularAttribute<AttendantEvent, Boolean> isAttending;
    public static volatile SingularAttribute<AttendantEvent, Long> attendantId;
    public static volatile SingularAttribute<AttendantEvent, Event> event;

}