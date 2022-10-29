package it.ddbdev.ticketsystem.repository;

import it.ddbdev.ticketsystem.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
