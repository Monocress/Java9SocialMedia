package com.dorukt.utility;

import com.dorukt.dto.response.UserProfileResponseDto;
import com.dorukt.manager.IUserManager;
import com.dorukt.mapper.IUserMapper;
import com.dorukt.repository.entity.UserProfile;
import com.dorukt.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllData {

    private final UserProfileService userProfileService;
    private final IUserManager userManager;

//    @PostConstruct
//    public void initData(){
//        List<UserProfileResponseDto> list = userManager.findAllforElasticService().getBody();
//        userProfileService.saveAll(IUserMapper.INSTANCE.toUserProfiles(list));
//    }


}
