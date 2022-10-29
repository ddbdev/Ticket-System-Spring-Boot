package it.ddbdev.ticketsystem.payload.request;

import lombok.Getter;

@Getter
public class CommentRequest {

    private Long ticketId;
    private String content;
}
