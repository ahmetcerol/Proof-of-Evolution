package com.blockchain.poe.domain;

import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Block class represents the structure of a block in a blockchain. It encapsulates
 * the key attributes of a block, including its index, timestamp, list of transactions,
 * proof of work, and the hash of the previous block. The @JsonPropertyOrder annotation
 * ensures that attributes are sorted alphabetically for consistent hashing. This class
 * also provides a method to calculate the hash of the block based on its content.
 *
 * @author : Ahmet Can Erol
 */
@Data
@Builder
/*
 * Hash should be calculated on the ordered list of attributes and hence keeping
 * them sorted to ensure that hashing is consistent.
 */
@JsonPropertyOrder(alphabetic = true)
@AllArgsConstructor
@NoArgsConstructor
public class Block {
    private Long index;
    private Long timestamp;
    private List<Transaction> transactions;
    private Long proof;
    // hash of the previous Block
    private String previousHash;
    public static final Long GENESIS_BLOCK_PROOF = 100L;
    public static final String GENESIS_BLOCK_PREV_HASH = "1";
    public String hash(ObjectMapper mapper) throws JsonProcessingException {
        String json = mapper.writeValueAsString(this);
        return Hashing.sha256().hashString(json, StandardCharsets.UTF_8).toString();
    }
}
