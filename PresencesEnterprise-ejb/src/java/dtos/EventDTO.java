/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Event")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventDTO {

    private Long id;
    private String name;
    private String password;
    private String description;
    private String startDate;
    private String finishDate;
    private boolean openForEnroll;
    private boolean openForPresence;

    public EventDTO() {
    }

    public EventDTO(Long id, String name, String description, String startDate, String finishDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public void reset() {
        this.name = null;
        this.description = null;
        this.openForEnroll = false;
        this.finishDate = null;
        this.startDate = null;
        this.password = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public boolean isOpenForEnroll() {
        return openForEnroll;
    }

    public void setOpenForEnroll(boolean OpenForEnroll) {
        this.openForEnroll = OpenForEnroll;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isOpenForPresence() {
        return openForPresence;
    }

    public void setOpenForPresence(boolean openForPresence) {
        this.openForPresence = openForPresence;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
