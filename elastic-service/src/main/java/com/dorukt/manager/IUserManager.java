package com.dorukt.manager;

import com.dorukt.dto.response.UserProfileResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.dorukt.constant.EndPoints.FINDALL;

@FeignClient(url = "http://localhost:7072/api/v1/user", decode404 = true, name = "elastic-userprofile")
public interface IUserManager {


    @GetMapping(FINDALL+"forelastic")
    ResponseEntity<List<UserProfileResponseDto>> findAllforElasticService();
}
