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
@Table(name="sys_menus")
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;

    @Column(nullable = false)
    public Long pid;

    @Column(nullable = false)
    public String path;

    public Boolean display;

    @Column(nullable = false)
    public String title;

    public String component;

    public String name;

    public String description;

    public String icon;

    public String layout;

    @Column(nullable = false)
    public Integer type;

    @Column(nullable = false)
    public Integer sort;

    public Boolean iframe;

    public String iframeSrc;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    public Date createTime;

    @Column(nullable = false)
    public Integer state;
}
