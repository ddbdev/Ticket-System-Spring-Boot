package it.ddbdev.ticketsystem.repository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.ddbdev.ticketsystem.entity.Category;
import it.ddbdev.ticketsystem.entity.Ticket;
import it.ddbdev.ticketsystem.entity.TicketStatus;
import it.ddbdev.ticketsystem.payload.response.TicketResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query(value = "select t from Ticket t where t.category = ?1")
    List<Ticket> findByCategory(Category category);

    @Query(value = "select new it.ddbdev.ticketsystem.payload.response.TicketResponse(t.uuid, t.category.id, t.user.username, t.title, t.status, t.createdAt, t.updatedAt) from Ticket t where t.status = ?1  and t.category = ?2 order by t.updatedAt desc")
    List<TicketResponse> findTicketByStatus(TicketStatus status, Category category);

    @Query(value = "select new it.ddbdev.ticketsystem.payload.response.TicketResponse(t.uuid, t.category.id, t.user.username, t.title, t.status, t.createdAt, t.updatedAt) from Ticket t where t.category = ?1")
    List<TicketResponse> getAllTickets(Category category);

    Optional<Ticket> findTicketByUuid(String uuid);

    @Modifying
    @Transactional
    @Query(value = "update Ticket t SET t.status = ?1 where t.uuid = ?2")
    void closeTicket(TicketStatus status, String uuid);

    @Query(value = "select new it.ddbdev.ticketsystem.payload.response.TicketResponse(t.uuid, t.category.id, t.user.username, t.title, t.status, t.createdAt, t.updatedAt) from Ticket t where t.user.id = ?1 order by t.status")
    List<TicketResponse> getTicketByAuthorId(Long userId);
}
