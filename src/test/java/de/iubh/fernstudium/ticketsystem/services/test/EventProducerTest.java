package de.iubh.fernstudium.ticketsystem.services.test;

import de.iubh.fernstudium.ticketsystem.beans.CurrentUserBean;
import de.iubh.fernstudium.ticketsystem.domain.UserRole;
import de.iubh.fernstudium.ticketsystem.domain.event.payload.CachePayload;
import de.iubh.fernstudium.ticketsystem.domain.event.payload.HistoryPayload;
import de.iubh.fernstudium.ticketsystem.domain.history.HistoryAction;
import de.iubh.fernstudium.ticketsystem.dtos.UserDTO;
import de.iubh.fernstudium.ticketsystem.services.impl.EventProducer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventProducerTest {

    @Rule
    public NeedleRule needleRule = NeedleBuilders.needleMockitoRule().build();

    @ObjectUnderTest
    EventProducer eventProducer;

    @Inject
    private Event<HistoryPayload> historyEvent;
    @Inject
    private Event<CachePayload> cachePayloadEvent;
    @Inject
    private CurrentUserBean currentUserBean;

    @Test
    public void testProduceHistoryEventWithPayload(){
        HistoryPayload historyPayload = new HistoryPayload(1L, HistoryAction.AC, buildUserDTO(), "Detail");
        assertNotNull(historyPayload.getUserId());
        eventProducer.produceHistoryEvent(historyPayload);
        Mockito.verify(historyEvent, times(1)).fire(any(HistoryPayload.class));
    }

    @Test
    public void testProduceHistoryEvent(){
        when(currentUserBean.createUserDto()).thenReturn(buildUserDTO());
        eventProducer.produceHistoryEvent(1L, HistoryAction.AC, "Detail");
        Mockito.verify(historyEvent, times(1)).fire(any(HistoryPayload.class));
    }

    @Test
    public void testProduceCacheEvent(){
        CachePayload cachePayload = new CachePayload(buildUserDTO());
        eventProducer.produceCacheEvent(cachePayload);
        Mockito.verify(cachePayloadEvent, times(1)).fire(any(CachePayload.class));
    }

    private UserDTO buildUserDTO() {
        return new UserDTO("userid", "firstName", "lastName", "pw", UserRole.TU);
    }

}
