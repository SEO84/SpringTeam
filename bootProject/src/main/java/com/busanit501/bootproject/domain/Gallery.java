package com.busanit501.bootproject.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Gallery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long galleryId; // 갤러리 ID

    @Column(nullable = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Long userId; // 사용자 ID (FK)

    @Column(length = 255, nullable = false)
    private String mediaUrl; // 미디어 URL

    @Column
    private String galleryText; // 게시글 텍스트

    public void changeGallery(String mediaUrl, String galleryText) {
        this.mediaUrl = mediaUrl;
        this.galleryText = galleryText;
    }

}
