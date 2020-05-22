package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.domain.unitmanagement.context.UnitManagementContext;
import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;
import com.gepardec.hogarama.domain.unitmanagement.service.UserProfileService;
import com.gepardec.hogarama.rest.unitmanagement.dto.UserDto;
import com.gepardec.hogarama.rest.unitmanagement.interceptor.DetermineOwner;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@DetermineOwner
public class UserApiImpl implements UserApi {

    private static final Logger LOG = LoggerFactory.getLogger(UserApiImpl.class);

    @Inject
    private UserProfileService userProfileService;

    @Override
    public Response getUser(SecurityContext securityContext) {
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
