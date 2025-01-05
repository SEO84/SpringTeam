package com.busanit501.bootproject.galleryTests;

import com.busanit501.bootproject.domain.Gallery;
import com.busanit501.bootproject.dto.GalleryDTO;
import com.busanit501.bootproject.repository.GalleryRepository;
import com.busanit501.bootproject.service.GalleryService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
public class GalleryServiceTests {
    @Autowired
    private GalleryService galleryService;
    @Autowired
    private GalleryRepository galleryRepository;

    @Test
    public void insertGalleryTest() {
        GalleryDTO galleryDTO = GalleryDTO.builder()
                .userId(1L)
                .mediaUrl("insetGalleryImageUrl")
                .galleryText("테스트입니다.")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        galleryService.register(galleryDTO);
    }

    @Test
    public void insertGallerysTest (){
        IntStream.range(1,20).forEach(i -> {
            GalleryDTO galleryDTO = GalleryDTO.builder()
                    .userId(1L)
                    .mediaUrl("insetGalleryImageUrl")
                    .galleryText("테스트입니다."+i)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            galleryService.register(galleryDTO);
        });
    }

    @Test
    public void readAllTest () {
        List<Gallery> result = galleryRepository.findAll();

        for (Gallery gallery : result) {
            log.info(gallery);
        }
    }

    // 조회
    @Test
    public void readOneTest () {
        Long userId = 1L;
        Optional<Gallery> result = galleryRepository.findById(userId);
        Gallery gallery = result.orElseThrow();
        log.info(gallery);
    }

    // 수정
    @Test
    public void updateTest () {
        Long GalleryId = 1L;
        Optional<Gallery> result = galleryRepository.findById(GalleryId);
        Gallery gallery = result.orElseThrow();
        gallery.changeGallery("test", "수정테스트");

        galleryRepository.save(gallery);
    }

    // 삭제
    @Test
    public void deleteTest () {
        Long GalleryId = 3L;
        galleryRepository.deleteById(GalleryId);
    }


}
