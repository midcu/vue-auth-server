package com.midcu.authsystem.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="sys_users_roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    public Long userId;

    public Long roleId;

}
