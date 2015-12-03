
package dtos;

import static auxCategories.UserType.ADMINISTRATOR;
import entities.UserGroup.GROUP;
import java.io.Serializable;
import java.util.List;

public class AdministratorDTO extends UserDTO implements Serializable {
    
    public AdministratorDTO() {
    }    
    
    public AdministratorDTO(Long id, String username, String password, String name, String email) {
        super(id, username, password, name, email, GROUP.Administrator);   
    }
    
    @Override
    public void reset() {
        super.reset();
    }
    
}