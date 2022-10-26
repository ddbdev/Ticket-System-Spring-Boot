package it.ddbdev.ticketsystem.service;

import it.ddbdev.ticketsystem.entity.Avatar;
import it.ddbdev.ticketsystem.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AvatarService {

    @Autowired
    private AvatarRepository avatarRepository;

    public Avatar createAvatarFromMultipartFile(MultipartFile file) throws IOException {

        Avatar avatar = new Avatar();
        avatar.setFilename(file.getOriginalFilename());
        avatar.setData(file.getBytes());
        avatar.setFiletype(file.getContentType());

        return avatar;

    }

    public void addAvatar(Avatar avatar){
        avatarRepository.save(avatar);
    }

    public void deleteAvatar(Long id){
        avatarRepository.deleteById(id);
    }
}
