package it.ddbdev.ticketsystem.repository;

import it.ddbdev.ticketsystem.entity.User;
import it.ddbdev.ticketsystem.payload.response.GetMeResponse;
import it.ddbdev.ticketsystem.payload.response.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);

    Optional<User> findUserByConfirm(String confirm);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    List<User> findByIdIn(List<Long> userIds);

    @Query("select new it.ddbdev.ticketsystem.payload.response.UserResponse(u.id, u.username) from User u where u.id = :id")
    UserResponse getUserResponse(@Param("id") Long id);

    @Query("select new it.ddbdev.ticketsystem.payload.response.GetMeResponse(u.id, u.username, u.email, u.bio, a,0L, u.createdAt) from User u left join Avatar a on a.id = u.avatar.id WHERE u.id = :id")
    GetMeResponse getMe(@Param("id") Long id);
}
