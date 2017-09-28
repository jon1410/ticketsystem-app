package de.iubh.fernstudium.ticketsystem.services.mockups;

import de.iubh.fernstudium.ticketsystem.domain.*;
import de.iubh.fernstudium.ticketsystem.domain.exception.NoSuchTicketException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.CommentDTO;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ivanj on 07.07.2017.
 *
 * Dieser Mock ist ApplicationScoped,
 * damit die PostConstructMethode nicht ständig aufgerufen wird (simuliert einen Datenbestand).
 * Das tatsächliche Bean wird dann RequestScope.
 */
@ApplicationScoped
@Alternative
public class TicketServiceMockup implements TicketService {

    private static final Logger LOG = LogManager.getLogger(TicketServiceMockup.class);

    @Inject
    private UserService userServiceMockup;

    private Map<Long, TicketDTO> defaultDTOs;
	
	

    @PostConstruct
    public void initDefaultDTOList() {

        if (defaultDTOs == null) {
            defaultDTOs = createDefaultList();
        }
    }

    @Override
    public TicketDTO getTicketByID(Long ticketId) throws NoSuchTicketException {
        TicketDTO dto = defaultDTOs.get(ticketId);
        if(dto == null){
            throw new NoSuchTicketException("Could not find Ticket with ID: " + ticketId);
        }
        return dto;
    }

    @Override
    public List<TicketDTO> getOpenTicketsForUserId(String userId) {
        return defaultDTOs.values().stream().filter(t -> t.getAssignee().equals(userId))
                .filter(t -> {
                    TicketStatus ts = t.getTicketStatus();
                    if(ts == TicketStatus.NEW || ts == TicketStatus.IPS || ts == TicketStatus.IPU || ts == TicketStatus.RES){
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> getHistoricTicketsByUserId(String userId) {
        return defaultDTOs.values().stream().filter(t -> t.getAssignee().equals(userId))
                .filter(t -> {
                    TicketStatus ts = t.getTicketStatus();
                    if(ts == TicketStatus.CLO || ts == TicketStatus.DEL || ts == TicketStatus.DLS){
                        return true;
                    }
                    return false;
                } ).collect(Collectors.toList());
    }

    @Override
    public void changeStatus(Long ticketId, TicketStatus newStatus) {

        TicketDTO tDto = defaultDTOs.get(ticketId);
        tDto.setTicketStatus(newStatus);
        defaultDTOs.put(ticketId, tDto);
    }

    @Override
    public void addComment(long ticketId, String comment, String userId) throws NoSuchTicketException, UserNotExistsException{

        TicketDTO tDto = this.getTicketByID(ticketId);
        List<CommentDTO> comments = tDto.getComments();
        if(comments == null) {
            comments = new ArrayList<>();
        }
        UserDTO userDTO = userServiceMockup.getUserByUserId(userId);
        CommentDTO commentDTO = new CommentDTO(userDTO, comment);
        comments.add(commentDTO);
        tDto.setComments(comments);
        defaultDTOs.put(ticketId, tDto);
    }

    @Override
    public TicketDTO createTicket(TicketDTO ticketDTO) {
        long nextVal = defaultDTOs.size() + 1;
        ticketDTO.setId(nextVal);
        defaultDTOs.put(nextVal, ticketDTO);
        return ticketDTO;
    }

    @Override
    public void createMasterTicket(Long masterTicketId, List<Long> childTickets) {

    }

    private Map<Long, TicketDTO> createDefaultList() {

        Map<Long, TicketDTO> defaultList = new HashMap<>();
        try {
            userServiceMockup.createUser(MockupConstatns.SYSDEF_USER,
                    "System",
                    "System",
                    "system",
                    UserRole.AD);

            defaultList.put(1L, new TicketDTO(1L, "Test-Ticket 1", "Erstes Test-Ticket",
                    userServiceMockup.getUserByUserId(MockupConstatns.SYSDEF_USER), LocalDateTime.now(), null,
                    userServiceMockup.getUserByUserId("admin"), null));
            defaultList.put(2L, new TicketDTO(2L, "Test-Ticket 2", "Zweites Test-Ticket",
                    userServiceMockup.getUserByUserId(MockupConstatns.SYSDEF_USER), LocalDateTime.now(), null,
                    userServiceMockup.getUserByUserId("admin"), null));
            defaultList.put(3L, new TicketDTO(3L, "Test-Ticket 3", "Drittes Test-Ticket",
                    userServiceMockup.getUserByUserId(MockupConstatns.SYSDEF_USER), LocalDateTime.now(), null,
                    userServiceMockup.getUserByUserId("admin"), null));
            defaultList.put(4L, new TicketDTO(4L, "Test-Ticket 4", "Viertes Test-Ticket",
                    userServiceMockup.getUserByUserId(MockupConstatns.SYSDEF_USER), LocalDateTime.now(), null,
                    userServiceMockup.getUserByUserId("admin"), null));
            defaultList.put(5L, new TicketDTO(5L, "Test-Ticket 5", "Fünftes Test-Ticket",
                    userServiceMockup.getUserByUserId(MockupConstatns.SYSDEF_USER), LocalDateTime.now(), null,
                    userServiceMockup.getUserByUserId("admin"), null));
        } catch (UserAlreadyExistsException | UserNotExistsException ex) {
            //TODO: Exception Handling
        }

        return defaultList;
    }

    public List<TicketDTO> getDefaultTestTickets(){
        return new ArrayList<>(defaultDTOs.values());
    }
}
