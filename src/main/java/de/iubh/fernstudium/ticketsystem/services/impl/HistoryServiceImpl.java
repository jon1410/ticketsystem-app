package de.iubh.fernstudium.ticketsystem.services.impl;

import de.iubh.fernstudium.ticketsystem.db.entities.HistoryEntity;
import de.iubh.fernstudium.ticketsystem.db.services.HistoryDBService;
import de.iubh.fernstudium.ticketsystem.dtos.HistoryDTO;
import de.iubh.fernstudium.ticketsystem.services.HistoryService;
import org.apache.commons.collections.CollectionUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class HistoryServiceImpl implements HistoryService {

    @Inject
    private HistoryDBService historyDBService;

    @Override
    public List<HistoryDTO> getHistoryForTicket(Long ticketId) {
        List<HistoryEntity> historyEntities = historyDBService.getHistoryForTicketId(ticketId);
        if(CollectionUtils.isNotEmpty(historyEntities)){
            return historyEntities.stream().map(HistoryEntity::toDto).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
