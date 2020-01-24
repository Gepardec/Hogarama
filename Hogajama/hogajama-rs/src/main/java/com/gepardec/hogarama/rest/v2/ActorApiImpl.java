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
    public Response getAllActors(SecurityContext securityContext) {
        List<ActorDto> actors = translator.toDtoList(service.getAllActors());
        return new BaseResponse<>(actors, HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    public Response getActorsForOwner(SecurityContext securityContext) {
        List<ActorDto> dtoList = translator.toDtoList(service.getActorsForOwner());
        return new BaseResponse<>(dtoList, HttpStatus.SC_OK).createRestResponse();
    }

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
    @Transactional
    public Response updateActor(String id, SecurityContext securityContext, ActorDto actorDto) {
        Actor sensor = translator.fromDto(actorDto);
        if (id == null || !id.equals(actorDto.getId().toString()) || actorDto.getUnitId() == null) {
            return new BaseResponse<>(HttpStatus.SC_BAD_REQUEST).createRestResponse();
        } else {
            service.updateActor(sensor);
        }

        return new BaseResponse<>(HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    @Transactional
    public Response deleteActor(String id, SecurityContext securityContext) {
        if (id == null) {
            return new BaseResponse<>(HttpStatus.SC_BAD_REQUEST).createRestResponse();
        } else {
            Long idNum;
            try {
                idNum = Long.parseLong(id);
            } catch (NumberFormatException e) {
                return new BaseResponse<>(HttpStatus.SC_BAD_REQUEST).createRestResponse();
            }

            service.deleteActor(idNum);
        }

        return new BaseResponse<>(HttpStatus.SC_OK).createRestResponse();
    }
}
