package com.company.store.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "store_users")
@NamedQueries({
          @NamedQuery(name=User.USER_FIND_ALL, query=User.USER_FIND_ALL_JPQL),
          @NamedQuery(name=User.USER_FIND_BY_EMAIL, query=User.USER_FIND_BY_EMAIL_JPQL)
})
public class User {

    // get list of all users
    public static final String USER_FIND_ALL = "userAll";
    public static final String USER_FIND_ALL_JPQL = "SELECT u FROM User u";
    // get list of all users
    public static final String USER_FIND_BY_EMAIL = "userByEmail";
    public static final String USER_FIND_BY_EMAIL_JPQL = "SELECT u FROM User u WHERE u.email = :email";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String firstname;

    private String lastname;

    @Column(name="email")
    @NotEmpty(message = "Email can't be empty.")
    private String email;

    private String role;

    private Integer enabled;

    @Column(name="password")
    @NotEmpty(message = "Password can't be empty.")
    private String password;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
