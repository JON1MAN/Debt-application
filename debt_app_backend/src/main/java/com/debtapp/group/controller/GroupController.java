package com.debtapp.group.controller;

import com.debtapp.debt.controller.dto.DebtResponseDTO;
import com.debtapp.debt.controller.dto.DebtSplitRequestDTO;
import com.debtapp.debt.service.DebtService;
import com.debtapp.group.dao.model.Group;
import com.debtapp.group.service.GroupService;
import com.debtapp.user.controller.mapper.dto.UserIdsDTO;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.debtapp.config.security.SecurityUtils.getCurrentAuthenticatedUserId;

@RestController
@RequestMapping("/v1/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    private final DebtService debtService;
    private static final Logger log = LogManager.getLogger(GroupController.class);

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestParam String name) {
        log.info("Received a request to create a group with name: {}, by user: {}",
                name,
                getCurrentAuthenticatedUserId()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(groupService.createGroup(name, getCurrentAuthenticatedUserId()));
    }

    @PutMapping("/{groupId}/add_users")
    public ResponseEntity<Group> addUsersToGroup(
            @PathVariable Integer groupId,
            @RequestBody UserIdsDTO request
    ) {
        log.info("Received a request to add users to group: {}, by user: {}", groupId, getCurrentAuthenticatedUserId());
        return ResponseEntity.ok(groupService.addUsersToGroup(groupId, request, getCurrentAuthenticatedUserId()));
    }

    @PostMapping("/split-debts")
    public ResponseEntity<List<DebtResponseDTO>> splitDebts(@RequestBody DebtSplitRequestDTO request) {
        log.info("Splitting debts for {} users", request.getPayments().size());
        return ResponseEntity.ok(debtService.splitDebts(request, getCurrentAuthenticatedUserId()));
    }

}
