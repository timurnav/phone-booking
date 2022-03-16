package com.yourcompany.phonebooking.entity;

import javax.persistence.*;

@MappedSuperclass
public class IdentifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @SequenceGenerator(name = "global_seq", sequenceName = "SEQ_GLOBAL", allocationSize = 10)
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
