package id.ac.ui.cs.advprog.papikos.payment.service;

import id.ac.ui.cs.advprog.papikos.payment.model.Payment;
import id.ac.ui.cs.advprog.papikos.payment.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PaymentService {
    /**
     * Add funds to a user's wallet
     *
     * @param userId User ID to top up
     * @param amount Amount to add to balance
     */
    void topUp(UUID userId, BigDecimal amount);

    /**
     * Get current balance for a user
     *
     * @param userId User ID to check
     * @return Current balance
     */
    BigDecimal getBalance(UUID userId);

    /**
     * Transfer money from one user to another
     *
     * @param fromUserId Source user ID
     * @param toUserId Destination user ID
     * @param amount Amount to transfer
     */
    void pay(UUID fromUserId, UUID toUserId, BigDecimal amount);

    /**
     * Get all transactions for a specific user (as sender or receiver)
     *
     * @param userId User ID
     * @return List of transactions
     */
    List<Payment> getUserTransactions(UUID userId);

    /**
     * Filter transactions by date range and/or type
     *
     * @param userId User ID
     * @param startDate Start date (optional)
     * @param endDate End date (optional)
     * @param type Transaction type (optional)
     * @return Filtered list of transactions
     */
    List<Payment> filterTransactions(UUID userId, LocalDate startDate, LocalDate endDate, TransactionType type);
}