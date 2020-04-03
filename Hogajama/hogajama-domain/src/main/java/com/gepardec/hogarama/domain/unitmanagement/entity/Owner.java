package com.gepardec.hogarama.domain.unitmanagement.entity;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.NotImplementedException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Owner implements Serializable {

    @Id
    @GeneratedValue(generator = "OwnerIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "OwnerIdGenerator", sequenceName = "seq_owner_id")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "sso_user_id")
    private String ssoUserId;

    @OneToMany(mappedBy = "owner")
    private List<Unit> unitList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSsoUserId() {
        return ssoUserId;
    }

    public void setSsoUserId(String ssoUserId) {
        this.ssoUserId = ssoUserId;
    }

    public List<Unit> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<Unit> unitList) {
        this.unitList = unitList;
    }

    public Unit getDefaultUnit() {
        throw new NotImplementedException("add isDefault to unit first");
//        return getUnitList().stream()
////                .filter(Unit::isDefaultUnit)
//                .findFirst()
//                .orElseThrow(() ->
//                        new RuntimeException(String.format("No Default unit given for user with id %s present.", getId())));
    }

    public void addToUnitList(Unit unit) {
        Preconditions.checkNotNull(unit, "Unit must not be null.");

        if (getUnitList() == null) {
            unitList = new ArrayList<>();
        }
        unitList.add(unit);
    }
}
