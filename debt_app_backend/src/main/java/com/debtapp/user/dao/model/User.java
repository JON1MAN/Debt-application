package com.debtapp.user.dao.model;

import com.debtapp.group.dao.model.Group;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "users")
public class User extends AbstractEntity{

    private String email;

    @Column(name = "hashed_password")
    private String hashedPassword;

    private String username;

    @ManyToMany(mappedBy = "subscribedUsers")
    private List<Group> groups = new ArrayList<>();

}
