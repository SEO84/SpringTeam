package com.busanit501.bootproject.service;

import com.busanit501.bootproject.domain.Gallery;
import com.busanit501.bootproject.dto.GalleryDTO;
import com.busanit501.bootproject.repository.GalleryRepository;
import groovy.util.logging.Log4j2;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class GalleryServiceImpl implements GalleryService {

    private final GalleryRepository galleryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long register(GalleryDTO galleryDTO) {
        Gallery gallery = modelMapper.map(galleryDTO, Gallery.class);
        Long user_id = galleryRepository.save(gallery).getGalleryId();
        return user_id;
    }

    @Override
    public GalleryDTO readOne(Long GalleryId) {
        Optional<Gallery> result = galleryRepository.findById(GalleryId);
        Gallery gallery = result.orElseThrow();
        GalleryDTO galleryDTO = modelMapper.map(gallery, GalleryDTO.class);
        return galleryDTO;
    }

    @Override
    public void update(GalleryDTO galleryDTO) {
        Optional<Gallery> result = galleryRepository.findById(galleryDTO.getGalleryId());
        Gallery gallery = result.orElseThrow();
        gallery.changeGallery(
                gallery.getMediaUrl(), gallery.getGalleryText());

        galleryRepository.save(gallery);
    }

    @Override
    public void delete(Long GalleryId) {
        galleryRepository.deleteById(GalleryId);
    }

    @Override
    public List<GalleryDTO> getAll(GalleryDTO galleryDTO) {
        List<Gallery> galleryList = galleryRepository.findAll();

        List<GalleryDTO> gallerys = galleryList.stream()
                .map(gallery -> modelMapper.map(gallery, GalleryDTO.class))
                .toList();

        return gallerys;
    }
//    public List<UsersDTO> readAll(UsersDTO usersDTO) {
//        List<Users> usersList = usersRepository.findAll();
//
//        List<UsersDTO> usersDTOList = usersList.stream()
//                .map(users -> modelMapper.map(users, UsersDTO.class)) // ModelMapper 사용
//                .toList();
//
//        return usersDTOList;
//    }

}
