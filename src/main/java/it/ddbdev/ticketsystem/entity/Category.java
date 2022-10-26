package it.ddbdev.ticketsystem.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@RequiredArgsConstructor
public class Category {

    @Id
    @Column(length = 15)
    private String id;

    private boolean visible = true;
}

