package it.ddbdev.ticketsystem.service;

import it.ddbdev.ticketsystem.entity.Category;
import it.ddbdev.ticketsystem.entity.Ticket;
import it.ddbdev.ticketsystem.entity.TicketStatus;
import it.ddbdev.ticketsystem.payload.response.TicketResponse;
import it.ddbdev.ticketsystem.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void saveTicket(Ticket ticket){
        ticketRepository.save(ticket);
    }

    public Optional<Ticket> findTicketById(Long id){
        return ticketRepository.findById(id);
    }

    public List<Ticket> findTicketByCategory(Category category){
        return ticketRepository.findByCategory(category);
    }

    public List<TicketResponse> findTicketByStatus(TicketStatus status, Category category){
        return ticketRepository.findTicketByStatus(status,category);
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public List<TicketResponse> getAllTickets(Category category){
        return ticketRepository.getAllTickets(category);
    }

    public void closeTicketByUUID(TicketStatus status, String UUID){
        ticketRepository.closeTicket(status,UUID);
    }

    public Optional<Ticket> getTicketByUUID(String uuid){
        return ticketRepository.findTicketByUuid(uuid);
    }

    public List<TicketResponse> getTicketsByUserId(Long userId){
        return ticketRepository.getTicketByAuthorId(userId);
    }
}
