package com.debtapp.debt.service;

import com.debtapp.debt.controller.dto.DebtResponseDTO;
import com.debtapp.debt.controller.dto.DebtSplitRequestDTO;
import com.debtapp.debt.dao.model.Debt;
import com.debtapp.debt.dao.repository.DebtRepository;
import com.debtapp.user.controller.exceptions.UserNotFoundException;
import com.debtapp.user.dao.model.User;
import com.debtapp.user.dao.repository.UserRepository;
import com.debtapp.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.debtapp.config.security.SecurityUtils.getCurrentAuthenticatedUserId;

@Service
@RequiredArgsConstructor
public class DebtService {
    private final DebtRepository debtRepository;
    private final UserRepository userRepository;
    private static final Logger log = LogManager.getLogger(DebtService.class);

    public Debt createDebt(String title, String receiver, Double amount, Integer debtorId, Integer creditorId) {
        log.info("Creating a debt by user: {}, for user: {}", getCurrentAuthenticatedUserId(), debtorId);
        User debtor = userRepository.findById(debtorId)
                .orElseThrow(() ->
                        {
                            log.error("User with id: {}, not found", debtorId);
                            return new UserNotFoundException("Debtor with ID " + debtorId + " not found");
                        }
                );

        User creditor = userRepository.findById(creditorId)
                .orElseThrow(() -> {
                    log.error("User with id: {}, not found", creditorId);
                    return new UserNotFoundException("Creditor with ID " + creditorId + " not found");
                });

        Debt debt = Debt.builder()
                .title(title)
                .receiver(receiver)
                .amount(amount)
                .debtorUser(debtor)
                .creditorUser(creditor)
                .build();

        return debtRepository.save(debt);
    }

    public List<Debt> findAllDebts() {
        log.info("Fetching all debts from db");
        return debtRepository.findAll();
    }

    public Debt findDebtById(Integer id) {
        log.info("Fetching debt with id: {}", id);
        return debtRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Debt with ID " + id + " not found"));
    }

    public void deleteDebtById(Integer id) {
        log.info("Deleting debt with id: {}", id);
        Debt debt = findDebtById(id);
        debtRepository.delete(debt);
    }

    public List<Debt> findDebtsByDebtorId(Integer userId) {
        log.info("Fetching debts for user: {}", userId);
        return debtRepository.findAll().stream()
                .filter(d -> d.getDebtorUser().getId().equals(userId))
                .toList();
    }

    public Double sumDebtsByDebtor(Integer userId) {
        log.info("Calculating Sum of debts for user: {}", userId);
        return debtRepository.findAll().stream()
                .filter(d -> d.getDebtorUser().getId().equals(userId))
                .mapToDouble(Debt::getAmount)
                .sum();
    }

    public List<DebtResponseDTO> splitDebts(DebtSplitRequestDTO request, Integer userId) {
        log.info("Splitting debts that were requested by user : {}", userId);
        double costs = request.getCosts();
        Map<Integer, Double> payments = request.getPayments();

        int numUsers = payments.size();
        if (numUsers == 0) {
            throw new IllegalArgumentException("No users provided.");
        }

        double totalPayments = payments.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Double.compare(totalPayments, costs) != 0) {
            throw new IllegalArgumentException("Sum of payments (" + totalPayments + ") must equal total costs (" + costs + ").");
        }

        double fairShare = costs / numUsers;

        // Compute balances
        Map<Integer, Double> balances = new HashMap<>();
        for (Map.Entry<Integer, Double> entry : payments.entrySet()) {
            balances.put(entry.getKey(), Math.round((entry.getValue() - fairShare) * 100.0) / 100.0);
        }

        List<UserBalance> creditors = new ArrayList<>();
        List<UserBalance> debtors = new ArrayList<>();

        for (Map.Entry<Integer, Double> entry : balances.entrySet()) {
            if (entry.getValue() > 0) {
                creditors.add(new UserBalance(entry.getKey(), entry.getValue()));
            } else if (entry.getValue() < 0) {
                debtors.add(new UserBalance(entry.getKey(), -entry.getValue()));
            }
        }

        creditors.sort((a, b) -> Double.compare(b.balance, a.balance));
        debtors.sort((a, b) -> Double.compare(b.balance, a.balance));

        List<DebtResponseDTO> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < debtors.size() && j < creditors.size()) {
            UserBalance debtor = debtors.get(i);
            UserBalance creditor = creditors.get(j);

            double amount = Math.min(debtor.balance, creditor.balance);

            User debtorUser = userRepository.findById(debtor.userId)
                    .orElseThrow(() -> new UserNotFoundException("Debtor not found"));

            User creditorUser = userRepository.findById(creditor.userId)
                    .orElseThrow(() -> new UserNotFoundException("Creditor not found"));

            Debt debt = Debt.builder()
                    .title("Debt from " + debtorUser.getUsername() + " to " + creditorUser.getUsername())
                    .receiver(creditorUser.getUsername())
                    .amount(amount)
                    .debtorUser(debtorUser)
                    .creditorUser(creditorUser)
                    .build();

            debtRepository.save(debt);

            result.add(new DebtResponseDTO(debtor.userId, creditor.userId, amount));

            debtor.balance -= amount;
            creditor.balance -= amount;

            if (debtor.balance == 0) i++;
            if (creditor.balance == 0) j++;
        }

        return result;
    }

    private static class UserBalance {
        Integer userId;
        double balance;

        UserBalance(Integer userId, double balance) {
            this.userId = userId;
            this.balance = balance;
        }
    }
}

