package com.blockchain.poe.model;

import java.util.List;

import com.blockchain.poe.domain.Transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Holds the mined block details.
 *
 * @author Ahmet Can Erol
 *
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MineResponse {
    private String message;
    private Long index;
    private List<Transaction> transactions;
    private Long proof;
    private String previousHsh;
}