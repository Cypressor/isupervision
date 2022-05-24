package com.cypress.isupervision.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;

@Entity
public class Assistent extends AbstractEntity {

    private String vorname;
    private String nachname;
    private String email;
    @JsonIgnore
    private String passwort;
    private Integer projLimit;
    private Integer baLimit;
    private Integer maLimit;

    public String getVorname() {
        return vorname;
    }
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }
    public String getNachname() {
        return nachname;
    }
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPasswort() {
        return passwort;
    }
    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
    public Integer getProjLimit() {
        return projLimit;
    }
    public void setProjLimit(Integer projLimit) {
        this.projLimit = projLimit;
    }
    public Integer getBaLimit() {
        return baLimit;
    }
    public void setBaLimit(Integer baLimit) {
        this.baLimit = baLimit;
    }
    public Integer getMaLimit() {
        return maLimit;
    }
    public void setMaLimit(Integer maLimit) {
        this.maLimit = maLimit;
    }

}
