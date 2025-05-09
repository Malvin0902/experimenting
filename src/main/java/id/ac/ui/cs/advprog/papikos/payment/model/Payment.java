package id.ac.ui.cs.advprog.papikos.payment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Payment {
    @Id
    private UUID id;
    private UUID fromUserId;
    private UUID toUserId;
    @Column(nullable = false)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private LocalDateTime timestamp;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private UUID roomId;
    private String description;

    public Payment(UUID fromUserId, UUID toUserId, BigDecimal amount,
                   TransactionType type, PaymentStatus status,
                   UUID roomId, String description) {
        this.id = UUID.randomUUID();
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.roomId = roomId;
        this.description = description;
    }
}