package entities;

import entities.UserGroup;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-12-12T17:20:47")
=======
@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-12-12T17:41:54")
>>>>>>> d2828903042436e8a044049915cc65fccec2de32
@StaticMetamodel(User.class)
public abstract class User_ { 

    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, String> name;
    public static volatile SingularAttribute<User, Long> id;
    public static volatile SingularAttribute<User, String> email;
    public static volatile SingularAttribute<User, String> username;
    public static volatile SingularAttribute<User, UserGroup> group;

}