package com.gepardec.hogarama.rest.unitmanagement;

import com.gepardec.hogarama.domain.unitmanagement.entity.LowWaterWateringRule;
import com.gepardec.hogarama.domain.unitmanagement.service.RuleService;
import com.gepardec.hogarama.rest.unitmanagement.dto.RuleDto;
import com.gepardec.hogarama.rest.unitmanagement.interceptor.DetermineOwner;
import com.gepardec.hogarama.rest.unitmanagement.translator.RuleDtoTranslator;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@SuppressWarnings("unused")
@DetermineOwner
public class RuleApiImpl implements RuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(RuleApiImpl.class);

    @Inject
    private RuleService service;
    @Inject
    private RuleDtoTranslator translator;

    @Override
    public Response getForOwner(SecurityContext securityContext) {
        LOG.info("Get rules for current owner.");
        List<RuleDto> dtoList = translator.toDtoList(service.getAllRulesForOwner());
        return new BaseResponse<>(dtoList, HttpStatus.SC_OK).createRestResponse();
    }

    @Override
    @Transactional
    public Response create(SecurityContext securityContext, RuleDto ruleDto) {
        LOG.info("Create rule.");
        LowWaterWateringRule rule = translator.fromDto(ruleDto);
        service.createRule(rule);

        return new BaseResponse<>(ruleDto, HttpStatus.SC_CREATED).createRestResponse();
    }

    @Override
    @Transactional
    public Response update(String id, SecurityContext securityContext, RuleDto ruleDto) {
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
    @Transactional
    public Response delete(String id, SecurityContext securityContext) {
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
