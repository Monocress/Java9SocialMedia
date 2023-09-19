package com.dorukt.manager;

import com.dorukt.dto.request.UserSaveRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.dorukt.constant.EndPoints.*;

@FeignClient(url = "${feign.user}", decode404 = true, name = "auth-userprofile")
public interface IUserManager {

    @PostMapping(SAVE)
    public ResponseEntity<Boolean> createNewUser(@RequestBody UserSaveRequestDto dto,@RequestHeader(value = "Authorization") String token);

    @PostMapping(ACTIVATION + "/{authId}")
    public ResponseEntity<String> activateUser(@PathVariable Long authId);

    @PostMapping(ACTIVATION)
    public ResponseEntity<String> activateUser2(@RequestParam Long authId);
}
