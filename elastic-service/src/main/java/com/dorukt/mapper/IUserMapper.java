package com.dorukt.mapper;


import com.dorukt.dto.response.UserProfileResponseDto;
import com.dorukt.graphql.model.UserProfileInput;
import com.dorukt.rabbitmq.model.RegisterElasticModel;
import com.dorukt.repository.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper {
    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);

    List<UserProfile> toUserProfiles(List<UserProfileResponseDto> userProfileResponseDtos);

    UserProfile toUserProfile(RegisterElasticModel elasticModel);
    UserProfile toUserProfile(UserProfileInput userProfileInput);



}
