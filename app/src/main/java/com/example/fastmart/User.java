package com.example.fastmart;
public class User {
    String email, name, accountType, phone, gender, country, residential_add, code, uid;
    public User() {}
    public User(String email, String name, String accountType, String code, String phone, String gender, String country, String residential_add){
        this.email = email;
        this.name = name;
        this.accountType = accountType;
        this.phone = phone;
        this.gender = gender;
        this.country = country;
        this.residential_add = residential_add;
        this.code = code;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getEmail(){
        return email;
    }
    public String getName(){
        return name;
    }
    public String getAccountType(){
        return accountType;
    }
    public String getPhone(){
        return phone;
    }
    public String getGender(){
        return gender;
    }
    public String getCountry(){
        return country;
    }
    public String getResidential_add(){
        return residential_add;
    }
    public String getCode(){
        return code;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setResidential_add(String residential_add) {
        this.residential_add = residential_add;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
