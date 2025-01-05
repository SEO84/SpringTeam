package com.busanit501.bootproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GalleryDTO {

    private Long galleryId;
    private Long userId;
    private String mediaUrl;
    private String galleryText;
    private LocalDateTime createdAt; // 등록 시간
    private LocalDateTime updatedAt; // 수정 시간

}
