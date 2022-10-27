package it.ddbdev.ticketsystem.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.ddbdev.ticketsystem.entity.Category;
import it.ddbdev.ticketsystem.entity.Ticket;
import it.ddbdev.ticketsystem.entity.TicketStatus;
import it.ddbdev.ticketsystem.entity.User;
import it.ddbdev.ticketsystem.payload.request.TicketRequest;
import it.ddbdev.ticketsystem.payload.response.TicketResponse;
import it.ddbdev.ticketsystem.service.CategoryService;
import it.ddbdev.ticketsystem.service.TicketService;
import it.ddbdev.ticketsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/ticket")
@SecurityRequirement(name = "ddbdev_ticketsystem")
@Slf4j
@Validated
public class TicketController {

    private final TicketService ticketService;
    private final CategoryService categoryService;

    @Autowired
    public TicketController(TicketService ticketService, CategoryService categoryService) {
        this.ticketService = ticketService;
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> sendTicket(

            @CurrentSecurityContext(expression = "authentication?.principal.id") Long currentUserId,
            @Valid @RequestBody TicketRequest request
    ){

        Optional<Category> foundCategory =  categoryService.getCategoryByName(request.getCategory());

        if (!foundCategory.isPresent())
            return new ResponseEntity<>("Category not found", HttpStatus.FORBIDDEN);


        Ticket ticket = new Ticket(new User(currentUserId), foundCategory.get(), request.getTitle(), request.getContent(), RandomString.make(10).toUpperCase());

        ticketService.saveTicket(ticket);

        return new ResponseEntity<>("Ticket has been opened with id " + ticket.getId(), HttpStatus.OK);

    }

    @PutMapping("/{UUID}")
    public ResponseEntity<?> updateTicket(
            @PathVariable String UUID,
            @CurrentSecurityContext(expression = "authentication?.principal.id") Long currentUserId
    ){
        Optional<Ticket> foundTicket = ticketService.getTicketByUUID(UUID);

        if (!foundTicket.isPresent())
            return new ResponseEntity<>("No ticket found with this " + UUID, HttpStatus.FORBIDDEN);

        if (!foundTicket.get().getUser().getId().equals(currentUserId))
            return new ResponseEntity<>("You're not the author of this ticket, you can't perform this action", HttpStatus.FORBIDDEN);

        if (foundTicket.get().getStatus().equals(TicketStatus.CLOSED) || foundTicket.get().getStatus().equals(TicketStatus.RESOLVED))
            return new ResponseEntity<>("The ticket is already closed.", HttpStatus.OK);
        ticketService.closeTicketByUUID(TicketStatus.CLOSED, UUID);

        return new ResponseEntity<>("Ticket closed by the author.", HttpStatus.OK);

    }

    @GetMapping("public/{category}/{status}")
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_MODERATOR')")
    public ResponseEntity<?> getTicketList(@PathVariable("status") String status, @PathVariable String category){

        Optional<Category> foundCategory = categoryService.getCategoryByName(category);

        if(!EnumUtils.isValidEnum(TicketStatus.class, status))
            return new ResponseEntity<>("Not a valid status", HttpStatus.FORBIDDEN);

        if (!foundCategory.isPresent())
            return new ResponseEntity<>("No category found for " + category + ".", HttpStatus.FORBIDDEN);

        List<TicketResponse> ticketList;

        if (TicketStatus.valueOf(status).equals(TicketStatus.ALL))
            ticketList = ticketService.getAllTickets(foundCategory.get());
        else
            ticketList = ticketService.findTicketByStatus(TicketStatus.valueOf(status), foundCategory.get());

        if (ticketList.isEmpty())
            return new ResponseEntity<>("No ticket found with " + status + " status." , HttpStatus.OK);

        return new ResponseEntity<>(ticketList, HttpStatus.OK);
    }


    @GetMapping("/my-tickets")
    //@PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getMyTickets(
            @CurrentSecurityContext(expression = "authentication?.principal.id") Long currentUserId
    ){
        List<TicketResponse> ticketList = ticketService.getTicketsByUserId(currentUserId);

        if (ticketList.isEmpty())
            return new ResponseEntity<>("No ticket found for this user", HttpStatus.OK);

        return new ResponseEntity<>(ticketList, HttpStatus.OK);
    }
}
