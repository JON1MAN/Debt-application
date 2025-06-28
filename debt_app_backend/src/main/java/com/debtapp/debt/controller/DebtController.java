package com.debtapp.debt.controller;

import com.debtapp.debt.controller.dto.DebtCreateRequestDTO;
import com.debtapp.debt.controller.dto.DebtMapper;
import com.debtapp.debt.controller.dto.DebtSummaryDTO;
import com.debtapp.debt.controller.dto.DebtsDTO;
import com.debtapp.debt.dao.model.Debt;
import com.debtapp.debt.service.DebtService;
import com.debtapp.user.controller.UserController;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.debtapp.config.security.SecurityUtils.getCurrentAuthenticatedUserId;

@RestController
@RequestMapping("/v1/debts")
@RequiredArgsConstructor
public class DebtController {

    private final DebtService debtService;
    private static final Logger log = LogManager.getLogger(DebtController.class);
    @GetMapping
    public ResponseEntity<DebtsDTO> getAllDebtsOfCurrentUser() {
        Integer currentUserId = getCurrentAuthenticatedUserId();
        log.info("Received a request to get all debts for user: {}", currentUserId);
        var result = debtService.findDebtsByDebtorId(currentUserId)
                .stream()
                .map(DebtMapper::map)
                .toList();
        DebtsDTO response = new DebtsDTO();
        response.setDebts(result);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Debt> createDebt(@RequestBody DebtCreateRequestDTO request) {
        Integer currentUserId = getCurrentAuthenticatedUserId();
        log.info("Received a request to create debt from user: {}", currentUserId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(debtService.createDebt(
                        request.getTitle(),
                        request.getReceiver(),
                        request.getAmount(),
                        request.getUserId(),
                        request.getReceiverId()
                ));
    }

    @DeleteMapping("/{debtId}")
    public ResponseEntity<Map<String, String>> deleteDebt(@PathVariable Integer debtId) {
        Integer currentUserId = getCurrentAuthenticatedUserId();
        log.info("Received a request to delete debt with id:{}, from user: {}",debtId, currentUserId);
        debtService.deleteDebtById(debtId);
        return ResponseEntity.ok(Map.of("message", "Debt deleted"));
    }

    @GetMapping("/my/sum")
    public ResponseEntity<DebtSummaryDTO> getSumOfMyDebts() {
        Integer currentUserId = getCurrentAuthenticatedUserId();
        log.info("Received a request to get sum of all debts for user: {}", currentUserId);
        Double sum = debtService.sumDebtsByDebtor(currentUserId);
        return ResponseEntity.ok(new DebtSummaryDTO(currentUserId, sum));
    }
}

