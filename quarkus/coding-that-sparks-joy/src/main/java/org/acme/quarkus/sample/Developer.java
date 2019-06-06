package org.acme.quarkus.sample;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Developer extends PanacheEntity
{
    public String name;
}
