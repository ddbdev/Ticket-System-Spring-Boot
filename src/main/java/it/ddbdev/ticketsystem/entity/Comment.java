package it.ddbdev.ticketsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false, updatable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "author", nullable = false, updatable = false)
    private User author;

    @Column(nullable = false, updatable = false)
    private String content;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
}
