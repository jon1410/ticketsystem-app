package de.iubh.fernstudium.ticketsystem.beans.test;

import de.iubh.fernstudium.ticketsystem.beans.TutorRepositoryBean;
import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({FacesContextUtils.class})
public class TutorRepositoryBeanPowerMockTest {

    private List<UserDTO> userDTOList;

    @InjectMocks
    TutorRepositoryBean tutorRepositoryBean;

    @Mock
    private UserService userService;




    @Before
    public void init(){
        userDTOList = new ArrayList<>(5);
        for(int i=0; i<5; i++){
            userDTOList.add(buildUserDTO(i));
        }
        tutorRepositoryBean = new TutorRepositoryBean();
        MockitoAnnotations.initMocks(this);
        Mockito.when(userService.getAllTutors()).thenReturn(userDTOList);
        tutorRepositoryBean.initializeTutors();
    }

    @Test
    public void test1PostConstruct(){
        assertNotNull(tutorRepositoryBean.getAllTutors());
        assertTrue(tutorRepositoryBean.getAllTutors().size() == 5);
    }

    @Test
    public void test2SetNewUser(){
        tutorRepositoryBean.setTutor(buildUserDTO(1));
        assertTrue(tutorRepositoryBean.getAllTutors().size() == 6);
    }

    @Test
    public void test3SetNewUserToNullList(){
        tutorRepositoryBean.setAllTutors(null);
        tutorRepositoryBean.setTutor(buildUserDTO(1));
        assertTrue(tutorRepositoryBean.getAllTutors().size() == 1);
    }

    @Test
    public void test4UpdateCache(){
        UserDTO userDTO = buildUserDTO(1);
        userDTO.setFirstName("newFirstName");
        userDTO.setLastName("newLastName");

        tutorRepositoryBean.updateCache(userDTO);

        List<UserDTO> userDTOList = tutorRepositoryBean.getAllTutors();

        UserDTO fromList = null;
        for(UserDTO u : userDTOList){
            if(u.getUserId().equals(userDTO.getUserId())){
                fromList = u;
            }
        }
        assertNotNull(fromList);
        assertEquals("newFirstName", fromList.getFirstName());
        assertEquals("newLastName", fromList.getLastName());
    }

    private static UserDTO buildUserDTO(int i) {
        return new UserDTO("userid" + i, "firstName", "lastName", "pw", UserRole.TU);
    }
}
