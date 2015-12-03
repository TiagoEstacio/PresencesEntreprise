
package dtos;

import static auxCategories.UserType.ATTENDANT;
import entities.Event;
import entities.UserGroup;
import entities.UserGroup.GROUP;
import java.io.Serializable;
import java.util.List;

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