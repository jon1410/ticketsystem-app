package de.iubh.fernstudium.ticketsystem.services.test;

import de.iubh.fernstudium.ticketsystem.db.entities.CategoryEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.CommentEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.TicketEntity;
import de.iubh.fernstudium.ticketsystem.db.entities.UserEntity;
import de.iubh.fernstudium.ticketsystem.db.services.TicketDBService;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.services.impl.TicketServiceImpl;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceTest {

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @ObjectUnderTest(implementation = TicketServiceImpl.class)
    TicketService ticketService;

    @Inject
    private TicketDBService ticketDBService;
    @Inject
    private UserService userService;
    
    @Test
    public void testGetTicketByIDOK() throws NoSuchTicketException {
        when(ticketDBService.getTicketById(anyLong())).thenReturn(buildTicketEntity());
        TicketDTO ticketDTO = ticketService.getTicketByID(1L);
        assertNotNull(ticketDTO);
    }

    @Test(expected = NoSuchTicketException.class)
    public void testGetTicketByIDException() throws NoSuchTicketException {
        when(ticketDBService.getTicketById(anyLong())).thenReturn(null);
        TicketDTO ticketDTO = ticketService.getTicketByID(1L);
    }

    @Test
    public void testGetOpenTicketsForUserIdEmptyList() throws UserNotExistsException {
        UserEntity userEntity = buildUserEntity();
        when(userService.getUserByUserId(anyString())).thenReturn(userEntity.toDto());
        when(ticketDBService.getOpenTicketsForUserId(Mockito.any(UserEntity.class))).thenReturn(null);
        List<TicketDTO> ticketDTOList = ticketService.getOpenTicketsForUserId("test");
        assertTrue(CollectionUtils.isEmpty(ticketDTOList));
    }

    @Test
    public void testGetOpenTicketsForUserIdOK() throws UserNotExistsException {
        UserEntity userEntity = buildUserEntity();
        List<TicketEntity> ticketEntityList = buildTicketEntityList(3);
        when(userService.getUserByUserId(anyString())).thenReturn(userEntity.toDto());
        when(ticketDBService.getOpenTicketsForUserId(Mockito.any(UserEntity.class))).thenReturn(ticketEntityList);
        List<TicketDTO> ticketDTOList = ticketService.getOpenTicketsForUserId("test");
        assertFalse(CollectionUtils.isEmpty(ticketDTOList));
    }

    @Test
    public void testGetTicketsReportedByUserIdEmptyList()throws UserNotExistsException{
        UserEntity userEntity = buildUserEntity();
        when(userService.getUserByUserId(anyString())).thenReturn(userEntity.toDto());
        when(ticketDBService.getTicketsReportedByUserId(Mockito.any(UserEntity.class))).thenReturn(null);
        List<TicketDTO> ticketDTOList = ticketService.getTicketsReportedByUserId("test");
        assertTrue(CollectionUtils.isEmpty(ticketDTOList));
    }

    @Test
    public void testGetTicketsReportedByUserIdOK() throws UserNotExistsException {
        UserEntity userEntity = buildUserEntity();
        List<TicketEntity> ticketEntityList = buildTicketEntityList(3);
        when(userService.getUserByUserId(anyString())).thenReturn(userEntity.toDto());
        when(ticketDBService.getTicketsReportedByUserId(Mockito.any(UserEntity.class))).thenReturn(ticketEntityList);
        List<TicketDTO> ticketDTOList = ticketService.getTicketsReportedByUserId("test");
        assertFalse(CollectionUtils.isEmpty(ticketDTOList));
    }

    @Test
    public void testChangeStatusOK() throws NoSuchTicketException {
        when(ticketDBService.changeStauts(anyLong(), any(TicketStatus.class))).thenReturn(true);
        ticketService.changeStatus(1L, TicketStatus.RES);
    }

    @Test(expected = NoSuchTicketException.class)
    public void testChangeStatusNOK() throws NoSuchTicketException {
        when(ticketDBService.changeStauts(anyLong(), any(TicketStatus.class))).thenReturn(false);
        ticketService.changeStatus(1L, TicketStatus.RES);
    }

    @Test
    public void testAddCommentOK() throws UserNotExistsException, NoSuchTicketException {
        UserEntity userEntity = buildUserEntity();
        when(userService.getUserByUserId(anyString())).thenReturn(userEntity.toDto());
        when(ticketDBService.addComment(anyLong(), any(CommentEntity.class))).thenReturn(buildTicketEntity());
        TicketDTO dto = ticketService.addComment(1L, "Comment", "user");
        assertNotNull(dto);
    }

    @Test(expected = NoSuchTicketException.class)
    public void testAddCommentNOK() throws UserNotExistsException, NoSuchTicketException {
        UserEntity userEntity = buildUserEntity();
        when(userService.getUserByUserId(anyString())).thenReturn(userEntity.toDto());
        when(ticketDBService.addComment(anyLong(), any(CommentEntity.class))).thenReturn(null);
        TicketDTO dto = ticketService.addComment(1L, "Comment", "user");
    }

    @Test
    public void testCreateTicketOK(){
        when(ticketDBService.createTicket(any(TicketEntity.class))).thenReturn(buildTicketEntity());
        TicketEntity t = buildTicketEntity();
        TicketDTO dto = ticketService.createTicket(t.toDto());
        assertNotNull(dto);
    }

    @Test
    public void testCreateMasterTicketOK() throws NoSuchTicketException {
        when(ticketDBService.createMasterTicket(anyLong(), anyList())).thenReturn(buildTicketEntity());
        TicketDTO dto = ticketService.createMasterTicket(1L,  Arrays.asList(new Long[]{2L, 3L, 4L}));
        assertNotNull(dto);
    }

    @Test
    public void testCreateMasterTicket2OK() throws NoSuchTicketException {
        when(ticketDBService.createMasterTicket(anyLong(), anyLong())).thenReturn(buildTicketEntity());
        TicketDTO dto = ticketService.createMasterTicket(1L,  3L);
        assertNotNull(dto);
    }

    @Test
    public void testChangeCategoryOfTicket() throws NoSuchTicketException {
        when(ticketDBService.changeCategoryOfTicket(anyLong(), any(CategoryEntity.class))).thenReturn(buildTicketEntity());
        CategoryEntity categoryEntity = buildCategoryEntity();
        TicketDTO ticketDTO  = ticketService.changeCategoryOfTicket(1L, categoryEntity.toDto());
        assertNotNull(ticketDTO);
    }

    private List<TicketEntity> buildTicketEntityList(int size) {
        List<TicketEntity> ticketEntityList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ticketEntityList.add(buildTicketEntity());
        }
        return ticketEntityList;
    }

    private TicketEntity buildTicketEntity() {
        return new TicketEntity("title", "desc", TicketStatus.NEW, buildUserEntity(),
                DateTimeUtil.now(), buildCategoryEntity(), buildUserEntity(),
                null, null, null);
    }

    private UserEntity buildUserEntity() {
        return new UserEntity("user", "firstName", "lastName", "pw", UserRole.TU);
    }

    private CategoryEntity buildCategoryEntity() {
        return new CategoryEntity("ID", "name", buildUserEntity());
    }
}
