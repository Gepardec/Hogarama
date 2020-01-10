package com.gepardec.hogarama.rest.v2;

import com.gepardec.hogarama.domain.entity.Actor;
import com.gepardec.hogarama.rest.interceptor.DetermineOwner;
import com.gepardec.hogarama.rest.translator.ActorDtoTranslator;
import com.gepardec.hogarama.rest.v2.dto.ActorDto;
import com.gepardec.hogarama.service.ActorService;
import org.apache.http.HttpStatus;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@DetermineOwner
public class ActorApiImpl implements ActorApi {

    @Inject
    private ActorService service;
    @Inject
    private ActorDtoTranslator translator;

    @Override
    @Transactional
    public Response createActor(SecurityContext securityContext, ActorDto actorDto) {
        Actor actor = translator.fromDto(actorDto);
        if (actorDto.getUnitId() == null) {
            service.createActorForDefaultUnit(actor);
        } else {
            service.createActor(actor);
        }

        return new BaseResponse<>(HttpStatus.SC_CREATED).createRestResponse();
    }

    @Override
    public Response getActorsForOwner(SecurityContext securityContext) {
        List<ActorDto> dtoList = translator.toDtoList(service.getActorsForOwner());
        return new BaseResponse<>(dtoList, HttpStatus.SC_OK).createRestResponse();
    }
}
