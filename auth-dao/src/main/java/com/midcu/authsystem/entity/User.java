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
@AllArgsConstructor
@NoArgsConstructor
@Table(name="sys_users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    @Column(unique = true)
    public String username;
    
    public String nickname;

    public String password;

    public String description;

    @Column(unique = true)
    public String phone;

    public String email;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    public Date createTime;

    @Column(nullable = false)
    public Integer state;
}