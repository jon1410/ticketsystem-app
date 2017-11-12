package de.iubh.fernstudium.ticketsystem.util.test;

import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.UserDBService;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.util.config.StartUpConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import javax.inject.Inject;

@RunWith(MockitoJUnitRunner.class)
public class StartUpConfigTest {

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @ObjectUnderTest
    StartUpConfig startUpConfig;

    @Inject
    private UserDBService userDBService;

    @Test
    public void testPostConstructOK(){
        Mockito.when(userDBService.findById(Mockito.anyString())).thenReturn(buildUserEntity());
        startUpConfig.createAdminUser();
        Mockito.verify(userDBService, Mockito.never()).persistUser(Mockito.any(UserEntity.class));
    }

    @Test
    public void testPostConstructNOK(){
        Mockito.when(userDBService.findById(Mockito.anyString())).thenReturn(null);
        startUpConfig.createAdminUser();
        Mockito.verify(userDBService, Mockito.times(1)).persistUser(Mockito.any(UserEntity.class));
    }

    private UserEntity buildUserEntity() {
        return new UserEntity("user", "firstName", "lastName", "pw", UserRole.AD);
    }
}
