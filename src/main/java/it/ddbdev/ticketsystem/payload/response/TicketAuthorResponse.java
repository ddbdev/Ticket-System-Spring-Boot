package it.ddbdev.ticketsystem.payload.response;

import it.ddbdev.ticketsystem.entity.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketAuthorResponse {

    private String UUID;
    private String category;
    private String title;
    private TicketStatus status;
    private LocalDateTime updatedAt;

}
