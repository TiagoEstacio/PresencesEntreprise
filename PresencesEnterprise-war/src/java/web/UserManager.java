package web;

import auxCategories.UserType;
import exceptions.PasswordValidationException;
import dtos.AdministratorDTO;
import dtos.AttendantDTO;
import dtos.CategoryDTO;
import dtos.EventDTO;
import dtos.ManagerDTO;
import dtos.UserDTO;
import ejbs.AdministratorBean;
import ejbs.AttendantBean;
import ejbs.CategoryBean;
import ejbs.EventBean;
import ejbs.ManagerBean;
import entities.User;
import entities.UserGroup;
import exceptions.AttendantEnrolledException;
import exceptions.AttendantNotEnrolledException;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistsException;
import exceptions.EventEnrolledException;
import exceptions.ManagerEnrolledException;
import exceptions.ManagerNotEnrolledException;
import exceptions.MyConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class UserManager {

    @EJB
    private AdministratorBean administratorBean;
    @EJB
    private ManagerBean managerBean;
    @EJB
    private AttendantBean attendantBean;
    @EJB
    private EventBean eventBean;
    @EJB
    private CategoryBean categoryBean;

    //USERS CONTROLLER
    private boolean loginFlag = true;
    private static final Logger logger = Logger.getLogger("web.UserManager");

    private UserDTO newUser;
    private UserDTO currentUser;
    private AdministratorDTO newAdministrator;
    private AdministratorDTO currentAdministrator;
    private ManagerDTO newManager;
    private ManagerDTO currentManager;
    private AttendantDTO newAttendant;
    private AttendantDTO currentAttendant;
    private EventDTO newEvent;
    private EventDTO currentEvent;
    private CategoryDTO newCategory;
    private CategoryDTO currentCategory;
    
    private String username;
    private String password;

    private UIComponent component;

    //variavel auxiliar de veridicacao de password
    private String passwordVerify;

    private List<AttendantDTO> attendantsDisponiveisSelected = new ArrayList<>();
    private List<ManagerDTO> managersDisponiveisSelected = new ArrayList<>();
    private List<String> attendantsSelected;
    private List<String> categoriesSelected;
    private List<String> managersSelected;

    public UserManager() {
        newUser = new UserDTO();
        newAdministrator = new AdministratorDTO();
        newManager = new ManagerDTO();
        newAttendant = new AttendantDTO();
        newEvent = new EventDTO();
        newCategory = new CategoryDTO();

        currentCategory = new CategoryDTO();
        currentAdministrator = new AdministratorDTO();
        currentAttendant = new AttendantDTO();
        currentEvent = new EventDTO();
        currentManager = new ManagerDTO();
        currentUser = new UserDTO();
    }
    
////////////////////////////////////////////    
//Retirado do LoginManager//////////////////
    
 /**
    * Verifica se existe algum utilizador autenticado.
    * @return true se há algum utilizador autenticado e, falso em caso contrário.
    */
   public boolean isSomeUserAuthenticated() {
       return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() != null;
   }      
  
   /**
    * Verifica se o utilizador atual pertence a determinado role.
    * Não é utilizado neste projeto.
    * @param role a verificar
    * @return boolean true se o utilizador atual pertencer ao role e false em caso contrário.
    */    
   public boolean isUserInRole(String role) {
       return (isSomeUserAuthenticated() &&
               FacesContext.getCurrentInstance().getExternalContext().isUserInRole(role));
   }    
  
   public String tratarLoginErrado(){
       if(isSomeUserAuthenticated()){
           logout();
       }
      
       // return "index.xhtml?faces-redirect=true"; //Usar esta linha se a página inicial for a página index
       return "faces/index_login.xhtml?faces-redirect=true"; //Usar esta linha se a página inicial for a página index_login
   }
    
   public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {           
            System.out.println("USER_TRY_: " +this.username+this.password);
//            request.login(this.username, this.password); 
            request.login(administratorBean.getUserIdByUserName(this.username)+"",this.password);         
        } catch (ServletException e) {
            logger.log(Level.WARNING, e.getMessage());
            //System.out.println("USER: " +this.username+this.password);
            return "faces/error?faces-redirect=true";
        }

        if (isUserInRole("Administrator")) {
            return "faces/administrator/administrator_panel?faces-redirect=true";
        }
        if (isUserInRole("Attendant")) {
            getCurrentUserFromLogIn();
            return "faces/attendant/attendant_events_list?faces-redirect=true";
        }
        //O if já não era necessário
        if (isUserInRole("Manager")) {
            getCurrentUserFromLogIn();
            return "/faces/manager/manager_details?faces-redirect=true";
        }

        //!!!!!!!!!!!!
        return "faces/error?faces-redirect=true";
    } 
   
   //Melhorar para não usar User no UserManager apenas UserDTO
    public void getCurrentUserFromLogIn(){
       User user = administratorBean.getUserByUserName(username);
       currentUser = userToDTO(user);
    }
    
     private UserDTO userToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUserName(),
                null,
                user.getName(),
                user.getEmail(),
                user.getGroup().getGroupName());
    }

    public String logout() {
       FacesContext context = FacesContext.getCurrentInstance();

       // remove data from beans:
       for (String bean : context.getExternalContext().getSessionMap().keySet()) {
           context.getExternalContext().getSessionMap().remove(bean);
       }

       // destroy session:
       HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
       session.invalidate();

       // using faces-redirect to initiate a new request:
       //return "/index.xhtml?faces-redirect=true"; //Usar esta linha se a página inicial for a página index
       return "faces/index_login.xhtml?faces-redirect=true"; //Usar esta linha se a página inicial for a página index_login
   }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(boolean loginFlag) {
        this.loginFlag = loginFlag;
    }
    
    //////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////// ADMINISTRATORS ////////////
    /*public String createAdministrator() {
     try {
     administratorBean.createAdministrator(
     newAttendant.getUsername(),
     newAttendant.getPassword(),
     newAttendant.getName(),
     newAttendant.getEmail());
     newAdministrator.reset();
     return "administrator_panel?faces-redirect=true";
     } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
     FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
     } catch (Exception e) {
     FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", component, logger);
     }
     return null;
     }*/
    public String createUser() throws PasswordValidationException {
        try {
            //verificar password
            if (newUser.getPassword().equals(passwordVerify)) {
                administratorBean.createUser(
                        newUser.getUsername(),
                        newUser.getPassword(),
                        newUser.getName(),
                        newUser.getEmail(),
                        newUser.getGroup());
                newUser.reset();
                return "/faces/administrator/administrator_panel?faces-redirect=true";
            } else {
                throw new PasswordValidationException("Password not equal to password confirmation.");
            }
        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
        return null;
    }

    //updateUser -> qualquer user pode ser atualizado para Manager. Um admin tb pode ser manager e fica com 2 tipos?
    //não é possível fazer o update para Admin e para attendant(pôr esta hipótese ao grupo)
    public String updateUser() throws EntityDoesNotExistsException, MyConstraintViolationException {
        System.out.println("Current User ID" + currentUser.getId());
        try {
            //if (currentUser.getPassword().equals(passwordVerify)) {
            administratorBean.updateUser(
                    currentUser.getId(),
                    currentUser.getName(),
                    currentUser.getEmail(),
                    currentUser.getPassword()
            );
            return "/faces/administrator/administrator_lists?faces-redirect=true";
//            } else {
//                //TODO - NOT WORKING
//                throw new PasswordValidationException("Password not equal to password confirmation.");
//            }
        } catch (EntityDoesNotExistsException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
        return "/faces/administrator/administrator_update?faces-redirect=true";

    }

    public List<AdministratorDTO> getAllAdministrators() {
        try {
            return administratorBean.getAllAdministrators();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }

    public void removeUser(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteUserId");
            Long id = Long.parseLong(param.getValue().toString());
            administratorBean.removeUser(id);
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////// MANAGERS //////////////////
//    public String createManager() throws PasswordValidationException {
//        try {
//            //verificar password
//            if (newManager.getPassword().equals(passwordVerify)) {
//                managerBean.createManager(
//                        newManager.getUsername(),
//                        newManager.getPassword(),
//                        newManager.getName(),
//                        newManager.getEmail());
//                newManager.reset();
//                return "/faces/administrator/manager_panel?faces-redirect=true";
//            } else {
//                //TODO - NOT WORKING
//                throw new PasswordValidationException("Password not equal to password confirmation.");
//            }
//        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
//        }
//        return null;
//    }

    public List<ManagerDTO> getAllManagers() {
        try {
            return managerBean.getAllManagers();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }

//    public String updateManager() throws PasswordValidationException {
//        try {
//            //verificar password
//            if (currentManager.getPassword().equals(passwordVerify)) {
//                managerBean.updateManager(
//                        currentManager.getId(),
//                        currentManager.getUsername(),
//                        currentManager.getPassword(),
//                        currentManager.getName(),
//                        currentManager.getEmail());
//                return "/faces/administrator/manager_lists?faces-redirect=true";
//            } else {
//                //TODO - NOT WORKING
//                throw new PasswordValidationException("Password not equal to password confirmation.");
//            }
//        } catch (EntityDoesNotExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
//        }
//        return "/faces/administrator/manager_update?faces-redirect=true";
//    }

    public List<EventDTO> getAllEventsOfCurrentManager() {
        try {
            return managerBean.getAllEventsOfManager(currentUser.getId());
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public int getManagerNumberEvents(Long managerId) throws EntityDoesNotExistsException {
        return managerBean.getManagerEvents(managerId).size();
    }

//    public void removeManager(ActionEvent event) {
//        try {
//            UIParameter param = (UIParameter) event.getComponent().findComponent("managerId");
//            Long id = Long.parseLong(param.getValue().toString());
//            managerBean.removeManager(id);
//        } catch (EntityDoesNotExistsException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
//        }
//    }

    /*
     public List<ManagerDTO> getCurrentManagersInEvent() {
     try {
     return eventBean.getManager(currentManager.getUsername());
     } catch (Exception e) {
     FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
     return null;
     }
     }
    
     /* Caso manager precise de categories
     public List<ManagerDTO> getCurrentManagersInCategory() {
     try {
     return categoryBean.getManager(currentManager.getUsername());
     } catch (Exception e) {
     FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
     return null;
     }
     }
     */
    public void enrollManagerInEvent(Long managerId, Long eventId) {
        try {

            managerBean.enrollManagerInEvent(managerId, eventId);
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }

    public void unrollManagerInEvent(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("managerId");
            Long id = Long.parseLong(param.getValue().toString());
            managerBean.unrollManagerInEvent(id, currentEvent.getId());
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }

    public List<ManagerDTO> getEnrolledManagersInEvents(Long eventId) {
        System.out.println("event.id capturado: " + eventId);
        try {
            return managerBean.getEnrolledManagersInEvents(eventId);
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
        return null;
    }


    /* Caso manager precise de categories
     public void enrollManagerInCategory(ActionEvent event) {
     try {
     UIParameter param = (UIParameter) event.getComponent().findComponent("managerId");
     Long id = Long.parseLong(param.getValue().toString());
     managerBean.enrollManagerInCategory(id, currentCategory.getId());
     } catch (EntityDoesNotExistsException e) {
     FacesExceptionHandler.handleException(e, e.getMessage(), logger);
     } catch (Exception e) {
     FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
     }
     }  
    
     public void unrollManagerInCategory(ActionEvent event) {
     try {
     UIParameter param = (UIParameter) event.getComponent().findComponent("managerId");
     Long id = Long.parseLong(param.getValue().toString());
     managerBean.unrollManagerInCategory(id, currentCategory.getId());
     } catch (EntityDoesNotExistsException e) {
     FacesExceptionHandler.handleException(e, e.getMessage(), logger);
     } catch (Exception e) {
     FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
     }
     }
     */
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////// ATTENDANTS ////////////////
//    public String createAttendandt() throws PasswordValidationException {
//        try {
//            //verificar password
//            if (newAttendant.getPassword().equals(passwordVerify)) {
//                attendantBean.createAttendant(
//                        newAttendant.getUsername(),
//                        newAttendant.getPassword(),
//                        newAttendant.getName(),
//                        newAttendant.getEmail());
//                newAttendant.reset();
//                return "/faces/administrator/attendant_panel?faces-redirect=true";
//            } else {
//                //TODO - NOT WORKING
//                throw new PasswordValidationException("Password not equal to password confirmation.");
//            }
//        } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
//            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
//        } catch (Exception e) {
//            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
//        }
//        return null;
//    }

    public List<AttendantDTO> getAllAttendants() {
        try {
            return attendantBean.getAllAttendants();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }
/*
    public int getAllAttendantsOfCategory(long id) {
        try {

            return categoryBean.getNumberofAttendants(id);
            // return attendantBean.getAllAttendants();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return 0;
        }
    }
*/
    public String updateAttendant() {
        try {
            //verificar password
            if (currentAttendant.getPassword().equals(passwordVerify)) {
                attendantBean.updateAttendant(
                        currentAttendant.getId(),
                        currentAttendant.getUsername(),
                        currentAttendant.getPassword(),
                        currentAttendant.getName(),
                        currentAttendant.getEmail());
                return "/faces/administrator/attendant_panel?faces-redirect=true";
            } else {
                //TODO - NOT WORKING
                throw new PasswordValidationException("Password not equal to password confirmation.");
            }
        } catch (EntityDoesNotExistsException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
        return "/faces/administrator/attendant_update?faces-redirect=true";
    }
/*
    public void removeAttendant(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("attendantId");
            Long id = Long.parseLong(param.getValue().toString());
            attendantBean.removeAttendant(id);
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }
*/
    public List<EventDTO> getAllEventsOfCurrentAttendant() {
        try {
            return attendantBean.getAllEventsOfAttendant(currentUser.getId());
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
    }

    public int getAttendantNumberEvents(Long attendantId) throws EntityDoesNotExistsException {
        return attendantBean.getAllEventsOfAttendant(attendantId).size();
    }

    /*
     public List<AttendantDTO> getCurrentAttendantsInEvent() {
     try {
     return eventBean.getAttendant(currentAttendant.getUsername());
     } catch (Exception e) {
     FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
     return null;
     }
     }
    
     public List<AttendantDTO> getCurrentAttendantsInCategory() {
     try {
     return categoryBean.getAttendant(currentAttendant.getUsername());
     } catch (Exception e) {
     FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
     return null;
     }
     }
     */
    public void enrollAttendantInEvent(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("attendantId");
            Long id = Long.parseLong(param.getValue().toString());
            attendantBean.enrollAttendantInEvent(id, currentEvent.getId());
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }

    public void unrollAttendantInEvent(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("attendantId");
            Long id = Long.parseLong(param.getValue().toString());
            attendantBean.unrollAttendantInEvent(id, currentEvent.getId());
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }
/*
    public void enrollAttendantInCategory(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("attendantId");
            Long id = Long.parseLong(param.getValue().toString());
            attendantBean.enrollAttendantInCategory(id, currentCategory.getId());
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }

    public void unrollAttendantInCategory(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("attendantId");
            Long id = Long.parseLong(param.getValue().toString());
            attendantBean.unrollAttendantInCategory(id, currentCategory.getId());
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }
*/
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

    public String addAttendantsList() throws EntityDoesNotExistsException, AttendantNotEnrolledException, AttendantEnrolledException {
        System.out.println("Eeeeeeeeevent ID: ");
        for (AttendantDTO att1 : eventBean.getEventAttendants(currentEvent.getName())) {
            attendantBean.unrollAttendantInEvent(att1.getId(), currentEvent.getId());
        }
        eventBean.clearAllAttendantsInEvent(currentEvent.getName());

        for (String str : attendantsSelected) {
            System.err.println("STRINGG: " + str);
            System.out.println("LALALAL: " + attendantBean.getAttendantByName(str));
            //System.out.println(currentEvent.getId());

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

//    public void actualizarManagersSelected() {
//
//        managersDisponiveisSelected.clear();
//        if (eventBean.getEventManagers(currentEvent.getName()).isEmpty()) {
//            for (ManagerDTO att : managerBean.getAllManagers()) {
//                managersDisponiveisSelected.add(att);
//            }
//            //  System.out.println("Disponiveis PAra selecao Se estava vazia:" + attendantsDisponiveisSelected);
//        } else {
//            for (ManagerDTO att : managerBean.getAllManagers()) {
//                managersDisponiveisSelected.add(att);
//            }
//
//        }
//    }

//    public void addManagersList() throws EntityDoesNotExistsException, AttendantNotEnrolledException, AttendantEnrolledException, ManagerNotEnrolledException, ManagerEnrolledException {
//        for (ManagerDTO att1 : eventBean.getEventManagers(currentEvent.getName())) {
//            managerBean.unrollManagerInEvent(att1.getId(), currentEvent.getId());
//        }
//        eventBean.clearAllManagersInEvent(currentEvent.getName());
//
//        for (String str : managersSelected) {
//
//            enrollManagerInEvent((managerBean.getManager(str)).getId(), currentEvent.getId());
//        }
//        actualizarManagersSelected();
//    }
/*
    public List<AttendantDTO> getEnrolledAttendantsInCategories() {
        try {
            return attendantBean.getEnrolledAttendantsInCategories(currentCategory.getId());
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
        return null;
    }
    */
    /*
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
     return "event_lists?faces-redirect=true";
     }
     */

    /* Caso manager precise de categories
     public List<ManagerDTO> getEnrolledManagersInCategories() {
     try {
     return managerBean.getEnrolledManagersInCategories(currentCategory.getId());
     } catch (EntityDoesNotExistsException e) {
     FacesExceptionHandler.handleException(e, e.getMessage(), logger);
     } catch (Exception e) {
     FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
     }
     return null;
     }
     */
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////// GETTERS & SETTERS /////////
    public UserGroup.GROUP[] getUserGroupValues() {
        return administratorBean.getUserGroup();
    }

    public List<ManagerDTO> getManagersDisponiveisSelected() {
        return managersDisponiveisSelected;
    }

    public void setManagersDisponiveisSelected(List<ManagerDTO> managersDisponiveisSelected) {
        this.managersDisponiveisSelected = managersDisponiveisSelected;
    }

    public UserDTO getNewUser() {
        return newUser;
    }

    public void setNewUser(UserDTO newUser) {
        this.newUser = newUser;
    }

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDTO currentUser) {
        this.currentUser = currentUser;
    }

    public AdministratorDTO getNewAdministrator() {
        return newAdministrator;
    }

    public void setNewAdministrator(AdministratorDTO newAdministrator) {
        this.newAdministrator = newAdministrator;
    }

    public AdministratorDTO getCurrentAdministrator() {
        return currentAdministrator;
    }

    public void setCurrentAdministrator(AdministratorDTO currentAdministrator) {
        this.currentAdministrator = currentAdministrator;
    }

    public ManagerDTO getNewManager() {
        return newManager;
    }

    public void setNewManager(ManagerDTO newManager) {
        this.newManager = newManager;
    }

    public ManagerDTO getCurrentManager() {
        return currentManager;
    }

    public void setCurrentManager(ManagerDTO currentManager) {
        this.currentManager = currentManager;
    }

    public AttendantDTO getNewAttendant() {
        return newAttendant;
    }

    public void setNewAttendant(AttendantDTO newAttendant) {
        this.newAttendant = newAttendant;
    }

    public AttendantDTO getCurrentAttendant() {
        return currentAttendant;
    }

    public void setCurrentAttendant(AttendantDTO currentAttendant) {
        this.currentAttendant = currentAttendant;
    }

    public EventDTO getNewEvent() {
        return newEvent;
    }

    public void setNewEvent(EventDTO newEvent) {
        this.newEvent = newEvent;
    }

    public EventDTO getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(EventDTO currentEvent) {
        this.currentEvent = currentEvent;
    }

    public CategoryDTO getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(CategoryDTO newCategory) {
        this.newCategory = newCategory;
    }

    public CategoryDTO getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(CategoryDTO currentCategory) {
        this.currentCategory = currentCategory;
    }

    public UIComponent getComponent() {
        return component;
    }

    public void setComponent(UIComponent component) {
        this.component = component;
    }

    public List<String> getCategoriesSelected() {
        return categoriesSelected;
    }

    public void setCategoriesSelected(List<String> categoriesSelected) {
        this.categoriesSelected = categoriesSelected;
    }

    public List<String> getManagersSelected() {
        return managersSelected;
    }

    public void setManagersSelected(List<String> managersSelected) {
        this.managersSelected = managersSelected;
    }

    public List<String> getAttendantsSelected() {
        return attendantsSelected;
    }

    public void setAttendantsSelected(List<String> attendantsSelected) {
        this.attendantsSelected = attendantsSelected;
    }

    //retorna os attendants actuais do evento
    public List<AttendantDTO> getAttendantsDisponiveisSelected() {
        actualizarAttendantsSelected();
        return attendantsDisponiveisSelected;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////// VALIDATORS ////////////////

    /* PARA ADAPTAR DEPOIS - CÓDIGO DA FICHA 5
     public void validateUsername(FacesContext context, UIComponent toValidate, Object value) {
     try {
     //Your validation code goes here
     String username = (String) value;
     //If the validation fails
     if (username.startsWith("xpto")) {
     FacesMessage message = new FacesMessage("Error: invalid username.");
     message.setSeverity(FacesMessage.SEVERITY_ERROR);
     context.addMessage(toValidate.getClientId(context), message);
     ((UIInput) toValidate).setValid(false);
     }
     } catch (Exception e) {
     FacesExceptionHandler.handleException(e, "Unkown error.", logger);
     }
     }
     */
    public String getPasswordVerify() {
        return passwordVerify;
    }

    public void setPasswordVerify(String passwordVerify) {
        this.passwordVerify = passwordVerify;
    }


    
}
