package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.domain.unitmanagement.entity.LowWaterWateringRule;
import com.gepardec.hogarama.domain.unitmanagement.service.RuleService;
import com.gepardec.hogarama.rest.unitmanagement.dto.RuleDto;
import com.gepardec.hogarama.rest.unitmanagement.interceptor.DetermineUser;
import com.gepardec.hogarama.rest.unitmanagement.translator.RuleDtoTranslator;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.List;

@SuppressWarnings("unused")
@Path("/unitmanagement/rule")
@DetermineUser
@Transactional
public class RuleApiImpl implements RuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(RuleApiImpl.class);

    @Inject
    private RuleService service;
    @Inject
    private RuleDtoTranslator translator;

    @Override
    public Response getForUser() {
        LOG.info("Get rules for current user.");
        List<RuleDto> dtoList = translator.toDtoList(service.getAllRulesForUser());
        return new BaseResponse<>(dtoList, HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    public Response create(RuleDto ruleDto) {
        LOG.info("Create rule.");
        LowWaterWateringRule rule = translator.fromDto(ruleDto);
        service.createRule(rule);

        return new BaseResponse<>(ruleDto, HttpStatus.SC_CREATED).createRestResponse();
    }

    @Override
    public Response update(String id, RuleDto ruleDto) {
        LOG.info("Updating rule with id {}.", id);
        LowWaterWateringRule rule = translator.fromDto(ruleDto);

        if (id == null) {
            return new BaseResponse<>("Required parameter ID is not set!", HttpStatus.SC_BAD_REQUEST).createRestResponse();
        } else if (!id.equals(ruleDto.getId().toString())) {
            return new BaseResponse<>(String.format("ID %s has to match with ID %s", id, ruleDto.getId().toString()), HttpStatus.SC_BAD_REQUEST).createRestResponse();
        } else {
            service.updateRule(rule);
        }

        return new BaseResponse<>(ruleDto, HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    public Response delete(String id) {
        LOG.info("Deleting rule with id {}.", id);
        if (id == null) {
            return new BaseResponse<>(HttpStatus.SC_BAD_REQUEST).createRestResponse();
        } else {
            Long idNum;
            try {
                idNum = Long.parseLong(id);
            } catch (NumberFormatException e) {
                return new BaseResponse<>(HttpStatus.SC_BAD_REQUEST).createRestResponse();
            }

            service.deleteRule(idNum);
        }

        return new BaseResponse<>(HttpStatus.SC_OK).createRestResponse();
    }
}
