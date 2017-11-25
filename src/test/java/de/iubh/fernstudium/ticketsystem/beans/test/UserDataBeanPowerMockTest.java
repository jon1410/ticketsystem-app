package de.iubh.fernstudium.ticketsystem.beans.test;

import com.sun.org.apache.regexp.internal.RE;
import de.iubh.fernstudium.ticketsystem.beans.CurrentUserBean;
import de.iubh.fernstudium.ticketsystem.beans.UserDataBean;
import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.CategoryNotFoundException;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;
import de.iubh.fernstudium.ticketsystem.dtos.CategoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.HistoryDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.CategoryService;
import de.iubh.fernstudium.ticketsystem.services.HistoryService;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.services.impl.EventProducer;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.primefaces.context.RequestContext;

import javax.faces.context.FacesContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({FacesContextUtils.class, RequestContext.class})
public class UserDataBeanPowerMockTest {

    @InjectMocks
    UserDataBean userDataBean;

    @Mock
    private UserService userService;
    @Mock
    private TicketService ticketService;
    @Mock
    private CurrentUserBean currentUserBean;
    @Mock
    private EventProducer eventProducer;
    @Mock
    private HistoryService historyService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private RequestContext requestContext;
    @Mock
    private FacesContext facesContext;

    @Before
    public void init() throws UserNotExistsException {
        userDataBean = new UserDataBean();
        MockitoAnnotations.initMocks(this);
        when(ticketService.getOpenTicketsForUserId(anyString())).thenReturn(buildDTOList(4));
        when(ticketService.getTicketsReportedByUserId(anyString())).thenReturn(buildDTOList(7));
        userDataBean.init("test");
    }

    @Test
    public void testTerminateTicket(){
        TicketDTO ticketDTO = buildTicketDTO();
        userDataBean.terminateTicket(ticketDTO);
        verify(eventProducer, times(1)).produceHistoryEvent(anyLong(), any(HistoryAction.class), anyString());
    }

