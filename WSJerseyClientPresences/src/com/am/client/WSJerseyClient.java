/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.am.client;

import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

/**
 *
 * @author rmartin
 */
public class WSJerseyClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String baseUri = "http://localhost:8080/PresencesEnterprise_RESTWS/webapi";
        Client client = ClientBuilder.newClient();
        HttpAuthenticationFeature feature =  null;
        
        
        /*//ADMIN
        feature = HttpAuthenticationFeature.basic("a1", "a1");

        client.register(feature);

        List<StudentDTO> returnedStudents = null;
        try {
            returnedStudents = client.target(baseUri)
                    .path("/students/all")
                    .request(MediaType.APPLICATION_XML)
                    .get(new GenericType<List<StudentDTO>>() {
                    });
            if (returnedStudents == null) {
                System.out.println("Returned null...");
                return;
            } else {
                for (StudentDTO student : returnedStudents) {
                    System.out.println(student);
                }

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        */
        //STUDENT
        System.out.println("teste--");
       //USERNAME + PASSWORD
        feature = HttpAuthenticationFeature.basic("11", "111");
        client.register(feature);
        List<EventDTO> returnedEvents = null;
        System.out.println("teste-1");
        try {
            System.out.println("teste-2");
            returnedEvents = client.target(baseUri)
                    .path("/events/attendant_events")//.path("/attendant/attendant_events")
                    .request(MediaType.APPLICATION_XML)
                    .get(new GenericType<List<EventDTO>>() {
                    });
            if (returnedEvents == null) {
                System.out.println("Returned null...");
                return;
            } else {
                
                
                for (EventDTO event : returnedEvents) {
                    System.out.println(event.getName());
                }

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        
    }
}
