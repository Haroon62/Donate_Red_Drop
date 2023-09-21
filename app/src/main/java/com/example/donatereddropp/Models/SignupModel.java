package com.example.donatereddropp.Models;

import java.io.Serializable;

public class SignupModel implements Serializable {

    String id, purl,name,bgroup,donatee,phone,emaiil,paswrd,addresmodel,street,gender,fcmtoken;

    public SignupModel() {

    }

    public SignupModel(String id,String purl, String name, String addresmodel,
                       String bgroup, String donatee ,
                       String phone, String emaiil, String paswrd,String street,String gender,String fcmtoken) {

        this.id=id;
        this.purl=purl;
        this.name = name;
        this.addresmodel=addresmodel;
        this.bgroup = bgroup;
        this.donatee=donatee;
        this.phone=phone;
        this.emaiil = emaiil;
        this.paswrd = paswrd;
        this.street=street;
        this.gender=gender;
        this.fcmtoken=fcmtoken;
    }

    public String getFcmtoken() {
        return fcmtoken;
    }

    public void setFcmtoken(String fcmtoken) {
        this.fcmtoken = fcmtoken;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getDonatee() {
        return donatee;
    }

    public void setDonatee(String donatee) {
        this.donatee = donatee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBgroup() {
        return bgroup;
    }

    public void setBgroup(String bgroup) {
        this.bgroup = bgroup;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmaiil() {
        return emaiil;
    }

    public void setEmaiil(String emaiil) {
        this.emaiil = emaiil;
    }

    public String getPaswrd() {
        return paswrd;
    }

    public String getAddresmodel() {
        return addresmodel;
    }

    public void setAddresmodel(String addresmodel) {
        this.addresmodel = addresmodel;
    }

    public void setPaswrd(String paswrd) {
        this.paswrd = paswrd;
    }

    @Override
    public String toString() {
        return "SignupModel{" +
                "purl='" + purl + '\'' +
                ", name='" + name + '\'' +
                ", bgroup='" + bgroup + '\'' +
                ", donatee='" + donatee + '\'' +
                ", phone='" + phone + '\'' +
                ", emaiil='" + emaiil + '\'' +
                ", paswrd='" + paswrd + '\'' +
                ", addresmodel='" + addresmodel + '\'' +
                '}';
    }
}
