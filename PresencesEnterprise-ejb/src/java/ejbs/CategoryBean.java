package ejbs;

import dtos.AttendantCategoryDTO;
import dtos.CategoryDTO;
import dtos.EventCategoryDTO;
import dtos.EventDTO;
import entities.Attendant;
import entities.AttendantCategory;
import entities.Category;
import entities.EventCategory;
import entities.Event;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless
public class CategoryBean {

    @PersistenceContext
    private EntityManager em;

    public void createEventCategory(String name) throws EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            List<EventCategory> categories = (List<EventCategory>) em.createNamedQuery("getAllEventCategories").getResultList();
            for (EventCategory c : categories) {
                if (name.equals(c.getName())) {
                    throw new EntityAlreadyExistsException("A category with that name already exists.");
                }
            }
            EventCategory category = new EventCategory(name);
            em.persist(category);
        } catch (EntityAlreadyExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void createAttendantCategory(String name) throws EntityAlreadyExistsException, MyConstraintViolationException {
        try {
            List<AttendantCategory> categories = (List<AttendantCategory>) em.createNamedQuery("getAllAttendantCategories").getResultList();
            for (AttendantCategory c : categories) {
                if (name.equals(c.getName())) {
                    throw new EntityAlreadyExistsException("A category with that name already exists.");
                }
            }
            AttendantCategory category = new AttendantCategory(name);
            em.persist(category);
        } catch (EntityAlreadyExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public List<EventCategoryDTO> getAllEventCategories() {
        try {
            List<EventCategory> categories = (List<EventCategory>) em.createNamedQuery("getAllEventCategories").getResultList();
            return eventCategoriesToDTOs(categories);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public List<AttendantCategoryDTO> getAllAttendantCategories() {
        try {
            List<AttendantCategory> categories = (List<AttendantCategory>) em.createNamedQuery("getAllAttendantCategories").getResultList();
            return attendantCategoriesToDTOs(categories);
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public List<EventCategoryDTO> getAllCategoriesOfCurrentEvent(Long id) throws EntityDoesNotExistsException {

        Event event = em.find(Event.class, id);
        if (event == null) {
            throw new EntityDoesNotExistsException("There is no event with that id.");
        }
        List<EventCategory> eventCategories = event.getCategories();
        return eventCategoriesToDTOs(eventCategories);
    }

    public List<EventDTO> getAllCategoryEvents(Long id) throws EntityDoesNotExistsException {
        EventCategory category = em.find(EventCategory.class, id);
        if (category == null) {
            throw new EntityDoesNotExistsException("There is no category with that id.");
        }
        return eventsToDTOs(category.getEvents());

    }

    /*
     public int getNumberofAttendants(Long id) throws EntityDoesNotExistsException {
     EventCategory category = em.find(EventCategory.class, id);
     if (category == null) {
     throw new EntityDoesNotExistsException("There is no category with that id.");
     }
     return category.getAttendants().size();
     }
     */
    public int getNumberofEvents(Long id) throws EntityDoesNotExistsException {
        EventCategory category = em.find(EventCategory.class, id);
        if (category == null) {
            throw new EntityDoesNotExistsException("There is no category with that id.");
        }
        return category.getNumberOfEvents();
    }

    public int getNumberofAttendants(Long id) throws EntityDoesNotExistsException {
        AttendantCategory category = em.find(AttendantCategory.class, id);
        if (category == null) {
            throw new EntityDoesNotExistsException("There is no category with that id.");
        }
        return category.getNumberOfAttendants();
    }

    public CategoryDTO getCategoryByName(String name) {
        try {
            List<EventCategory> categories = (List<EventCategory>) em.createNamedQuery("getAllEventCategories").getResultList();
            for (EventCategory cat : categories) {
                if (name.equals(cat.getName())) {
                    return eventCategoryToDTO(cat);
                }
            }
            return null;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void updateCategory(Long id, String name) throws EntityDoesNotExistsException, MyConstraintViolationException {
        try {

            EventCategory category = em.find(EventCategory.class, id);
            if (category == null) {
                throw new EntityDoesNotExistsException("There is no category with that id.");
            }
            System.out.println("ID: " + id);
            System.out.println("NAME: " + name);

            System.out.println("CategoryID: " + category.getId());
            /*
             List<Category> categories = (List<Category>) em.createNamedQuery("getAllCategories").getResultList();
             for (EventCategory c : categories) {
             if (name.equals(c.getName())) {
             throw new EntityAlreadyExistsException("That category already exists.");
             }
             }
             */
            category.setName(name);
            em.merge(category);
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    public void removeCategory(Long id) throws EntityDoesNotExistsException {
        try {
            Category category = em.find(EventCategory.class, id);
            if (category == null) {
                throw new EntityDoesNotExistsException("There is no category with that id.");
            }
            /*
             for (Attendant attendant : category.getAttendants()) {
             attendant.removeCategory((EventCategory)category);
                      
             }
            
             for (Event event : category.getEvents()) {
             event.removeCategory(category);
             }
             */
            em.remove(category);
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    /* Caso o manager tenha categories
     public List<CategoryDTO> getManagerCategories(Long managerId) throws EntityDoesNotExistsException {
     try {
     Manager manager = em.find(Manager.class, managerId);
     if (manager == null) {
     throw new EntityDoesNotExistsException("Manager does not exists.");
     }
     return eventCategoriesToDTOs(manager.getCategories()); 
     } catch (EntityDoesNotExistsException e) {
     throw e;
     } catch (Exception e) {
     throw new EJBException(e.getMessage());
     }
     }
     */
//    public List<AttendantCategoryDTO> getAttendantCategories(Long attendantId) throws EntityDoesNotExistsException {
//        try {
//            Attendant attendant = em.find(Attendant.class, attendantId);
//            if (attendant == null) {
//                throw new EntityDoesNotExistsException("Attendant does not exists.");
//            }
//            return attendantCategoriesToDTOs(attendant.getCategories());
//        } catch (EntityDoesNotExistsException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new EJBException(e.getMessage());
//        }
//    }
    public List<EventCategoryDTO> getEventCategories(Long eventId) throws EntityDoesNotExistsException {
        try {
            Event event = em.find(Event.class, eventId);
            if (event == null) {
                throw new EntityDoesNotExistsException("Event does not exists.");
            }
            return eventCategoriesToDTOs(event.getCategories());
        } catch (EntityDoesNotExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    EventCategoryDTO eventCategoryToDTO(EventCategory category) {
        return new EventCategoryDTO(
                category.getId(),
                category.getName());
    }

    List<EventCategoryDTO> eventCategoriesToDTOs(List<EventCategory> categories) {
        List<EventCategoryDTO> dtos = new ArrayList<>();
        for (EventCategory c : categories) {
            dtos.add(eventCategoryToDTO(c));
        }
        return dtos;
    }

    AttendantCategoryDTO attendantCategoryToDTO(AttendantCategory category) {
        return new AttendantCategoryDTO(
                category.getId(),
                category.getName());
    }

    List<AttendantCategoryDTO> attendantCategoriesToDTOs(List<AttendantCategory> categories) {
        List<AttendantCategoryDTO> dtos = new ArrayList<>();
        for (AttendantCategory c : categories) {
            dtos.add(attendantCategoryToDTO(c));
        }
        return dtos;
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

}
