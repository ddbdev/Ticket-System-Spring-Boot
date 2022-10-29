package it.ddbdev.ticketsystem.service;

import it.ddbdev.ticketsystem.entity.Comment;
import it.ddbdev.ticketsystem.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void addComment(Comment comment){
        commentRepository.save(comment);
    }
}
