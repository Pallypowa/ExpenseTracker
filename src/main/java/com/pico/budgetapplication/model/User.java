package com.pico.budgetapplication.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Profile")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "user_name",
            nullable = false,
            unique = true)
    private String username;
    @Column(name = "password",
            nullable = false)
    private String password;

    @Column(name = "email", nullable = false,
            unique = true)
    private String email;

    private String firstName;
    private String lastName;
    private Integer age;
    private Gender gender;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Expense> expenses;
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Income> incomes;
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Category> categories;

    public User(String username, String password, String email, List<Expense> expenses) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.expenses = expenses;
    }

    public User(Long id){
        this.id = id;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password, String email, String firstName, String lastName, Integer age, Gender gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
    }

    public User() {
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

