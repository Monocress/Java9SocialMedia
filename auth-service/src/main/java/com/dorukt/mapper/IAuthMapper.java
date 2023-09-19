package com.dorukt.mapper;

import com.dorukt.dto.reponse.LoginResponseDto;
import com.dorukt.dto.reponse.RegisterResponseDto;
import com.dorukt.dto.request.RegisterRequestDto;
import com.dorukt.dto.request.UserSaveRequestDto;
import com.dorukt.rabbitmq.model.ActivateModel;
import com.dorukt.rabbitmq.model.MailModel;
import com.dorukt.rabbitmq.model.RegisterModel;
import com.dorukt.repository.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthMapper {
    IAuthMapper INSTANCE = Mappers.getMapper(IAuthMapper.class);
    Auth toAuth(final RegisterRequestDto dto);


    LoginResponseDto toLoginResponseDto(final Auth auth);

    @Mapping(source = "id",target="authId")
    UserSaveRequestDto toUserSaveRequestDto(final Auth auth);

    RegisterResponseDto toRegisterResponseDto (final Auth auth);

    @Mapping(source = "id",target = "authId")
    RegisterModel toRegisterModel(final Auth auth);

    @Mapping(source = "id",target = "authId")
    ActivateModel toActivateModel(final Auth auth);

    MailModel toMailModel(Auth auth);

}
