/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Table(name = "AttendantEvents")
@Entity
@IdClass(AttendantEventId.class)
public class AttendantEvent implements Serializable {

    @Id
    private Long attendantId;

    @Id
    private Long eventId;
    
    @Column(name="IS_ATTENDING")
    private boolean isAttending;
    
    @ManyToOne
    @PrimaryKeyJoinColumn(name="ATTENDANTID", referencedColumnName="ID")
    private Attendant attendant;
    
    @ManyToOne
    @PrimaryKeyJoinColumn(name="EVENTID", referencedColumnName="ID")
    private Event event;
    

    @Override
    public String toString() {
        return "entities.AttendantEvent";
    }

    public Long getAttendantId() {
        return attendantId;
    }

    public void setAttendantId(Long attendantId) {
        this.attendantId = attendantId;
    }

    public boolean isIsAttending() {
        return isAttending;
    }

    public void setIsAttending(boolean isAttending) {
        this.isAttending = isAttending;
    }

    public Attendant getAttendant() {
        return attendant;
    }

    public void setAttendant(Attendant attendant) {
        this.attendant = attendant;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
    
    
}
