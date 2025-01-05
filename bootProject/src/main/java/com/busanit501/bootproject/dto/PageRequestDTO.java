package com.busanit501.bootproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page=1;

    @Builder.Default
    private int size=10;

    private String type; // "t", "c", "w", "tc", "tcw"
    private String keyword;

    private String link;

    public String[] getTypes() {
        if (type == null || type.isEmpty()) {
            return null;
        }
        // type = "tcw", ->getTypes -> ={"t","c","w"}
        return type.split("");
    }

    // 검색시, 키워드 조건 이용해서 페이징 처리
    // ...props -> 가변 인자, 여러개의 매개변수를 받을수 있음.
    public Pageable getPageable(String ...props) {
        Pageable pageable = PageRequest.of(this.page-1,
                this.size,
                Sort.by(props).descending());
        return pageable;
    }

    public String getLink() {
        if (link == null || link.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);

            if (type != null && type.length() > 0) {
                    builder.append("&type="+ type);
            }

            if(keyword != null) {
                try {
                    builder.append("&keyword="+ URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
//                오류수정
//                link = builder.toString();
            } //if

            link = builder.toString();
        } //if
        return link;
    }

}
