/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package web;

import dtos.UserDTO;
import ejbs.AdministratorBean;
import entities.Attendant;
import entities.Manager;
import entities.User;
import static entities.UserGroup.GROUP.Manager;
import exceptions.EntityDoesNotExistsException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
*
* 
*/
@ManagedBean
@SessionScoped
public class LoginManager {

   /**
    * Creates a new instance of UserManager
    */
   private String username;
   private String password;
   private UserDTO currentUser;
      
   private boolean loginFlag = true;
   private static final Logger logger = Logger.getLogger("web.LoginManager");
   
   //TESTE
   @EJB
   private AdministratorBean administratorBean;
      
   public LoginManager() {
   }

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
  
   public void prepareLogin(){
       loginFlag = true;
   }

   public boolean isLoginFlag() {
       return loginFlag;
   }

   public void setLoginFlag(boolean loginFlag) {
       this.loginFlag = loginFlag;
   }

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDTO currentUser) {
        this.currentUser = currentUser;
    }
 
}