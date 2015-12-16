/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import dtos.AttendantDTO;
import dtos.EventDTO;
import ejbs.AttendantBean;
import ejbs.EventBean;
import exceptions.EntityDoesNotExistsException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author rmartin
 */
@Path("/events")
public class RESTServiceLayer {
    
    
    
    @EJB
    private EventBean eventBean;
    
    @EJB
   private AttendantBean attendantBean;
    
    @Context private SecurityContext securityContext;
  
   /* @GET
    @RolesAllowed({"Administrator"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("all")
    public List<StudentDTO> getAll() {
        return studentBean.getAll();
    }
    */
    @GET
    @RolesAllowed({"Attendant"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("attendant_events")
    public List<EventDTO> getAttendantEvents() {
       System.out.println("chegou pedido");
        List<EventDTO> events = null;
        try {
             AttendantDTO att = attendantBean.getAttendant(securityContext.getUserPrincipal().getName());
            events =    eventBean.getAttendantEvents(att.getId());
        } catch (EntityDoesNotExistsException ex) {
            Logger.getLogger(RESTServiceLayer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return events;
    }
    
    //update event key
 /* @POST
    @RolesAllowed({"Attendant"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("attendant_event_update_key")
    
     /*public String updateStudentREST() {
       try {
           client.target(baseUri)
                   .path("students/update")
                   .path(currentStudent.getUsername())
                   .path(currentStudent.getPassword())
                   .path(currentStudent.getName())
                   .path(currentStudent.getEmail())
                   .path(Integer.toString(currentStudent.getCourseCode()))
                   .request(MediaType.TEXT_PLAIN)
                   .post(Entity.text("A"));
           
           return "/faces/index?faces-redirect=true";

       } catch (ResponseProcessingException ex) {
           logger.log(Level.SEVERE, "ResponseProcessingException thrown");
           logger.log(Level.SEVERE, "Error is {0}", ex.getMessage());
       } catch (Exception e) {
           FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
       }
       return "admin_students_update";
   }
  */
    
    
}
