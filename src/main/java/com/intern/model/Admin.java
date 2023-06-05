package com.intern.model;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name="admin")
@Getter
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Length(min = 2, max = 20)
    private String firstName;

    @NotNull
    @Length(min = 2, max = 20)
    private String lastName;

    @NotNull
    private String name;

    @NotNull
    @Length(max = 255, min = 10)
    @Column(name = "email",unique = true,nullable = false)
    private String mail;

    @NotNull
    @Column(name = "password",nullable = true)
    private String password;

    private String role;

    private String permission;

    public Admin() {

    }

    public static class Builder {
        private final Admin admin;

        public Builder() {
            this.admin = new Admin();
        }

        public Builder name(String first, String last){
            admin.firstName = first;
            admin.lastName = last;
            admin.name = admin.firstName + admin.lastName;
            return this;
        }
        public Builder mail(String mail){
            admin.mail = mail;
            return this;
        }

        public Builder password(String password){
            admin.password = password;
            return this;
        }

        public Builder role(String...roles) {
            StringBuilder sb = new StringBuilder();
            for(String role : roles) {
                sb.append(String.format("%s,", role));
            }
            admin.role = sb.toString();
            return this;
        }

        public Builder permission(String...permissions) {
            StringBuilder sb = new StringBuilder();
            for(String permission : permissions) {
                sb.append(String.format("%s,", permission));
            }
            admin.permission = sb.toString();
            return this;
        }

        public Admin build() {
            return this.admin;
        }
    }

    public List<String> getRoleList(){
        if(this.role.length()>0){
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }

    public List<String> getPermissionList(){
        if(this.permission.length()>0){
            return Arrays.asList(this.permission.split(","));
        }
        return new ArrayList<>();
    }
}
