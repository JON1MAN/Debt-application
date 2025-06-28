package com.debtapp.group.dao.model;

import com.debtapp.user.dao.model.AbstractEntity;
import com.debtapp.user.dao.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "`groups`")
public class Group extends AbstractEntity {

    private String name;

    @ManyToMany
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> subscribedUsers = new ArrayList<>();

    public Group(String groupName) {
        this.name = groupName;
    }
}
