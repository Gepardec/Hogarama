package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;
import com.gepardec.hogarama.domain.unitmanagement.service.UserProfileService;
import com.gepardec.hogarama.rest.unitmanagement.dto.UserDto;
import com.gepardec.hogarama.rest.unitmanagement.interceptor.DetermineUser;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/unitmanagement/user")
@DetermineUser
public class UserApiImpl implements UserApi {

    private static final Logger LOG = LoggerFactory.getLogger(UserApiImpl.class);

    @Inject
    private UserProfileService userProfileService;

    @Override
    public Response getUser() {
        LOG.info("Get user data. Currently dummy data.");
        // provide temporary dummy response for frontend
        UserDto userDto = new UserDto();
        UserProfile userProfile = userProfileService.getUserProfile();
        userDto.setName(userProfile.getName());
        userDto.setGivenName(userProfile.getGivenName());
        userDto.setFamilyName(userProfile.getFamilyName());
        userDto.setEmail(userProfile.getEmail());
        return new BaseResponse<>(userDto, HttpStatus.SC_OK).createRestResponse();
    }
}
