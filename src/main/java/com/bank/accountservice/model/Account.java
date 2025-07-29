package com.bank.accountservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts") // تحديد اسم الجدول في قاعدة البيانات
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber; // رقم الحساب البنكي (مثل IBAN أو رقم داخلي)

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // لتخزين نوع الحساب كنص (مثال: SAVINGS, CURRENT)
    private AccountType accountType;

    @Column(nullable = false)
    private BigDecimal balance; // الرصيد الحالي للحساب

    @Column(nullable = false)
    private LocalDateTime createdAt; // تاريخ إنشاء الحساب

    private LocalDateTime updatedAt; // تاريخ آخر تحديث

    @ManyToOne // علاقة Many-to-One: العديد من الحسابات يمكن أن تنتمي لعميل واحد
    @JoinColumn(name = "customer_id", nullable = false) // المفتاح الخارجي الذي يربط بالعميل
    private Customer customer;

    // قبل الحفظ لأول مرة
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        // التأكد من أن الرصيد لا يكون Null عند الإنشاء
        if (this.balance == null) {
            this.balance = BigDecimal.ZERO;
        }
    }

    // قبل التحديث
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}