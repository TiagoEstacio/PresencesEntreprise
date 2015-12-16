/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import auxCategories.UserType;
import static auxCategories.UserType.UNDEFINED;
import entities.UserGroup.GROUP;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;



@Entity
@Table(name = "USERS",
uniqueConstraints =
@UniqueConstraint(columnNames = {"USERNAME"}))
@NamedQueries({
    @NamedQuery(
        name="getAllUsers",
        query="SELECT us FROM User us ORDER BY us.id"
    )
})
public abstract class User implements Serializable {
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    
    @NotNull
    protected String username;
    
    @NotNull
    protected String password;
   
    
    @NotNull(message = "Name must not be empty")
   protected String name;
  
    
    @NotNull
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
   protected UserGroup group;
   
    
    @NotNull
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            + "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
            message = "{invalid.email}")
    protected String email;
        
    public User() {
    }

    public User(String username, String password, String name, String email, GROUP group){
        this.username = username;
     
        this.name = name;
        this.email = email;
       
        this.password = hashPassword(password);
       this.group = new UserGroup(group, this);
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

    public String getUserName() {
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

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    

   

  
    @Override
    public String toString() {
        return "entities.User[id=" + id + "]: "+ name;
    }

     private String hashPassword(String password) {
       char[] encoded = null;
       try {
           ByteBuffer passwdBuffer = Charset.defaultCharset().encode(CharBuffer.wrap(password));
           byte[] passwdBytes = passwdBuffer.array();
           MessageDigest mdEnc = MessageDigest.getInstance("SHA-256");
           mdEnc.update(passwdBytes, 0, password.toCharArray().length);
           encoded = new BigInteger(1, mdEnc.digest()).toString(16).toCharArray();
       } catch (NoSuchAlgorithmException ex) {
           Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
       }
       return new String(encoded);
   }
    
    
}
