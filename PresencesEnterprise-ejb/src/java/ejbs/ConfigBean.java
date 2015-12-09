
package ejbs;

import entities.UserGroup;
import entities.UserGroup.GROUP;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class ConfigBean {

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
    
    
    @PostConstruct
    public void populateDB() {
        try {
            
            //createUser (String username, String password, String name, String email, UserType type)
            administratorBean.createUser ("a_01", "1", "Administrator_Name_01", "administrator_email_01@email.com", GROUP.Administrator);
            administratorBean.createUser ("a_02", "2", "Administrator_Name_02", "administrator_email_02@email.com", GROUP.Administrator);
            administratorBean.createUser ("a_03", "3", "Administrator_Name_03", "administrator_email_03@email.com", GROUP.Administrator);
            administratorBean.createUser ("a_04", "4", "Administrator_Name_04", "administrator_email_04@email.com", GROUP.Administrator);
            administratorBean.createUser ("m_01", "11", "Manager_Name_01", "manager_email_01@email.com", GROUP.Manager);
            administratorBean.createUser ("m_02", "12", "Manager_Name_02", "manager_email_02@email.com", GROUP.Manager);
            administratorBean.createUser ("m_03", "13", "Manager_Name_03", "manager_email_03@email.com", GROUP.Manager);
            administratorBean.createUser ("p_01", "111", "Attendant_Name_01", "attendant_email_01@email.com", GROUP.Attendant);
            administratorBean.createUser ("p_02", "112", "Attendant_Name_02", "attendant_email_02@email.com", GROUP.Attendant);
            administratorBean.createUser ("p_03", "113", "Attendant_Name_03", "attendant_email_03@email.com", GROUP.Attendant);
            administratorBean.createUser ("p_04", "114", "Attendant_Name_04", "attendant_email_04@email.com", GROUP.Attendant);
            administratorBean.createUser ("p_05", "115", "Attendant_Name_05", "attendant_email_05@email.com", GROUP.Attendant);
            administratorBean.createUser ("p_06", "116", "Attendant_Name_06", "attendant_email_06@email.com", GROUP.Attendant);
            administratorBean.createUser ("p_07", "117", "Attendant_Name_07", "attendant_email_07@email.com", GROUP.Attendant);
            administratorBean.createUser ("p_08", "118", "Attendant_Name_08", "attendant_email_08@email.com", GROUP.Attendant);
            administratorBean.createUser ("p_09", "119", "Attendant_Name_09", "attendant_email_09@email.com", GROUP.Attendant);

            //createEvent (String name,String description, String startDate, String finishDate)
            eventBean.createEvent("Evento_1","Descricao Evento_1", "01/10/2015 12:00", "01/10/2015 13:00");
            eventBean.createEvent("Evento_2","Descricao Evento_2", "08/10/2015 12:00", "08/10/2015 13:00");
            eventBean.createEvent("Evento_3","Descricao Evento_3", "09/10/2015 12:00", "09/10/2015 13:00");
            eventBean.createEvent("Evento_4","Descricao Evento_4", "01/11/2015 12:00", "01/11/2015 13:00");
            eventBean.createEvent("Evento_5","Descricao Evento_5", "01/10/2015 15:00", "01/10/2015 17:00");
            eventBean.createEvent("Evento_6","Descricao Evento_6", "01/11/2015 12:00", "02/11/2015 12:00");
            eventBean.createEvent("Evento_7","Descricao Evento_7", "01/10/2015 12:00", "01/10/2015 13:00");
            eventBean.createEvent("Evento_8","Descricao Evento_8", "01/10/2015 16:00", "01/10/2015 17:00");
            eventBean.createEvent("Evento_9","Descricao Evento_9", "01/10/2015 12:00", "01/10/2015 13:00");
            eventBean.createEvent("Evento_10","Descricao Evento_10", "01/10/2015 18:00", "01/10/2015 19:00");
            eventBean.createEvent("Evento_11","Descricao Evento_11", "01/10/2015 20:00", "01/10/2015 23:00");
            eventBean.createEvent("Evento_12","Descricao Evento_12", "01/11/2015 14:00", "01/11/2015 18:00");
           
            //createCategory (String name)
            categoryBean.createEventCategory("Categoria_1");
            categoryBean.createEventCategory("Categoria_2");
            categoryBean.createEventCategory("Categoria_3");
            categoryBean.createEventCategory("Categoria_4");
            categoryBean.createEventCategory("Categoria_5");
            categoryBean.createEventCategory("Categoria_6");
            categoryBean.createEventCategory("Categoria_7");
            
            categoryBean.createAttendantCategory("Categoria_10");
            categoryBean.createAttendantCategory("Categoria_11");
            categoryBean.createAttendantCategory("Categoria_12");
           
            
            Long managerID = Long.parseLong("5");
            Long eventID = Long.parseLong("18");
            managerBean.enrollManagerInEvent(managerID, eventID);
            eventBean.enrollEventInCategory(Long.parseLong("23"),Long.parseLong("28"));
            eventBean.enrollEventInCategory(Long.parseLong("24"),Long.parseLong("29"));
            eventBean.enrollEventInCategory(Long.parseLong("25"),Long.parseLong("28"));
            eventBean.enrollEventInCategory(Long.parseLong("26"),Long.parseLong("28"));
            
            //atttendants em events
            //attendantBean.enrollAttendantInEvent(id, currentEvent.getId());
            attendantBean.enrollAttendantInEvent(Long.parseLong("7"), Long.parseLong("23"));
            attendantBean.enrollAttendantInEvent(Long.parseLong("7"), Long.parseLong("24"));
            attendantBean.enrollAttendantInEvent(Long.parseLong("8"), Long.parseLong("24"));
            attendantBean.enrollAttendantInEvent(Long.parseLong("9"), Long.parseLong("24"));
            attendantBean.enrollAttendantInEvent(Long.parseLong("10"), Long.parseLong("23"));
            
            //atttendants em categories
            //attendantBean.enrollAttendantInEvent(id, currentEvent.getId());
            //attendantBean.enrollAttendantInCategory(Long.parseLong("7"), Long.parseLong("28"));
            //attendantBean.enrollAttendantInCategory(Long.parseLong("7"), Long.parseLong("29"));
            
 
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
