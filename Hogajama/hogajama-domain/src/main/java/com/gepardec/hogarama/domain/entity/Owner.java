package com.gepardec.hogarama.domain.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Owner {

    @Id
    @GeneratedValue(generator = "OwnerIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "OwnerIdGenerator", sequenceName = "seq_owner_id")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "sso_user_id", nullable = false)
    private Long ssoUserId;

    @OneToMany(mappedBy = "owner")
    private List<Unit> unitList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSsoUserId() {
        return ssoUserId;
    }

    public void setSsoUserId(Long ssoUserId) {
        this.ssoUserId = ssoUserId;
    }

    public List<Unit> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<Unit> unitList) {
        this.unitList = unitList;
    }
}
