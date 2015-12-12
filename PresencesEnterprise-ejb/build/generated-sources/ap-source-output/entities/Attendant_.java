package entities;

import entities.AttendantCategory;
import entities.Event;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-12-12T17:20:47")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-12-12T17:41:54")
>>>>>>> d2828903042436e8a044049915cc65fccec2de32
@StaticMetamodel(Attendant.class)
public class Attendant_ extends User_ {

    public static volatile ListAttribute<Attendant, AttendantCategory> categories;
    public static volatile ListAttribute<Attendant, Event> events;

}