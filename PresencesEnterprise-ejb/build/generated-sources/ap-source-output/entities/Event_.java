package entities;

import entities.Attendant;
import entities.AttendantEvent;
import entities.EventCategory;
import entities.Manager;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-12-16T19:49:40")
@StaticMetamodel(Event.class)
public class Event_ { 

    public static volatile ListAttribute<Event, AttendantEvent> attendantsPresences;
    public static volatile ListAttribute<Event, Attendant> presentes;
    public static volatile SingularAttribute<Event, String> description;
    public static volatile ListAttribute<Event, Attendant> attendants;
    public static volatile SingularAttribute<Event, String> password;
    public static volatile SingularAttribute<Event, Boolean> openForPresence;
    public static volatile SingularAttribute<Event, Boolean> openForEnroll;
    public static volatile SingularAttribute<Event, String> name;
    public static volatile SingularAttribute<Event, String> finishDate;
    public static volatile SingularAttribute<Event, Long> id;
    public static volatile ListAttribute<Event, EventCategory> categories;
    public static volatile SingularAttribute<Event, String> startDate;
    public static volatile ListAttribute<Event, Manager> managers;

}