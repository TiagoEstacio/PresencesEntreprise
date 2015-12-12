/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import static com.sun.xml.ws.security.addressing.impl.policy.Constants.logger;
import dtos.AttendantCategoryDTO;
import dtos.CategoryDTO;
import dtos.EventCategoryDTO;
import dtos.EventDTO;
import ejbs.CategoryBean;
import exceptions.EntityAlreadyExistsException;
import exceptions.EntityDoesNotExistsException;
import exceptions.MyConstraintViolationException;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class CategoryManager {

    @EJB
    private CategoryBean categoryBean;

    private CategoryDTO currentCategory;
    private CategoryDTO newCategory;

    public CategoryManager() {
        currentCategory = new CategoryDTO();
        newCategory = new CategoryDTO();
    }

    public String createCategory() {
        if (currentCategory.getType().toLowerCase().equals("event")) {
            try {
                categoryBean.createEventCategory(
                        currentCategory.getName());
                newCategory.reset();
                return "/faces/administrator/administrator_panel?faces-redirect=true";
            } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
                FacesExceptionHandler.handleException(e, e.getMessage(), logger);
            } catch (Exception e) {
                FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            }
        }
        if (currentCategory.getType().toLowerCase().equals("attendant")) {
            try {
                categoryBean.createAttendantCategory(
                        currentCategory.getName());
                newCategory.reset();
                return "/faces/administrator/administrator_panel?faces-redirect=true";
            } catch (EntityAlreadyExistsException | MyConstraintViolationException e) {
                FacesExceptionHandler.handleException(e, e.getMessage(), logger);
            } catch (Exception e) {
                FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            }

        }
        return null;
    }

    public List<EventCategoryDTO> getAllEventCategories() {
        try {
            return categoryBean.getAllEventCategories();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }

    public List<AttendantCategoryDTO> getAllAttendantCategories() {
        try {
            return categoryBean.getAllAttendantCategories();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }

    public List<EventDTO> getAllCategoryEvents() {
        try {
            return categoryBean.getAllCategoryEvents(currentCategory.getId());
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }

    public int getNumberAttendants(Long id) throws EntityDoesNotExistsException {
        return categoryBean.getNumberofAttendants(id);
    }

    public int getNumberEvents(Long id) throws EntityDoesNotExistsException {
        return categoryBean.getNumberofEvents(id);
    }

    /*
     public List<CategoryDTO> getAllCategoriesOfCurrentEvent() {
     try {
     return categoryBean.getAllCategoriesOfCurrentEvent(currentEvent.getId());
     } catch (Exception e) {
     FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
     return null;
     }
     }
     */
    public String updateCategory() {

        try {
            categoryBean.updateCategory(
                    currentCategory.getId(),
                    currentCategory.getName());
            return "/faces/administrator/category_lists?faces-redirect=true";

        } catch (EntityDoesNotExistsException | MyConstraintViolationException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
        return "/faces/administrator/category_update?faces-redirect=true";
    }

    public void removeCategory(ActionEvent event) {
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteCategoryId");
            Long id = Long.parseLong(param.getValue().toString());
            categoryBean.removeCategory(id);
        } catch (EntityDoesNotExistsException e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), logger);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }

    public CategoryDTO getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(CategoryDTO currentCategory) {
        this.currentCategory = currentCategory;
    }

}
