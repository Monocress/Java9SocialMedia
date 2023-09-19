package com.dorukt.dto.reponse;

import com.dorukt.repository.enums.ERole;
import com.dorukt.repository.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginResponseDto {
    private Long id;
    private String username;
    private String email;
    private ERole role;
    private EStatus status;
    private Long createDate;
}
