/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author ITWannaBe
 */
@Entity
@Table(name = "ATTENDANTCATEGORIES",
uniqueConstraints =
@UniqueConstraint(columnNames = {"NAME"}))
@NamedQueries({
    @NamedQuery(
        name="getAllAttendantCategories",
        query="SELECT ca FROM AttendantCategory ca ORDER BY ca.id"
    )
})
public class AttendantCategory extends Category implements Serializable {

    @ManyToMany
    @JoinTable(name = "ATTENDANTCATEGORIES_ATTENDANTS",
            joinColumns
            = @JoinColumn(name = "ATTENDANTCATEGORIES_ID", referencedColumnName = "ID"),
            inverseJoinColumns
            = @JoinColumn(name = "ATTENDANTS_ID", referencedColumnName = "ID"))
    private List<Attendant> attendants;

    public AttendantCategory() {
        attendants = new LinkedList<>();
    }

    public AttendantCategory(String name) {
        this.name = name;
        attendants = new LinkedList<>();
    }
  
     public void addAttendant(Attendant attendant){
        try {
            if (!attendants.contains(attendant)){
                attendants.add(attendant);
            }
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
    
    public void removeAttendant(Attendant attendant){
        try {
            if (attendants.contains(attendant)){
                attendants.remove(attendant);
            }
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }
    
    public int getNumberOfAttendants(){
        return this.attendants.size();
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

    public List<Attendant> getAttendants() {
        return attendants;
    }

    public void setAttendants(List<Attendant> attendants) {
        this.attendants = attendants;
    }
    
    

    @Override
    public String toString() {
        return "entities.AttendantCategory[ id=" + id + " ]";
    }
    
}
