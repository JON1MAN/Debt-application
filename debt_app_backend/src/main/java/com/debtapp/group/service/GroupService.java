package com.debtapp.group.service;

import com.debtapp.group.controller.exceptions.GroupNotFoundException;
import com.debtapp.group.dao.model.Group;
import com.debtapp.group.dao.repository.GroupRepository;
import com.debtapp.user.controller.mapper.dto.UserIdsDTO;
import com.debtapp.user.dao.model.User;
import com.debtapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private static final Logger log = LogManager.getLogger(GroupService.class);
    private final UserService userService;

    public Group findById(Integer id) {
        log.info("Fetching group by id: {}", id);
        return groupRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Group not found with id: {}", id);
                    return new GroupNotFoundException("Group with provided id not found");
                });
    }

    public Group createGroup(String name, Integer creatorId) {
        log.info("Creating a group with name: {}, by user: {}", name, creatorId);
        Group group = new Group(name);
        User user = userService.findById(creatorId);

        addUserToGroup(group, user);
        return groupRepository.save(group);
    }

    public Group addUsersToGroup(Integer groupId, UserIdsDTO dto, Integer creatorId) {
        log.info("Adding users to group: {}, by user: {}", groupId, creatorId);

        if (!isUserPartOfGroup(groupId, creatorId)) {
            log.error("User: {}, is not of group: {}", creatorId, groupId);
            throw new AccessDeniedException("You are not a part of this group");
        }

        Group group = findById(groupId);
        var users = userService.findAllUsers(dto.getUsersIds());

        addUsersToGroup(group, users);

        return groupRepository.save(group);
    }

    public void addUsersToGroup(Group group, List<User> users) {
        group.getSubscribedUsers().addAll(users);
    }

    public void addUserToGroup(Group group, User user) {
        group.getSubscribedUsers().add(user);
    }

    private boolean isUserPartOfGroup(Integer groupId, Integer userId) {
        return findById(groupId).getSubscribedUsers()
                .stream()
                .map(User::getId)
                .toList()
                .contains(userId);
    }
}
