/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 *
 * @author ITWannaBe
 */
@Entity(name = "USERS_GROUPS")
public class UserGroup implements Serializable {
   public static enum GROUP{
       Administrator,Manager,Attendant;
       
   };
   

  @Id
  @Enumerated(EnumType.STRING)
  private GROUP group_Name;
  
  @Id
  @OneToOne
  @JoinColumn(name = "ID")
  private User user;
  
  public UserGroup(){
  }
  
  public UserGroup(GROUP group, User user){
      this.group_Name = group;
      this.user = user;
  }

  public GROUP getGroupName() {
      return group_Name;
  }

  public void setGroupName(GROUP groupName) {
      this.group_Name = groupName;
  }

  public User getUser() {
      return user;
  }

  public void setUser(User user) {
      this.user = user;
  }

  @Override
  public int hashCode() {
      int hash = 5;
      hash = 79 * hash + Objects.hashCode(this.group_Name);
      hash = 79 * hash + Objects.hashCode(this.user);
      return hash;
  }

  @Override
  public boolean equals(Object obj) {
      if (obj == null) {
          return false;
      }
      if (getClass() != obj.getClass()) {
          return false;
      }
      final UserGroup other = (UserGroup) obj;
      if (this.group_Name != other.group_Name) {
          return false;
      }
      if (!Objects.equals(this.user, other.user)) {
          return false;
      }
      return true;
  }

  @Override
  public String toString() {
      return "UserGroup{" + "groupName=" + group_Name + ", user=" + user + '}';
  }
    
}
