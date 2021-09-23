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
@Table(name="sys_roles_menus")
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenu {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    public Long roleId;

    public Long menuId;
}
