package com.gepardec.hogarama.domain.owner;

import com.gepardec.hogarama.domain.GenericDao;
import com.gepardec.hogarama.domain.entity.Owner;

import java.util.Optional;

public interface OwnerDao extends GenericDao<Owner> {

    Optional<Owner> getBySsoUserId(String ssoUserId);

}
