package com.busanit501.bootproject.service;

import com.busanit501.bootproject.dto.GalleryDTO;

import java.util.List;

public interface GalleryService {
    Long register(GalleryDTO galleryDTO);
    GalleryDTO readOne(Long id);
    void update(GalleryDTO galleryDTO);
    void delete(Long id);
    // PageResponseDTO<GalleryDTO> list(PageRequestDTO pageRequestDTO);
    List<GalleryDTO> getAll(GalleryDTO galleryDTO);
}
