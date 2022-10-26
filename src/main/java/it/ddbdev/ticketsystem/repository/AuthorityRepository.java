package it.ddbdev.ticketsystem.repository;

import it.ddbdev.ticketsystem.entity.Authority;
import it.ddbdev.ticketsystem.payload.response.AuthorityUserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Optional<Authority> findByAuthorityName(String authorityName);
    Optional<Authority> findByDefaultAuthorityTrue();
    boolean existsByAuthorityName(String authorityName);
    List<Authority> findByVisibleTrue();
    Set<Authority> findByIdIn(Set<Long> autorityIds);

    @Query("select new it.ddbdev.ticketsystem.payload.response.AuthorityUserResponse(a.id, a.authorityName, case when au.id IS NULL then false else TRUE END) from Authority a left join a.users au on au.id = ?1 where a.visible = true order by a.authorityName ")
    List<AuthorityUserResponse> getAuthoritiesByUser(Long id);
}
