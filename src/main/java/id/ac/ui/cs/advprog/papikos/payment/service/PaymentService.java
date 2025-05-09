package id.ac.ui.cs.advprog.papikos.payment.service;

import id.ac.ui.cs.advprog.papikos.payment.model.Payment;
import id.ac.ui.cs.advprog.papikos.payment.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PaymentService {
    void topUp(UUID userId, BigDecimal amount);
    void pay(UUID fromUserId, UUID toUserId, BigDecimal amount);
    BigDecimal getBalance(UUID userId);
    List<Payment> getUserTransactions(UUID userId);
    List<Payment> filterTransactions(UUID userId, LocalDate startDate, LocalDate endDate, TransactionType type);
}