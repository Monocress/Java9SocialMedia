package com.dorukt.manager;

import com.dorukt.dto.request.UpdateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.dorukt.constant.EndPoints.UPDATE;

@FeignClient(url = "${feign.auth}", decode404 = true, name = "user-auth")
public interface IAuthManager {

    @PutMapping(UPDATE)
    ResponseEntity<String> updateAuthWithUserProfileInformation(@RequestBody UpdateRequestDto dto, @RequestHeader(value = "Authorization") String token);
}
