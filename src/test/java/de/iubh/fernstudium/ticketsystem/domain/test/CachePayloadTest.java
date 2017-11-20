package de.iubh.fernstudium.ticketsystem.domain.test;

import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.event.payload.CachePayload;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CachePayloadTest {

    @Test
    public void testGetSet(){

        CachePayload cachePayload = new CachePayload();
        cachePayload.setCategoryDTO(buildCategoryDTO());
        assertNotNull(cachePayload.getCategoryDTO());

        cachePayload.setUserDTO(buildUserDTO());
        assertNotNull(cachePayload.getUserDTO());

    }

    private UserDTO buildUserDTO() {
        return new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU);
    }

    private CategoryDTO buildCategoryDTO() {
        return new CategoryDTO("Test", "Test",
                buildUserDTO());
    }
}
