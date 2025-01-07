package com.busanit501.bootproject.dto;

import com.busanit501.bootproject.enums.Gender;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PetDTO {

    @NotBlank(message = "반려동물 이름은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "반려동물 종류는 필수 입력 항목입니다.")
    private String type;

    @NotNull(message = "반려동물 나이는 필수 입력 항목입니다.")
    @Min(value = 0, message = "반려동물 나이는 0 이상이어야 합니다.")
    private Long age;

    @NotNull(message = "반려동물 성별은 필수 입력 항목입니다.")
    private Gender gender;

    @NotNull(message = "반려동물 무게는 필수 입력 항목입니다.")
    @DecimalMin(value = "0.0", inclusive = true, message = "반려동물 무게는 0 이상이어야 합니다.")
    private Double weight; // Double 타입으로 변경

    @NotBlank(message = "반려동물 성격은 필수 입력 항목입니다.")
    private String personality;
}
