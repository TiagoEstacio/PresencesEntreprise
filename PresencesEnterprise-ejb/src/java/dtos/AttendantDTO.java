
package dtos;

import static auxCategories.UserType.ATTENDANT;
import entities.Event;
import entities.UserGroup;
import entities.UserGroup.GROUP;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "Attendant")
@XmlAccessorType(XmlAccessType.FIELD)
public class AttendantDTO extends UserDTO implements Serializable {
      
    public AttendantDTO() {
    }    
    
    public AttendantDTO(Long id, String username, String password, String name, String email) {
        super(id, username, password, name, email, GROUP.Attendant);   
    }
    
    @Override
    public void reset() {
        super.reset();
    }
   
}