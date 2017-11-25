package de.iubh.fernstudium.ticketsystem.domain.event.payload;

import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;

/**
 * Payload für Cache-Updates
 */
public class CachePayload {

    private UserDTO userDTO;
    private CategoryDTO categoryDTO;

    public CachePayload() {
    }

    public CachePayload(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public CachePayload(CategoryDTO categoryDTO) {
        this.categoryDTO = categoryDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public CategoryDTO getCategoryDTO() {
        return categoryDTO;
    }

    public void setCategoryDTO(CategoryDTO categoryDTO) {
        this.categoryDTO = categoryDTO;
    }
}
