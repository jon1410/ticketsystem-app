package de.iubh.fernstudium.ticketsystem.services.mockups;

import de.iubh.fernstudium.ticketsystem.domain.UserAlreadyExistsException;
import de.iubh.fernstudium.ticketsystem.domain.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.services.TicketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ivanj on 07.07.2017.
 */
public class TicketServiceMockup implements TicketService {

    private static final Logger LOG = LogManager.getLogger(TicketServiceMockup.class);

    @Inject
    private UserServiceMockup userServiceMockup;

    private Map<Long, TicketDTO> defaultDTOs;

    @PostConstruct
    public void initDefaultDTOList() {

        if (defaultDTOs == null) {
            defaultDTOs = createDefaultList();
        }
    }

    @Override
    public List<TicketDTO> getOpenTicketsForUserId(String userId) {

        return defaultDTOs.values().stream().filter(t -> t.getAssignee().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> getHistoricTicketsByUserId(String userId) {
        return null;
    }

    @Override
    public boolean changeStatus(Long ticketId, String newStatus) {
        return false;
    }

    @Override
    public boolean addComment(long ticketId, String comment) {
        return false;
    }

    @Override
    public TicketDTO createTicket(TicketDTO ticketDTO) {
        return null;
    }

    private Map<Long, TicketDTO> createDefaultList() {

        Map<Long, TicketDTO> defaultList = new HashMap<>();
        try {
            userServiceMockup.createUser(MockupConstatns.SYSDEF_USER,
                    "System",
                    "System",
                    "system",
                    UserRole.AD,
                    "system@ticketsystem.de");

            defaultList.put(1L, new TicketDTO(1L, "Test-Ticket 1", "Erstes Test-Ticket",
                    userServiceMockup.getUserByUserId(MockupConstatns.SYSDEF_USER), Calendar.getInstance(), null,
                    userServiceMockup.getUserByUserId("admin"), null));
            defaultList.put(2L, new TicketDTO(2L, "Test-Ticket 2", "Zweites Test-Ticket",
                    userServiceMockup.getUserByUserId(MockupConstatns.SYSDEF_USER), Calendar.getInstance(), null,
                    userServiceMockup.getUserByUserId("admin"), null));
            defaultList.put(3L, new TicketDTO(3L, "Test-Ticket 3", "Drittes Test-Ticket",
                    userServiceMockup.getUserByUserId(MockupConstatns.SYSDEF_USER), Calendar.getInstance(), null,
                    userServiceMockup.getUserByUserId("admin"), null));
            defaultList.put(4L, new TicketDTO(4L, "Test-Ticket 4", "Viertes Test-Ticket",
                    userServiceMockup.getUserByUserId(MockupConstatns.SYSDEF_USER), Calendar.getInstance(), null,
                    userServiceMockup.getUserByUserId("admin"), null));
            defaultList.put(5L, new TicketDTO(5L, "Test-Ticket 5", "Fünftes Test-Ticket",
                    userServiceMockup.getUserByUserId(MockupConstatns.SYSDEF_USER), Calendar.getInstance(), null,
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
