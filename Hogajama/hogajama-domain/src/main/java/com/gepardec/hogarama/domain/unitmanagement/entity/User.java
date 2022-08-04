package com.gepardec.hogarama.domain.unitmanagement.entity;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.google.common.base.Preconditions;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UserIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "UserIdGenerator", sequenceName = "seq_user_id")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "key")
    private String key;

    @OneToMany(mappedBy = "user")
    private List<Unit> unitList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Unit> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<Unit> unitList) {
        this.unitList = unitList;
    }

    public Unit getDefaultUnit() {
        return unitList.stream()
                .filter(Unit::isDefaultUnit)
                .findFirst()
                .orElseThrow(() ->
                        new TechnicalException(String.format("No Default unit given for user with id %s present.", getId())));
    }

    public void addUnit(Unit unit) {
        Preconditions.checkNotNull(unit, "Unit must not be null.");

        if (getUnitList() == null) {
            unitList = new ArrayList<>();
        }

        unitList.add(unit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(key, user.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key);
    }
}
