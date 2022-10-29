package it.ddbdev.ticketsystem.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.ddbdev.ticketsystem.entity.Comment;
import it.ddbdev.ticketsystem.entity.Ticket;
import it.ddbdev.ticketsystem.entity.TicketStatus;
import it.ddbdev.ticketsystem.entity.User;
import it.ddbdev.ticketsystem.payload.request.CommentRequest;
import it.ddbdev.ticketsystem.security.UserPrincipal;
import it.ddbdev.ticketsystem.service.AuthorityService;
import it.ddbdev.ticketsystem.service.CommentService;
import it.ddbdev.ticketsystem.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/comment")
@SecurityRequirement(name = "ddbdev_ticketsystem")
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final TicketService ticketService;

    @Autowired
    public CommentController(CommentService commentService, TicketService ticketService) {
        this.commentService = commentService;
        this.ticketService = ticketService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> addComment(
            @CurrentSecurityContext(expression = "authentication") Authentication authentication,
            @RequestBody CommentRequest request
    ){

        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        Optional<Ticket> foundTicket = ticketService.findTicketById(request.getTicketId());

        if (!foundTicket.isPresent())
            return new ResponseEntity<>("No ticket found with this id", HttpStatus.OK);


        if (foundTicket.get().getStatus().equals(TicketStatus.CLOSED) || foundTicket.get().getStatus().equals(TicketStatus.RESOLVED))
            return new ResponseEntity<>("You can't reply to a ticket already closed", HttpStatus.FORBIDDEN);


        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_USER"))) {
            if (!foundTicket.get().getUser().getId().equals(user.getId()))
                return new ResponseEntity<>("You're not the author of this ticket!", HttpStatus.FORBIDDEN);
        }
        else {
            if (foundTicket.get().getAssignedTo() == null){
                foundTicket.get().setAssignedTo(new User(user.getId()));
                foundTicket.get().setStatus(TicketStatus.ON_HOLD);
            }
            if (!foundTicket.get().getAssignedTo().getId().equals(user.getId()))
                return new ResponseEntity<>("This ticked is already assigned to someone else.", HttpStatus.FORBIDDEN);
        }

        Comment comment = new Comment();
        comment.setAuthor(new User(user.getId()));
        comment.setTicket(foundTicket.get());
        comment.setContent(request.getContent());
        comment.setCreatedAt(LocalDateTime.now());

        commentService.addComment(comment);

        return new ResponseEntity<>("Comment added to ticket with UUID " + foundTicket.get().getUuid(),HttpStatus.OK);

    }

}
