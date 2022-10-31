package it.ddbdev.ticketsystem.service;

import it.ddbdev.ticketsystem.payload.response.GetMeResponse;
import it.ddbdev.ticketsystem.payload.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.ddbdev.ticketsystem.entity.User;
import it.ddbdev.ticketsystem.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired UserRepository userRepository;

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public Optional<User> getUserByConfirmToken(String confirmToken){
        return userRepository.findUserByConfirm(confirmToken);
    }

    public UserResponse getUserResponse(Long id){
        return userRepository.getUserResponse(id);
    }

    public GetMeResponse getMe(Long id){
        GetMeResponse getMeResponse = userRepository.getMe(id);

        LocalDateTime ldt = getMeResponse.getCreatedAt();
        LocalDateTime ldtNow = LocalDateTime.now();
        Duration diff = Duration.between(ldt, ldtNow);

        getMeResponse.setDays(diff.toDays());

        return getMeResponse;
    }

    public Optional<User> getUserByIdAndRoleModerator(Long id){
        return userRepository.getUserByIdAndRoleModerator(id);
    }
}
