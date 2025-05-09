package id.ac.ui.cs.advprog.papikos.payment.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class PaymentDto {

    @Data
    public static class TopUpRequest {
        private UUID userId;
        private BigDecimal amount;
    }

    @Data
    public static class PaymentRequest {
        private UUID fromUserId;
        private UUID toUserId;
        private BigDecimal amount;
        private UUID roomId; // Optional, for room payment references
    }

    @Data
    public static class TransactionFilterRequest {
        private LocalDate startDate;
        private LocalDate endDate;
        private String transactionType;
    }

    @Data
    public static class TransactionResponse {
        private UUID id;
        private UUID fromUserId;
        private UUID toUserId;
        private BigDecimal amount;
        private String type;
        private String status;
        private String timestamp;
        private String description;
    }

    @Data
    public static class BalanceResponse {
        private UUID userId;
        private BigDecimal balance;
    }
}