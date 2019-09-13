package com.gepardec.hogarama.domain.entity.rule;

import com.gepardec.hogarama.domain.entity.Unit;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Rule {

    @Id
    @GeneratedValue(generator = "RuleIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "RuleIdGenerator", sequenceName = "seq_rule_id")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Unit getUnit() {
        return unit;
    }
}
