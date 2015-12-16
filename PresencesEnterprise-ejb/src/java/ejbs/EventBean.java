package ejbs;

import dtos.AttendantDTO;
import dtos.CategoryDTO;
import dtos.EventDTO;
import dtos.ManagerDTO;
import entities.Attendant;
import entities.EventCategory;
import entities.Event;
import entities.Manager;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistsException;
import exceptions.EventEnrolledException;
import exceptions.EventNotEnrolledException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless
public class EventBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private ManagerBean managerBean;
    @EJB
    private AttendantBean attendantBean;

    public void createEvent(String name, String description, String startDate, String finishDate) throws EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            List<Event> events = (List<Event>) em.createNamedQuery("getAllEvents").getResultList();
            for (Event e : events) {
                if (name.equals(e.getName())) {
                    throw new EntityAlreadyExistsException("A event with that name already exists.");
                }
            }
            Event event = new Event(name, description, startDate, finishDate);
            em.persist(event);
        } catch (EntityAlreadyExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public List<EventDTO> getAllEvents() {
        try {
            List<Event> events = (List<Event>) em.createNamedQuery("getAllEvents").getResultList();
            return eventsToDTOs(events);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    private Event getEventByName(String name) {
        try {
            Event event = new Event();
            List<Event> events = (List<Event>) em.createNamedQuery("getAllEvents").getResultList();
            for (Event e : events) {
                if (name.equals(e.getName())) {
                    event = e;
                    return event;
                }
            }
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
        return null;
    }

    public void updateEvent(Long id, String name, String description, String startDate, String finishDate) throws EntityDoesNotExistsException, MyConstraintViolationException {
        try {
            Event event = em.find(Event.class, id);
            if (event == null) {
                throw new EntityDoesNotExistsException("There is no event with that id.");
            }
            /*List<Event> events = (List<Event>) em.createNamedQuery("getAllEvents").getResultList();
            for (Event e : events){
                if (name.equals(e.getName())){
                    throw new EntityAlreadyExistsException("That event already exists.");
                }
            }*/
            event.setName(name);
            event.setDescription(description);
            event.setStartDate(startDate);
            event.setFinishDate(finishDate);
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void removeEvent(Long id) throws EntityDoesNotExistsException {
        try {
            Event event = em.find(Event.class, id);
            if (event == null) {
                throw new EntityDoesNotExistsException("There is no event with that id.");
            }

            for (ManagerDTO manager : managerBean.getEnrolledManagersInEvents(event.getId())) {

                managerBean.unrollManagerInEvent(manager.getId(), id);
            }

            for (AttendantDTO attendant : attendantBean.getEnrolledAttendantsInEvents(event.getId())) {
                attendantBean.unrollAttendantInEvent(attendant.getId(), id);
            }

            em.remove(event);

        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void changeEventStatus(EventDTO currentEvent) throws EntityDoesNotExistsException, MyConstraintViolationException {
        Event event = em.find(Event.class, currentEvent.getId());
        if (event == null) {
            throw new EntityDoesNotExistsException("There is no event with that id.");
        }
        System.out.println("");
        event.setOpenForEnroll(!event.isOpenForEnroll());
        currentEvent.setOpenForEnroll(!currentEvent.isOpenForEnroll());

        System.out.println("EVENT: " + event.toString());
        System.out.println("CURRENTEVENT: " + currentEvent.isOpenForEnroll());

    }

    public void changePresenceStatus(Long id) throws EntityDoesNotExistsException, MyConstraintViolationException {
        Event event = em.find(Event.class, id);
        if (event == null) {
            throw new EntityDoesNotExistsException("There is no event with that id.");
        }

        event.setOpenForPresence(!event.isOpenForPresence());
        eventToDTO(event);

    }

//    public List<EventDTO> getManagerEvents(Long managerId) throws EntityDoesNotExistsException {
//        try {
//            Manager manager = em.find(Manager.class, managerId);
//            if (manager == null) {
//                throw new EntityDoesNotExistsException("Manager does not exists.");
//            }
//            return eventsToDTOs(manager.getEvents()); 
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }
    public List<EventDTO> getAttendantEvents(Long attendantId) throws EntityDoesNotExistsException {
        try {
            Attendant attendant = em.find(Attendant.class, attendantId);
            if (attendant == null) {
                throw new EntityDoesNotExistsException("Attendant does not exists.");
            }
            return eventsToDTOs(attendant.getEvents());
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public List<AttendantDTO> getEventAttendants(String name) {
        Event event = getEventByName(name);
        if (event != null) {
            List<Attendant> eventAttendants = event.getAttendants();
            return attendantsToDTOs(eventAttendants);
        }
        return null;
    }

    public int getEventNumberOfAttendants(Long id) throws EntityDoesNotExistsException {
        System.out.println("PASSA#1");
        Event event = em.find(Event.class, id);
        System.out.println("PASSA#2");
        if (event == null) {
            throw new EntityDoesNotExistsException("There is no event with that id.");
        }
        return event.getNumberOfAttendants();
    }

    public List<ManagerDTO> getEventManagers(Long id) {
        Event event = em.find(Event.class, id);
        if (event != null) {
            return managersToDTOs(event.getManagers());
        }
        return null;
    }

//    public List<ManagerDTO> getEventManagers(String name) {
//        Event event = getEventByName(name);
//        if(event != null) {
//            List<Manager> eventManagers = event.getManagers();
//            return managersToDTOs(eventManagers);
//        }
//        return null;
//    }
    public void clearAllAttendantsInEvent(String name) {
        Event event = getEventByName(name);
        if (event != null) {
            event.clearAttendants();
        }

    }

    public void clearAllManagersInEvent(String name) {
        Event event = getEventByName(name);
        if (event != null) {
            event.clearManagers();
        }

    }

    public void clearAllCategoriesInEvent(Long id) throws EntityDoesNotExistsException {
        Event event = em.find(Event.class, id);
        if (event == null) {
            throw new EntityDoesNotExistsException("There is no event with that id.");
        }
        event.clearCategories();

    }

    public List<EventDTO> getCategoryEvents(Long categoryId) throws EntityDoesNotExistsException {
        try {
            EventCategory category = em.find(EventCategory.class, categoryId);
            if (category == null) {
                throw new EntityDoesNotExistsException("Category does not exists.");
            }
            return eventsToDTOs(category.getEvents());
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void enrollEventInCategory(Long eventId, Long categoryId) throws EntityDoesNotExistsException, EventEnrolledException {
        try {
            Event event = em.find(Event.class, eventId);
            if (event == null) {
                throw new EntityDoesNotExistsException("There is no event with that id.");
            }

            EventCategory category = em.find(EventCategory.class, categoryId);
            if (category == null) {
                throw new EntityDoesNotExistsException("There is no categoty with that id.");
            }

            if (category.getEvents().contains(event)) {
                throw new EventEnrolledException("Event is already enrolled in that category.");
            }

            category.addEvent(event);
            event.addCategory(category);

        } catch (EntityDoesNotExistsException | EventEnrolledException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void unrollEventInCategory(Long eventId, Long categoryId) throws EntityDoesNotExistsException, EventNotEnrolledException {
        try {
            EventCategory category = em.find(EventCategory.class, categoryId);
            if (category == null) {
                throw new EntityDoesNotExistsException("There is no category with that id.");
            }

            Event event = em.find(Event.class, eventId);
            if (event == null) {
                throw new EventNotEnrolledException("There is no event with that id.");
            }

            // if(!category.getAttendants().contains(event)){
            //   throw new EventNotEnrolledException("Event is not enrolled in that category.");
            // }
            category.removeEvent(event);
            event.removeCategory(category);

        } catch (EntityDoesNotExistsException | EventNotEnrolledException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public List<EventDTO> getEnrolledEventsInCategories(Long Id) throws EntityDoesNotExistsException {
        try {
            EventCategory category = em.find(EventCategory.class, Id);
            if (category == null) {
                throw new EntityDoesNotExistsException("There is no category with that id.");
            }
            List<Event> events = (List<Event>) category.getEvents();
            return eventsToDTOs(events);
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public List<EventDTO> getUnrolledEventsInCategories(Long id) throws EntityDoesNotExistsException {
        try {
            EventCategory category = em.find(EventCategory.class, id);
            if (category == null) {
                throw new EntityDoesNotExistsException("There is no category with that id.");
            }
            //nao sei se este código está correcto??
            List<Event> events = (List<Event>) em.createNamedQuery("getAllEventCategories")
                    .setParameter("categoryId", category.getId())
                    .getResultList();
            //-----------------------------------------------------------------------------------------
            List<Event> enrolled = em.find(EventCategory.class, id).getEvents();
            events.removeAll(enrolled);
            return eventsToDTOs(events);
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public List<CategoryDTO> getAllCategoriesOfEvent(Long id) throws EntityDoesNotExistsException {

        Event event = em.find(Event.class, id);
        if (event == null) {

            throw new EntityDoesNotExistsException("There is no event with that id.");
        }

        List<EventCategory> eventCategories = event.getCategories();
        return categoriesToDTOs(eventCategories);
    }

    public void updatePassword(Long id, String pass) throws EntityDoesNotExistsException {
        Event event = em.find(Event.class, id);
        if (event == null) {

            throw new EntityDoesNotExistsException("There is no event with that id.");
        }
        event.setPassword(pass);
    }

    public void adicionarAttendant(Long eventId, Long attendantId) throws EntityDoesNotExistsException {
        Event event = em.find(Event.class, eventId);
        if (event == null) {

            throw new EntityDoesNotExistsException("There is no event with that id.");
        }
        Attendant attendant = em.find(Attendant.class, attendantId);
        if (attendant == null) {

            throw new EntityDoesNotExistsException("There is no event with that id.");
        }
 
        event.addPresenca(attendant);
    }

    EventDTO eventToDTO(Event event) {
        EventDTO eventDTO = new EventDTO(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getStartDate(),
                event.getFinishDate());
        eventDTO.setOpenForEnroll(event.isOpenForEnroll());
        eventDTO.setOpenForPresence(event.isOpenForPresence());
        eventDTO.setPassword(event.getPassword());
        return eventDTO;
    }

    List<EventDTO> eventsToDTOs(List<Event> events) {
        List<EventDTO> dtos = new ArrayList<>();
        for (Event e : events) {
            dtos.add(eventToDTO(e));
        }
        return dtos;
    }

    CategoryDTO categoryToDTO(EventCategory category) {
        return new CategoryDTO(
                category.getId(),
                category.getName());
    }

    List<CategoryDTO> categoriesToDTOs(List<EventCategory> categories) {
        List<CategoryDTO> dtos = new ArrayList<>();
        for (EventCategory c : categories) {
            dtos.add(categoryToDTO(c));
        }
        return dtos;
    }

    AttendantDTO attendantToDTO(Attendant attendant) {
        return new AttendantDTO(
                attendant.getId(),
                attendant.getUserName(),
                null,
                attendant.getName(),
                attendant.getEmail());
    }

    List<AttendantDTO> attendantsToDTOs(List<Attendant> attendants) {
        List<AttendantDTO> dtos = new ArrayList<>();
        for (Attendant a : attendants) {
            dtos.add(attendantToDTO(a));
        }
        return dtos;
    }

    ManagerDTO managerToDTO(Manager manager) {
        return new ManagerDTO(
                manager.getId(),
                manager.getUserName(),
                null,
                manager.getName(),
                manager.getEmail());
    }

    List<ManagerDTO> managersToDTOs(List<Manager> managers) {
        List<ManagerDTO> dtos = new ArrayList<>();
        for (Manager m : managers) {
            dtos.add(managerToDTO(m));
        }
        return dtos;
    }

}
