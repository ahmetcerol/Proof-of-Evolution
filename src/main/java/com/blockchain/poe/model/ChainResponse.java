package com.blockchain.poe.model;

import java.util.List;

import com.blockchain.poe.domain.Block;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Holds Chain details.
 *
 * @author Ahmet Can Erol
 *
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChainResponse {
    private Integer length;
    private List<Block> chain;
}