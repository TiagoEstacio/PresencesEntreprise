/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import static com.sun.xml.ws.security.addressing.impl.policy.Constants.logger;
import dtos.AttendantDTO;
import dtos.CategoryDTO;
import dtos.EventCategoryDTO;
import dtos.EventDTO;
import dtos.ManagerDTO;
import ejbs.AttendantBean;
import ejbs.CategoryBean;
import ejbs.EventBean;
import ejbs.ManagerBean;
import exceptions.AttendantEnrolledException;
import exceptions.AttendantNotEnrolledException;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistsException;
import exceptions.EventEnrolledException;
import exceptions.EventNotEnrolledException;
import exceptions.ManagerEnrolledException;
import exceptions.ManagerNotEnrolledException;
import exceptions.MyConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class EventManager {

    @EJB
    private AttendantBean attendantBean;
    @EJB
    private EventBean eventBean;
    @EJB
    private CategoryBean categoryBean;
    @EJB
    private ManagerBean managerBean;

    private EventDTO newEvent;
    private EventDTO currentEvent;

    private List<String> attendantsSelected;
    private List<String> categoriesSelected;

    private List<AttendantDTO> attendantsDisponiveisSelected = new ArrayList<>();
    private List<ManagerDTO> managersDisponiveisSelected = new ArrayList<>();
    private List<String> managersSelected;

    public EventManager() {
        newEvent = new EventDTO();
        currentEvent = new EventDTO();
    }

    public String createEvent() {
        try {
            if (newEvent.getStartDate().matches("^(?=\\d)(?:(?:31(?!.(?:0?[2469]|11))|(?:30|29)(?!.0?2)|29"
                    + "(?=.0?2.(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]"
                    + "|[3579][26])00)))(?:\\x20|$))|(?:2[0-8]|1\\d|0?[1-9]))([-./])(?:1[012]|0?[1-9])\\1"
                    + "(?:1[6-9]|[2-9]\\d)?\\d\\d(?:(?=\\x20\\d)\\x20|$))(|([01]\\d|2[0-3])(:[0-5]\\d){1,2})?$")
                    && newEvent.getFinishDate().matches("^(?=\\d)(?:(?:31(?!.(?:0?[2469]|11))|(?:30|29)(?!.0?2)|29"
                            + "(?=.0?2.(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]"
                            + "|[3579][26])00)))(?:\\x20|$))|(?:2[0-8]|1\\d|0?[1-9]))([-./])(?:1[012]|0?[1-9])\\1"
                            + "(?:1[6-9]|[2-9]\\d)?\\d\\d(?:(?=\\x20\\d)\\x20|$))(|([01]\\d|2[0-3])(:[0-5]\\d){1,2})?$")) {
                eventBean.createEvent(
                        newEvent.getName(),
                        newEvent.getDescription(),
                        newEvent.getStartDate(),
                        newEvent.getStartDate());

                //addCategoriesList();
                newEvent.reset();
                return "/faces/administrator/administrator_panel?faces-redirect=true";
            }
        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
        return null;
    }

    public String updateEvent() {
        try {
            eventBean.updateEvent(
                    currentEvent.getId(),
                    currentEvent.getName(),
                    currentEvent.getDescription(),
                    currentEvent.getStartDate(),
                    currentEvent.getStartDate());
            return "/faces/administrator/event_lists?faces-redirect=true";

        } catch (EntityDoesNotExistsException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
        return "/faces/administrator/event_update?faces-redirect=true";
    }
    
        public List<EventDTO> getAllEvents() {
        try {
            return eventBean.getAllEvents();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }

    public int getAllEventsOfCategory(Long Id) {
        try {
            return categoryBean.getNumberofEvents(Id);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return 0;
        }
    }

    public int getNumberEvents(Long id) throws EntityDoesNotExistsException {
        return categoryBean.getNumberofEvents(id);
    }
    
    public void changeStatus() throws EntityDoesNotExistsException, MyConstraintViolationException {
       eventBean.changeEventStatus(currentEvent);
    }
    public void changePresenceStatus(Long id) throws EntityDoesNotExistsException, MyConstraintViolationException {
       eventBean.changePresenceStatus(id);
    }
    
    public int getNumberOfAttendants(Long id) throws EntityDoesNotExistsException {
        return eventBean.getEventNumberOfAttendants(id);
    }

    public void removeEvent(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteEventId");
            Long id = Long.parseLong(param.getValue().toString());
            eventBean.removeEvent(id);
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }

    public String addCategoriesList() throws EntityDoesNotExistsException, EventEnrolledException {

        for (String str : categoriesSelected) {
            eventBean.enrollEventInCategory(newEvent.getId(), (categoryBean.getCategoryByName(str)).getId());
        }
        return "/faces/administrator/administrator_panel?faces-redirect=true";
    }

    public List<AttendantDTO> getEnrolledAttendantsInEvents() {
        try {
            return attendantBean.getEnrolledAttendantsInEvents(currentEvent.getId());
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
        return null;
    }

    //retorna os attendants actuais do evento
    public List<AttendantDTO> getAttendantsDisponiveisSelected() {
        actualizarAttendantsSelected();
        return attendantsDisponiveisSelected;
    }

    public String addAttendantsList() throws EntityDoesNotExistsException, AttendantNotEnrolledException, AttendantEnrolledException {
        System.out.println("Eeeeeeeeevent ID: ");
        for (AttendantDTO att1 : eventBean.getEventAttendants(currentEvent.getName())) {
            attendantBean.unrollAttendantInEvent(att1.getId(), currentEvent.getId());
        }
        eventBean.clearAllAttendantsInEvent(currentEvent.getName());

        for (String str : attendantsSelected) {
     
            attendantBean.enrollAttendantInEvent((attendantBean.getAttendantByName(str)).getId(), currentEvent.getId());
        }
        actualizarAttendantsSelected();
        return "/faces/administrator/event_add_attendants?faces-redirect=true";
    }

    public void actualizarAttendantsSelected() {

        attendantsDisponiveisSelected.clear();
        if (eventBean.getEventAttendants(currentEvent.getName()).isEmpty()) {
            for (AttendantDTO att : attendantBean.getAllAttendants()) {
                attendantsDisponiveisSelected.add(att);
            }
            //  System.out.println("Disponiveis PAra selecao Se estava vazia:" + attendantsDisponiveisSelected);
        } else {
            for (AttendantDTO att : attendantBean.getAllAttendants()) {
                attendantsDisponiveisSelected.add(att);
            }

        }
    }
    ////CATEGORIES////////////////////////////////////////

    public List<EventCategoryDTO> getAllCategories() {
        try {
            return categoryBean.getAllEventCategories();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }

    public List<EventCategoryDTO> getAllCategoriesOfCurrentEvent() {
        try {
            return categoryBean.getAllCategoriesOfCurrentEvent(currentEvent.getId());
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }

    public String updateEventCategories() throws EntityDoesNotExistsException, EventNotEnrolledException, EventEnrolledException {
        for (CategoryDTO cat : eventBean.getAllCategoriesOfEvent(currentEvent.getId())) {

            eventBean.unrollEventInCategory(currentEvent.getId(), cat.getId());
        }
        //currentEvent.
        eventBean.clearAllCategoriesInEvent(currentEvent.getId());

        for (String str : categoriesSelected) {
            eventBean.enrollEventInCategory(currentEvent.getId(), (categoryBean.getCategoryByName(str)).getId());
        }
        //categoriesM.clear();
        return "/faces/administrator/event_lists?faces-redirect=true";
    }

    
/////EVENTS//////////////////////////////////////
   
   
   public void actualizarManagersSelected() {

       managersDisponiveisSelected.clear();
       //managersSelected.clear();
       if (eventBean.getEventManagers(currentEvent.getId()).isEmpty()) {
           for (ManagerDTO man : managerBean.getAllManagers()) {
               managersDisponiveisSelected.add(man);
         
           }
       } else {
           for (ManagerDTO man : managerBean.getAllManagers()) {
               managersDisponiveisSelected.add(man);
           }
       }
   }

   public String addManagersList() throws EntityDoesNotExistsException, AttendantNotEnrolledException, AttendantEnrolledException, ManagerNotEnrolledException, ManagerEnrolledException {
       for (ManagerDTO man : eventBean.getEventManagers(currentEvent.getId())) {
          
           managerBean.unrollManagerInEvent(man.getId(), currentEvent.getId());
       }
       eventBean.clearAllManagersInEvent(currentEvent.getName());

       for (String str : managersSelected) {

           managerBean.enrollManagerInEvent((managerBean.getManager(str)).getId(), currentEvent.getId());
       }
       actualizarManagersSelected();
       return "event_lists?faces-redirect=true";
   }
   
   public void updatePassword() throws EntityDoesNotExistsException {
       eventBean.updatePassword(currentEvent.getId(), currentEvent.getPassword());
   }
   
   //////////G&S/////////////////

    public List<ManagerDTO> getManagersDisponiveisSelected() {
        return managersDisponiveisSelected;
    }

    public void setManagersDisponiveisSelected(List<ManagerDTO> managersDisponiveisSelected) {
        this.managersDisponiveisSelected = managersDisponiveisSelected;
    }

    public EventDTO getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(EventDTO currentEvent) {
        this.currentEvent = currentEvent;
    }

    public List<String> getCategoriesSelected() {
        return categoriesSelected;
    }

    public void setCategoriesSelected(List<String> categoriesSelected) {
        this.categoriesSelected = categoriesSelected;
    }

    public EventDTO getNewEvent() {
        return newEvent;
    }

    public void setNewEvent(EventDTO newEvent) {
        this.newEvent = newEvent;
    }
    
    public List<String> getAttendantsSelected() {
        return attendantsSelected;
    }

    public void setAttendantsSelected(List<String> attendantsSelected) {
        this.attendantsSelected = attendantsSelected;
    }

    public List<String> getManagersSelected() {
        return managersSelected;
    }

    public void setManagersSelected(List<String> managersSelected) {
        this.managersSelected = managersSelected;
    }

    
}
