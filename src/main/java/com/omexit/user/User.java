package com.omexit.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by aomeri on 6/22/2015.
 */
@Data
@Entity
@Table(name = "tbl_user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonProperty("full_name")
    @Column(name = "full_name", nullable = false)
    private String fullNname;
    @JsonProperty("user_name")
    @Column(unique = true, name = "user_name", nullable = false)
    private String username;
    @JsonProperty("email")
    @Column(name = "email")
    private String email;
//    @JsonProperty("id_type")
//    @Column(name = "id_type")
//    private IdType idType;
    @JsonProperty("id_number")
    @Column(name = "id_number")
    private String idNumber;
    @JsonProperty("designation")
    @Column(name = "designation")
    private String designation;
    @JsonProperty("postal_number")
    @Column(name = "postal_number")
    private String postalNumber;
    @JsonProperty("postal_code")
    @Column(name = "postal_code")
    private String postalCode;
    @JsonProperty("town")
    @Column(name = "town")
    private String town;
    @JsonProperty("user_password")
    @Column(name = "user_password")
    private String password;
    @Column(name = "is_enabled")
    private boolean enabled;
//    @ManyToMany
//    @JoinTable(name = "tbl_user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private Collection<Role> roles;

    public User(){}
    public User(User user){
        this.id=user.getId();
        this.fullNname=user.getFullNname();
        this.username=user.getUsername();
        this.email=user.getEmail();
        this.idNumber=user.getIdNumber();
        this.designation=user.getDesignation();
        this.postalNumber=user.getPostalNumber();
        this.postalCode=user.getPostalCode();
        this.town=user.getTown();
        this.password=user.getPassword();
        this.enabled=user.isEnabled();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullNname='" + fullNname + '\'' +
                ", userName='" + username + '\'' +
                ", email='" + email + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", designation='" + designation + '\'' +
                ", postalNumber='" + postalNumber + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", town='" + town + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
