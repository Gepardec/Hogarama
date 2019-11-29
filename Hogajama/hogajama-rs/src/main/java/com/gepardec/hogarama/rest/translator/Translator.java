package com.gepardec.hogarama.rest.translator;

import com.gepardec.hogarama.rest.v2.dto.BaseDTO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface Translator<DTO extends BaseDTO, BO> {

    DTO toDto(BO bo);

    BO fromDto(DTO dto);

    default List<DTO> toDtoList(Collection<BO> boCollection) {
        return boCollection.stream().map(this::toDto).collect(Collectors.toList());
    }

    default List<BO> fromDtoList(Collection<DTO> dtoCollection) {
        return dtoCollection.stream().map(this::fromDto).collect(Collectors.toList());
    }
}
