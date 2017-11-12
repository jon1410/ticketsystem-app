package de.iubh.fernstudium.ticketsystem.db.util.test;

import de.iubh.fernstudium.ticketsystem.db.utils.CustomNativeQuery;
import de.iubh.fernstudium.ticketsystem.domain.TicketStatus;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.exception.UserNotExistsException;
import de.iubh.fernstudium.ticketsystem.dtos.TicketDTO;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.UserService;
import de.iubh.fernstudium.ticketsystem.util.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Future;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class QueryBuilderTest {

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @Inject
    UserService userService;

    @Test
    public void testQueryBuilder() throws UserNotExistsException {

        String dateFrom = "2017-01-01";
        String dateTo = "2017-10-01";
        String userIdReporter = "admin";
        String userIdAssignee = "ivan";
        Map<String, TicketStatus> statusValues;
        statusValues = new LinkedHashMap<>();
        statusValues.put("1", TicketStatus.NEW);
        statusValues.put("2", TicketStatus.CLO);

        Mockito.when(userService.getUserByUserId(Mockito.anyString()))
                .thenReturn(new UserDTO("ivan@ticketsystem.de", "ivan", "ivan", "abc", UserRole.AD));

        Set<TicketDTO> ticketSet = new HashSet<>();
        Future<List<TicketDTO>> tickets;

        LocalDateTime ldtFrom = DateTimeUtil.format(dateFrom);
        LocalDateTime ldtTo = DateTimeUtil.format(dateTo);
        CustomNativeQuery.QueryBuilder queryBuilder = CustomNativeQuery.builder()
                .selectAll().from("ticket").where("CREA_TSP")
                .between(DateTimeUtil.localDtToSqlTimestamp(ldtFrom), DateTimeUtil.localDtToSqlTimestamp(ldtTo));


        if(StringUtils.isNotEmpty(userIdReporter)){
            try {
                UserDTO user = userService.getUserByUserId(userIdReporter);
                queryBuilder = queryBuilder.and("reporter_USERID").isEqualTo(user.getUserId());
            } catch (UserNotExistsException e) {
                queryBuilder = queryBuilder.and("reporter_USERID").like(userIdReporter);
            }
        }

        if(StringUtils.isNotEmpty(userIdAssignee)){
            try {
                UserDTO user = userService.getUserByUserId(userIdAssignee);
                queryBuilder = queryBuilder.and("assignee_USERID").isEqualTo(user.getUserId());
            } catch (UserNotExistsException e) {
                queryBuilder = queryBuilder.and("assignee_USERID").like(userIdAssignee);
            }
        }

        if(statusValues != null && !statusValues.isEmpty()){
            queryBuilder = queryBuilder.and("STATUS").in(statusValues.values().stream().toArray());
        }
        CustomNativeQuery customNativeQuery = queryBuilder.buildQuery();
        assertNotNull(customNativeQuery);
        System.out.println(customNativeQuery.getQueryString());
        System.out.println(customNativeQuery.getParameters().toString());
    }

    @Test
    public void testQueryBuilderWithLike() throws UserNotExistsException {

        String dateFrom = "2017-01-01";
        String dateTo = "2017-10-01";
        String userIdReporter = "admin";
        String userIdAssignee = "ivan";
        Map<String, TicketStatus> statusValues;
        statusValues = new LinkedHashMap<>();
        statusValues.put("1", TicketStatus.NEW);
        statusValues.put("2", TicketStatus.CLO);

        Mockito.when(userService.getUserByUserId(Mockito.anyString())).thenThrow(UserNotExistsException.class);


        Set<TicketDTO> ticketSet = new HashSet<>();
        Future<List<TicketDTO>> tickets;

        LocalDateTime ldtFrom = DateTimeUtil.format(dateFrom);
        LocalDateTime ldtTo = DateTimeUtil.format(dateTo);
        CustomNativeQuery.QueryBuilder queryBuilder = CustomNativeQuery.builder()
                .selectAll().from("ticket").where("CREA_TSP")
                .between(DateTimeUtil.localDtToSqlTimestamp(ldtFrom), DateTimeUtil.localDtToSqlTimestamp(ldtTo));


        if(StringUtils.isNotEmpty(userIdReporter)){
            try {
                UserDTO user = userService.getUserByUserId(userIdReporter);
                queryBuilder = queryBuilder.and("reporter_USERID").isEqualTo(user.getUserId());
            } catch (UserNotExistsException e) {
                queryBuilder = queryBuilder.and("reporter_USERID").like(userIdReporter);
            }
        }

        if(StringUtils.isNotEmpty(userIdAssignee)){
            try {
                UserDTO user = userService.getUserByUserId(userIdAssignee);
                queryBuilder = queryBuilder.and("assignee_USERID").isEqualTo(user.getUserId());
            } catch (UserNotExistsException e) {
                queryBuilder = queryBuilder.and("assignee_USERID").like(userIdAssignee);
            }
        }

        if(statusValues != null && !statusValues.isEmpty()){
            queryBuilder = queryBuilder.and("STATUS").in(statusValues.values().stream().toArray());
        }
        CustomNativeQuery customNativeQuery = queryBuilder.buildQuery();
        assertNotNull(customNativeQuery);
        System.out.println(customNativeQuery.getQueryString());
        System.out.println(customNativeQuery.getParameters().toString());
    }

    @Test
    public void testQueryBuilder1() throws UserNotExistsException {

        String dateFrom = "2017-01-01";
        String dateTo = "2017-10-01";
        String userIdReporter = null;
        String userIdAssignee = "ivan";
        Map<String, TicketStatus> statusValues;
        statusValues = new LinkedHashMap<>();
        statusValues.put("1", TicketStatus.NEW);
        statusValues.put("2", TicketStatus.CLO);

        Mockito.when(userService.getUserByUserId(Mockito.anyString())).thenThrow(UserNotExistsException.class);


        Set<TicketDTO> ticketSet = new HashSet<>();
        Future<List<TicketDTO>> tickets;

        LocalDateTime ldtFrom = DateTimeUtil.format(dateFrom);
        LocalDateTime ldtTo = DateTimeUtil.format(dateTo);
        CustomNativeQuery.QueryBuilder queryBuilder = CustomNativeQuery.builder()
                .selectAll().from("ticket").where("CREA_TSP")
                .between(DateTimeUtil.localDtToSqlTimestamp(ldtFrom), DateTimeUtil.localDtToSqlTimestamp(ldtTo));


        if(StringUtils.isNotEmpty(userIdReporter)){
            try {
                UserDTO user = userService.getUserByUserId(userIdReporter);
                queryBuilder = queryBuilder.and("reporter_USERID").isEqualTo(user.getUserId());
            } catch (UserNotExistsException e) {
                queryBuilder = queryBuilder.and("reporter_USERID").like(userIdReporter);
            }
        }

        if(StringUtils.isNotEmpty(userIdAssignee)){
            try {
                UserDTO user = userService.getUserByUserId(userIdAssignee);
                queryBuilder = queryBuilder.and("assignee_USERID").isEqualTo(user.getUserId());
            } catch (UserNotExistsException e) {
                queryBuilder = queryBuilder.and("assignee_USERID").like(userIdAssignee);
            }
        }

        if(statusValues != null && !statusValues.isEmpty()){
            queryBuilder = queryBuilder.and("STATUS").in(statusValues.values().stream().toArray());
        }
        CustomNativeQuery customNativeQuery = queryBuilder.buildQuery();
        assertNotNull(customNativeQuery);
        System.out.println(customNativeQuery.getQueryString());
        System.out.println(customNativeQuery.getParameters().toString());
    }
}
