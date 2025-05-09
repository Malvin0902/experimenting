package id.ac.ui.cs.advprog.papikos.payment.service;

import id.ac.ui.cs.advprog.papikos.payment.model.Payment;
import id.ac.ui.cs.advprog.papikos.payment.model.PaymentStatus;
import id.ac.ui.cs.advprog.papikos.payment.model.TransactionType;
import id.ac.ui.cs.advprog.papikos.payment.model.Wallet;
import id.ac.ui.cs.advprog.papikos.payment.repository.IPaymentRepository;
import id.ac.ui.cs.advprog.papikos.payment.repository.WalletRepository;
import id.ac.ui.cs.advprog.papikos.authentication.model.User;
import id.ac.ui.cs.advprog.papikos.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final IPaymentRepository paymentRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void topUp(UUID userId, BigDecimal amount) {
        validateAmount(amount);
        validateUserExists(userId);

        Wallet wallet = getOrCreateWallet(userId);

        Payment payment = new Payment(
                null,
                userId,
                amount,
                TransactionType.TOPUP,
                PaymentStatus.SUCCESS,
                null,
                "Top-up balance"
        );

        // Update wallet balance
        wallet.addBalance(amount);

        // Save both records
        walletRepository.save(wallet);
        paymentRepository.save(payment);
    }

    @Override
    public BigDecimal getBalance(UUID userId) {
        validateUserExists(userId);

        Optional<Wallet> wallet = walletRepository.findById(userId);
        return wallet.map(Wallet::getBalance).orElse(BigDecimal.ZERO);
    }

    @Override
    @Transactional
    public void pay(UUID fromUserId, UUID toUserId, BigDecimal amount) {
        validateAmount(amount);
        validateUserExists(fromUserId);
        validateUserExists(toUserId);

        // Get wallets
        Wallet fromWallet = getOrCreateWallet(fromUserId);
        Wallet toWallet = getOrCreateWallet(toUserId);

        // Check balance
        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Saldo tidak mencukupi");
        }

        // Create payment record
        Payment payment = new Payment(
                fromUserId,
                toUserId,
                amount,
                TransactionType.PAYMENT,
                PaymentStatus.SUCCESS,
                null, // Will be set when room payment is implemented
                "Pembayaran kos"
        );

        // Update wallets
        fromWallet.subtractBalance(amount);
        toWallet.addBalance(amount);

        // Save all records
        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);
        paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getUserTransactions(UUID userId) {
        validateUserExists(userId);
        return paymentRepository.findByUserId(userId);
    }

    @Override
    public List<Payment> filterTransactions(UUID userId, LocalDate startDate, LocalDate endDate, TransactionType type) {
        List<Payment> transactions = paymentRepository.findByUserId(userId);

        // Apply filters
        return transactions.stream()
                .filter(payment -> {
                    boolean dateFilter = true;
                    if (startDate != null && endDate != null) {
                        LocalDateTime paymentDate = payment.getTimestamp();
                        dateFilter = !paymentDate.toLocalDate().isBefore(startDate) &&
                                !paymentDate.toLocalDate().isAfter(endDate);
                    }

                    boolean typeFilter = true;
                    if (type != null) {
                        typeFilter = payment.getType() == type;
                    }

                    return dateFilter && typeFilter;
                })
                .collect(Collectors.toList());
    }

    private Wallet getOrCreateWallet(UUID userId) {
        return walletRepository.findById(userId)
                .orElseGet(() -> {
                    Wallet newWallet = new Wallet(userId);
                    return walletRepository.save(newWallet);
                });
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Jumlah harus lebih besar dari nol");
        }
    }

    private void validateUserExists(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User dengan ID " + userId + " tidak ditemukan");
        }
    }
}