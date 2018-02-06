package com.wondersgroup.hiip.empi.service;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/2.
 */
@Entity
@Table(name="District")
public class District implements Serializable{

    @Id
    private String id;
    private String identifier;
    private String name;
    private String version;
    private String status;
    private String parent;
    private int codelevel;
    private String smallname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getCodelevel() {
        return codelevel;
    }

    public void setCodelevel(int codelevel) {
        this.codelevel = codelevel;
    }

    public String getSmallname() {
        return smallname;
    }

    public void setSmallname(String smallname) {
        this.smallname = smallname;
    }

}
