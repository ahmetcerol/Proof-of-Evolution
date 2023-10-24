package com.blockchain.poe.domain;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transaction class represents a transaction in a blockchain. It encapsulates the
 * essential attributes of a transaction, including the sender, recipient, and the
 * transaction amount. The sender and recipient fields are validated to ensure they
 * are not empty, and the amount is checked to be not null. This class is used to
 * define and manage individual transactions within the blockchain.
 *
 * @author : Ahmet Can EROL
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @NotEmpty
    private String sender;
    @NotEmpty
    private String recipient;
    @NotNull
    private BigDecimal amount;
}