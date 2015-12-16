
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
            /**
             * ->Deve existir uma tabela entre administradores e eventos e 
             *  entre admin e cat para ter sempre o id do criador de cada um deles??
             * 
             * -> Mudar status do evento de false/true para aberto/fechado
             * 
             * ->Os eventos e attendants qd criados devem ter logo categoria
             * 
             * ->botoes para criar os dois tipos de categoria
             * 
             * ->botao para criar nova categoria de eventos qd se cria um evento. mostrar tipos de categoria a escolher e permitir adicionar uma
             * ->botao para criar nova categoria de attendants qd se cria um evento. mostrar tipos de categoria a escolher e permitir adicionar um
             */
            //createUser (String username, String password, String name, String email, UserType type)
            administratorBean.createUser ("a_01", "1", "Administrator_Name_01", "administrator_email_01@email.com", GROUP.Administrator);
            administratorBean.createUser ("a_02", "2", "Administrator_Name_02", "administrator_email_02@email.com", GROUP.Administrator);
            administratorBean.createUser ("a_03", "3", "Administrator_Name_03", "administrator_email_03@email.com", GROUP.Administrator);
            administratorBean.createUser ("a_04", "4", "Administrator_Name_04", "administrator_email_04@email.com", GROUP.Administrator);
            administratorBean.createUser ("m_01", "11", "Manager_Name_01", "manager_email_01@email.com", GROUP.Manager);
            administratorBean.createUser ("m_02", "12", "Manager_Name_02", "manager_email_02@email.com", GROUP.Manager);
            administratorBean.createUser ("m_03", "13", "Manager_Name_03", "manager_email_03@email.com", GROUP.Manager);
            administratorBean.createUser ("m_04", "14", "Manager_Name_04", "manager_email_04@email.com", GROUP.Manager);
            administratorBean.createUser ("m_05", "16", "Manager_Name_05", "manager_email_05@email.com", GROUP.Manager);
            administratorBean.createUser ("m_06", "17", "Manager_Name_06", "manager_email_06@email.com", GROUP.Manager);
            administratorBean.createUser ("11", "111", "11", "attendant_email_01@email.com", GROUP.Attendant);
            administratorBean.createUser ("p_02", "112", "Attendant_Name_02", "attendant_email_02@email.com", GROUP.Attendant);
            administratorBean.createUser ("p_03", "113", "Attendant_Name_03", "attendant_email_03@email.com", GROUP.Attendant);
            administratorBean.createUser ("p_04", "114", "Attendant_Name_04", "attendant_email_04@email.com", GROUP.Attendant);
            administratorBean.createUser ("p_05", "115", "Attendant_Name_05", "attendant_email_05@email.com", GROUP.Attendant);
            administratorBean.createUser ("p_06", "116", "Attendant_Name_06", "attendant_email_06@email.com", GROUP.Attendant);
            administratorBean.createUser ("p_07", "117", "Attendant_Name_07", "attendant_email_07@email.com", GROUP.Attendant);
            administratorBean.createUser ("p_08", "118", "Attendant_Name_08", "attendant_email_08@email.com", GROUP.Attendant);
            administratorBean.createUser ("p_09", "119", "Attendant_Name_09", "attendant_email_09@email.com", GROUP.Attendant);


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
           
         
            attendantBean.enrollAttendantInEvent(Long.parseLong("11"), Long.parseLong("23"));
            attendantBean.enrollAttendantInEvent(Long.parseLong("12"), Long.parseLong("24"));
            attendantBean.enrollAttendantInEvent(Long.parseLong("14"), Long.parseLong("24"));
            attendantBean.enrollAttendantInEvent(Long.parseLong("13"), Long.parseLong("24"));
            attendantBean.enrollAttendantInEvent(Long.parseLong("11"), Long.parseLong("24"));
            
             
            managerBean.enrollManagerInEvent(Long.parseLong("5"), Long.parseLong("20"));
            managerBean.enrollManagerInEvent(Long.parseLong("6"), Long.parseLong("21"));
            managerBean.enrollManagerInEvent(Long.parseLong("7"), Long.parseLong("22"));
            managerBean.enrollManagerInEvent(Long.parseLong("8"), Long.parseLong("21"));
            managerBean.enrollManagerInEvent(Long.parseLong("6"), Long.parseLong("22"));
            managerBean.enrollManagerInEvent(Long.parseLong("5"), Long.parseLong("23"));
            managerBean.enrollManagerInEvent(Long.parseLong("5"), Long.parseLong("24"));

            
            categoryBean.createEventCategory("Aula-DAE");
            categoryBean.createEventCategory("Aula-IS");
            categoryBean.createEventCategory("Seminário - Informática no Ensino Básico");
            categoryBean.createEventCategory("Seminário - A Saúde do Ensino Superior Politécnico");
            categoryBean.createEventCategory("Workshop - Software Livre na Pastelaria");
            categoryBean.createEventCategory("Exame - DAE");
            categoryBean.createEventCategory("AulaOT - DAE");
            
            categoryBean.createAttendantCategory("Engenharia Informática");
            categoryBean.createAttendantCategory("Informática para a Saúde");
            categoryBean.createAttendantCategory("Bolos com Chantilly");
           
            eventBean.enrollEventInCategory(Long.parseLong("23"), Long.parseLong("32"));
            eventBean.enrollEventInCategory(Long.parseLong("24"), Long.parseLong("32"));
            eventBean.enrollEventInCategory(Long.parseLong("25"), Long.parseLong("33"));
            eventBean.enrollEventInCategory(Long.parseLong("26"), Long.parseLong("33"));
            eventBean.enrollEventInCategory(Long.parseLong("23"),Long.parseLong("34"));
            eventBean.enrollEventInCategory(Long.parseLong("24"),Long.parseLong("35"));
            eventBean.enrollEventInCategory(Long.parseLong("25"),Long.parseLong("34"));
            eventBean.enrollEventInCategory(Long.parseLong("26"),Long.parseLong("36"));

            
            attendantBean.enrollAttendantInCategory(Long.parseLong("11"), Long.parseLong("40"));
            attendantBean.enrollAttendantInCategory(Long.parseLong("11"), Long.parseLong("39"));
            
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
