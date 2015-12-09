
package entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJBException;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "EVENTCATEGORIES",
uniqueConstraints =
@UniqueConstraint(columnNames = {"NAME"}))
@NamedQueries({
    @NamedQuery(
        name="getAllEventCategories",
        query="SELECT ca FROM EventCategory ca ORDER BY ca.id"
    )
})
public class EventCategory extends Category implements Serializable {

    @ManyToMany
    @JoinTable(name = "EVENTCATEGORIES_EVENTS",
            joinColumns
            = @JoinColumn(name = "EVENTCATEGORIES_ID", referencedColumnName = "ID"),
            inverseJoinColumns
            = @JoinColumn(name = "EVENTS_ID", referencedColumnName = "ID"))
    private List<Event> events;
    
    public EventCategory() {
        this.events = new LinkedList<>();
    }

    public EventCategory(String name) {
        this.name = name;
        this.events = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event){
        try {
            if (!events.contains(event)){
                this.events.add(event);
            }
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
    
    public void removeEvent(Event event){
        try {
            if (events.contains(event)){
                this.events.remove(event);
            }
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
    
    public int getNumberOfEvents(){
        return this.events.size();
    }    
    
    @Override
    public String toString() {
        return "entities.Category[id=" + id + "]: "+ name;
    }
}
