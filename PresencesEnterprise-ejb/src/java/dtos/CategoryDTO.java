
package dtos;


public class CategoryDTO {
    private Long id;
    private String name;
    private String type;
    
    public CategoryDTO(){ 
    }
    
    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;       
    }
    
    public void reset(){
        this.name = null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