    @Test
    public void testStartProgressActiveTicketFalse() throws NoSuchTicketException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        userDataBean.startProgress();
        verify(ticketService, never()).changeStatus(anyLong(), any(TicketStatus.class));
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testStartProgress() throws NoSuchTicketException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());

        userDataBean.setActiveTicket(buildTicketDTO());
        userDataBean.startProgress();
        verify(ticketService, times(1)).changeStatus(anyLong(), any(TicketStatus.class));
        assertEquals(TicketStatus.IPU, userDataBean.getActiveTicket().getTicketStatus());

        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testStartProgressWithException() throws NoSuchTicketException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());
        doThrow(new NoSuchTicketException("Test")).when(ticketService).changeStatus(anyLong(), any(TicketStatus.class));
        userDataBean.setActiveTicket(buildTicketDTO());
        userDataBean.startProgress();

        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testResolveTicketOK(){
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());
        userDataBean.setActiveTicket(buildTicketDTO());
        userDataBean.resolveTicket();
        assertEquals(TicketStatus.RES, userDataBean.getActiveTicket().getTicketStatus());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testFinishTicketOK(){
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());
        userDataBean.setActiveTicket(buildTicketDTO());
        userDataBean.finishTicket();
        assertEquals(TicketStatus.CLO, userDataBean.getActiveTicket().getTicketStatus());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testAddTicket(){
        userDataBean.addTicket(buildTicketDTO());
        assertTrue(userDataBean.getTickets().size() == 5);
    }

    @Test
    public void testAddTicketNull(){
        userDataBean.setTickets(null);
        userDataBean.addTicket(buildTicketDTO());
        assertTrue(userDataBean.getTickets().size() == 1);
    }

    @Test
    public void testAddTicketToReporter(){
        userDataBean.addTicketToReporter(buildTicketDTO());
        assertTrue(userDataBean.getReportedByLoggedInUser().size() == 8);
    }

    @Test
    public void testAddTicketToReporterNull(){
        userDataBean.setReportedByLoggedInUser(null);
        userDataBean.addTicketToReporter(buildTicketDTO());
        assertTrue(userDataBean.getReportedByLoggedInUser().size() == 1);
    }

    @Test
    public void testCreateMasterTicketOK() throws NoSuchTicketException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());
        userDataBean.setActiveTicket(buildTicketDTO());

        TicketDTO dto = buildTicketDTO();
        List<Long> longs = Arrays.asList(new Long[]{1L, 2L,3L});

        when(ticketService.createMasterTicket(anyLong(), anyList())).thenReturn(dto);
        userDataBean.setChildTickets(longs);
        userDataBean.createMasterTicket();
        assertNull(userDataBean.getChildTickets());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testCreateMasterTicketException() throws NoSuchTicketException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());
        userDataBean.setActiveTicket(buildTicketDTO());

        TicketDTO dto = buildTicketDTO();
        List<Long> longs = Arrays.asList(new Long[]{1L, 2L,3L});

        when(ticketService.createMasterTicket(anyLong(), anyList())).thenThrow(new NoSuchTicketException("test"));
        userDataBean.setChildTickets(longs);
        userDataBean.createMasterTicket();
        assertNull(userDataBean.getChildTickets());
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testCreateMasterTicketEmptyList() throws NoSuchTicketException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());
        userDataBean.setActiveTicket(buildTicketDTO());
        userDataBean.setChildTickets(new ArrayList<>());
        userDataBean.createMasterTicket();
        assertNull(userDataBean.getChildTickets());
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testAddChildTicketNull(){
        assertNull(userDataBean.getChildTickets());
        userDataBean.addChildTicket(buildTicketDTO());
        assertTrue(userDataBean.getChildTickets().size() == 1);
    }

    @Test
    public void testAddChildTicket(){
        List<Long> longs = Arrays.asList(new Long[]{1L, 2L});
        userDataBean.setChildTickets(new ArrayList<>(longs));
        userDataBean.addChildTicket(buildTicketDTO());
        assertTrue(userDataBean.getChildTickets().size() == 3);
    }

    @Test
    public void testAddComment() throws UserNotExistsException, NoSuchTicketException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());
        when(ticketService.addComment(anyLong(), anyString(), anyString())).thenReturn(buildTicketDTO());

        userDataBean.setActiveTicket(buildTicketDTO());
        userDataBean.setNewComment("neuer Comment");
        userDataBean.addComment();
        assertNull(userDataBean.getNewComment());
        PowerMockito.verifyStatic(VerificationModeFactory.times(0));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testAddCommentUserNotExistsException() throws UserNotExistsException, NoSuchTicketException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());
        when(ticketService.addComment(anyLong(), anyString(), anyString())).thenThrow(new UserNotExistsException("test"));

        userDataBean.setActiveTicket(buildTicketDTO());
        userDataBean.setNewComment("neuer Comment");
        userDataBean.addComment();
        assertNull(userDataBean.getNewComment());
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testAddCommentNoSuchTicketException() throws UserNotExistsException, NoSuchTicketException {
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());
        when(ticketService.addComment(anyLong(), anyString(), anyString())).thenThrow(new NoSuchTicketException("test"));

        userDataBean.setActiveTicket(buildTicketDTO());
        userDataBean.setNewComment("neuer Comment");
        userDataBean.addComment();
        assertNull(userDataBean.getNewComment());
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testUpdateCache(){
        TicketDTO ticketDTO = userDataBean.getTickets().get(0);
        ticketDTO.setId(99L);
        userDataBean.getTickets().set(0, ticketDTO);

        ticketDTO.setDescription("Test");
        userDataBean.updateCache(ticketDTO);

        assertEquals("Test", userDataBean.getTickets().get(0).getDescription());
    }

    @Test
    public void testGetHistoryOfActiveTicketEmpty(){
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());

        userDataBean.setActiveTicket(buildTicketDTO());
        List<HistoryDTO> historyDTOS = userDataBean.getHistoryOfActiveTicket();
        assertTrue(CollectionUtils.isEmpty(historyDTOS));

        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testGetHistoryOfActiveTicketNotEmpty(){
        PowerMockito.mockStatic(FacesContextUtils.class);

        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());

        when(historyService.getHistoryForTicket(anyLong())).thenReturn(buildHistoryDTOList());
        userDataBean.setActiveTicket(buildTicketDTO());
        List<HistoryDTO> historyDTOS = userDataBean.getHistoryOfActiveTicket();
        assertTrue(CollectionUtils.isNotEmpty(historyDTOS));
        assertTrue(historyDTOS.size() == 5);

        RequestContext.releaseThreadLocalCache();
    }


    @Test
    public void testShowHistory(){
        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());

        userDataBean.showHistory();
        verify(requestContext, times(1)).execute(anyString());

        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testCheckLoggedInUser(){
        when(currentUserBean.getUserId()).thenReturn(null);
        assertEquals(FacesContextUtils.REDIRECT_LOGIN, userDataBean.checkLoggedInUser());
    }

    @Test
    public void testCheckLoggedInUserCurrentUserBeanNull(){
        currentUserBean = null;
        Whitebox.setInternalState(userDataBean, "currentUserBean", currentUserBean);
        assertEquals(FacesContextUtils.REDIRECT_LOGIN, userDataBean.checkLoggedInUser());
    }

    @Test
    public void testCheckLoginOK(){
        when(currentUserBean.getUserId()).thenReturn("test");
        assertNull(userDataBean.checkLoggedInUser());
    }

    @Test
    public void testTerminateActiveTicket(){
        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());
        userDataBean.setActiveTicket(buildTicketDTO());
        userDataBean.terminateActiveTicket();
        verify(eventProducer, times(1)).produceHistoryEvent(anyLong(), any(HistoryAction.class), anyString());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testChangeCategoryOK() throws CategoryNotFoundException, NoSuchTicketException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());

        userDataBean.setActiveTicket(buildTicketDTO());
        userDataBean.setNewCategoryId("cid");
        assertNotNull(userDataBean.getNewCategoryId());

        CategoryDTO categoryDTO = buildCategoryDTO();
        categoryDTO.setCategoryId("newId");
        TicketDTO ticketDTO = buildTicketDTO();
        ticketDTO.setCategory(categoryDTO);

        when(categoryService.getCategoryById(anyString())).thenReturn(categoryDTO);
        when(ticketService.changeCategoryOfTicket(anyLong(), any(CategoryDTO.class))).thenReturn(ticketDTO);

        userDataBean.changeCateogry();

        assertEquals("newId", userDataBean.getActiveTicket().getCategory().getCategoryId());
        verify(eventProducer, times(1)).produceHistoryEvent(anyLong(), any(HistoryAction.class), anyString());

        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testChangeCategoryNOKException() throws CategoryNotFoundException, NoSuchTicketException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());

        userDataBean.setActiveTicket(buildTicketDTO());
        userDataBean.setNewCategoryId("cid");
        assertNotNull(userDataBean.getNewCategoryId());

        CategoryDTO categoryDTO = buildCategoryDTO();
        categoryDTO.setCategoryId("newId");
        TicketDTO ticketDTO = buildTicketDTO();
        ticketDTO.setCategory(categoryDTO);

        when(categoryService.getCategoryById(anyString())).thenReturn(categoryDTO);
        when(ticketService.changeCategoryOfTicket(anyLong(), any(CategoryDTO.class))).thenThrow(new NoSuchTicketException("test"));

        userDataBean.changeCateogry();

        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testChangeCategoryNOKNoSuchTicketException() throws CategoryNotFoundException, NoSuchTicketException {
        PowerMockito.mockStatic(FacesContextUtils.class);
        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());

        userDataBean.setActiveTicket(buildTicketDTO());
        userDataBean.setNewCategoryId("cid");
        assertNotNull(userDataBean.getNewCategoryId());

        CategoryDTO categoryDTO = buildCategoryDTO();
        categoryDTO.setCategoryId("newId");
        TicketDTO ticketDTO = buildTicketDTO();
        ticketDTO.setCategory(categoryDTO);

        when(categoryService.getCategoryById(anyString())).thenThrow(new CategoryNotFoundException("test"));

        userDataBean.changeCateogry();

        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testChangeNOKNoActiveTicket(){
        PowerMockito.mockStatic(FacesContextUtils.class);
        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());

        userDataBean.changeCateogry();

        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        RequestContext.releaseThreadLocalCache();
    }

    @Test
    public void testChangeNOKNoNewCategory(){
        PowerMockito.mockStatic(FacesContextUtils.class);
        RequestContext.setCurrentInstance(requestContext, facesContext);
        doNothing().when(requestContext).execute(anyString());
        userDataBean.setActiveTicket(buildTicketDTO());

        userDataBean.changeCateogry();

        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        FacesContextUtils.resolveError(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        RequestContext.releaseThreadLocalCache();
    }

    private List<HistoryDTO> buildHistoryDTOList() {

        List<HistoryDTO> historyDTOS = new ArrayList<>();
        for (long i = 0; i < 5; i++) {
            historyDTOS.add(new HistoryDTO(i,  buildTicketDTO(), LocalDateTime.now(), HistoryAction.CA, "detail", buildUserDTO()));
        }
        return historyDTOS;
    }

    private List<TicketDTO> buildDTOList(int size) {
        List<TicketDTO> tickets = new ArrayList<>(size);
        for(int i=0; i<size; i++){
            tickets.add(buildTicketDTO());
        }
        return tickets;
    }

    private TicketDTO buildTicketDTO() {
        return new TicketDTO(1L, "title", "desc",
                TicketStatus.NEW, buildUserDTO(), LocalDateTime.now(), buildCategoryDTO(), buildUserDTO(),
                null, null, null);
    }

    private CategoryDTO buildCategoryDTO() {
        return new CategoryDTO("cid", "name", buildUserDTO());
    }

    private UserDTO buildUserDTO() {
        return new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU);
    }
}
