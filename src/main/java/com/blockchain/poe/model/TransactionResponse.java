package com.blockchain.poe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Holds the newly added block transaction details.
 *
 * @author Ahmet Can EROL
 *
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private Long index;
}