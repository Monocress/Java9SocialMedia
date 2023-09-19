package com.dorukt.graphql.mutation;

import com.dorukt.exception.ElasticManagerException;
import com.dorukt.exception.ErrorType;
import com.dorukt.graphql.model.UserProfileInput;
import com.dorukt.mapper.IUserMapper;
import com.dorukt.service.UserProfileService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileMutationResolver implements GraphQLMutationResolver {

    private  final UserProfileService userProfileService;

    public Boolean createUserProfile(UserProfileInput userProfileInput){

        try {
            userProfileService.save(IUserMapper.INSTANCE.toUserProfile(userProfileInput));
            return true;
        } catch (Exception e) {
            throw new ElasticManagerException(ErrorType.USER_NOT_CREATED);

        }
    }
}
