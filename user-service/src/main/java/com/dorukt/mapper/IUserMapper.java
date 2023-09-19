package com.dorukt.mapper;

import com.dorukt.dto.reponse.UserProfileResponseDto;
import com.dorukt.dto.request.UpdateRequestDto;
import com.dorukt.dto.request.UserSaveRequestDto;
import com.dorukt.dto.request.UserProfileUpdateRequestDto;
import com.dorukt.rabbitmq.model.ActivateModel;
import com.dorukt.rabbitmq.model.RegisterElasticModel;
import com.dorukt.rabbitmq.model.RegisterModel;
import com.dorukt.repository.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper {
    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);

    UserProfile toUserProfile(final UserSaveRequestDto dto);

    UserProfile toUserProfile(final UserProfileUpdateRequestDto dto);
    UserProfile toUserProfile(RegisterModel model);

    UserProfile toUserProfile(ActivateModel model);

    @Mapping(target ="id",source = "authId")
    UpdateRequestDto toUpdateRequestDto(final UserProfile userProfile);


    UserProfileResponseDto toUserProfileResponseDto(UserProfile userProfile);


    RegisterElasticModel toRegisterElasticModel(UserProfile userProfile);
}
