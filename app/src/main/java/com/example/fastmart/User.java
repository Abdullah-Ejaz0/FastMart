package com.example.fastmart;

public class User {
    String username, password, name, accountType, phone, gender, country, residential_add, code;
    User(String username, String password, String name, String accountType, String code, String phone, String gender, String country, String residential_add){
        this.username = username;
        this.password = password;
        this.name = name;
        this.accountType = accountType;
        this.phone = phone;
        this.gender = gender;
        this.country = country;
        this.residential_add = residential_add;
        this.code = code;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
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
}
