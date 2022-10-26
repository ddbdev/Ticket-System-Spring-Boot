package it.ddbdev.ticketsystem.repository;

import it.ddbdev.ticketsystem.entity.Avatar;
import it.ddbdev.ticketsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<Avatar, User> {

    void deleteById(Long id);
}
