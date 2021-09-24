package com.midcu.authsystem.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="sys_permission")
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    @Column(unique = true)
    public String name;

    public String title;

    public String description;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    public Date createTime;

    @Column(nullable = false)
    public Long pid;

    public Integer state;
}
