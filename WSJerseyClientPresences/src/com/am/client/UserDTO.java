
package com.am.client;


import java.io.Serializable;

public class UserDTO implements Serializable{
    
    protected Long id;
    protected String username;
    protected String password;
    protected String name;
    protected String email;
 
    public UserDTO() {
    }    
    
    public UserDTO(Long id, String username, String password, String name, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
 
    }
    
    public void reset() {
        setUsername(null);
        setPassword(null);
        setName(null);
        setEmail(null);
    }        

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

  
}
