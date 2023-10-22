package com.blockchain.poe.api;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

import javax.validation.Valid;

import com.blockchain.poe.util.BlockProofOfEvolutionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.blockchain.poe.domain.Block;
import com.blockchain.poe.domain.Transaction;
import com.blockchain.poe.model.ChainResponse;
import com.blockchain.poe.model.MineResponse;
import com.blockchain.poe.model.TransactionResponse;
import com.blockchain.poe.service.Blockchain;
import com.blockchain.poe.util.BlockProofOfWorkGenerator;

/**
 * Exposes Basic Blockchain related APIs.
 *
 * @author Ahmet Can EROL
 *
 */
@RestController
@RequestMapping("/blockchain")
public class BlockchainController {

    @Autowired
    private Blockchain blockChain;

    @Autowired
    private ObjectMapper mapper;

    public static final String NODE_ID = UUID.randomUUID().toString().replace("-", "");
    public static final String NODE_ACCOUNT_ADDRESS = "0";
    public static final BigDecimal MINING_CASH_AWARD = BigDecimal.ONE;
    @GetMapping("/poeMine")
    public MineResponse poeMine() throws JsonProcessingException {
        Block lastBlock = blockChain.lastBlock();
        Long previousProof = lastBlock.getProof();

        String proofString = BlockProofOfEvolutionGenerator.proofOfEvolution(previousProof); // Dizeden dönüşüm
        Long proof = null;
        try {
            proof = Long.parseLong(proofString);
        } catch (NumberFormatException e) {
            // Geçersiz diziyi ele almak için bir işlem yapabilirsiniz.
            // Örneğin, rastgele bir sayı oluşturabilir veya başka bir işlem yapabilirsiniz.
            proof = generateRandomLong(); // Rasgele bir Long üretildiğini varsayalım.
        }
// Dönüş değerini Long'a çevirin


        blockChain.addTransaction(NODE_ACCOUNT_ADDRESS, NODE_ID, MINING_CASH_AWARD);

        Block newBlock = blockChain.createBlock(proof, lastBlock.hash(mapper));

        return MineResponse.builder().message("New Block Forged").index(newBlock.getIndex())
                .transactions(newBlock.getTransactions()).proof(newBlock.getProof())
                .previousHsh(newBlock.getPreviousHash()).build();
    }private Long generateRandomLong() {
        Random random = new Random();
        return random.nextLong();
    }


    @GetMapping("/mine")
    public MineResponse mine() throws JsonProcessingException {

        // (1) - Calculate the Proof of Work
        Block lastBlock = blockChain.lastBlock();

        Long lastProof = lastBlock.getProof();

        Long proof = BlockProofOfWorkGenerator.proofOfWork(lastProof);

        // (2) - Reward the miner (us) by adding a transaction granting us 1
        // coin
        blockChain.addTransaction(NODE_ACCOUNT_ADDRESS, NODE_ID, MINING_CASH_AWARD);

        // (3) - Forge the new Block by adding it to the chain
        Block newBlock = blockChain.createBlock(proof, lastBlock.hash(mapper));

        return MineResponse.builder().message("New Block Forged").index(newBlock.getIndex())
                .transactions(newBlock.getTransactions()).proof(newBlock.getProof())
                .previousHsh(newBlock.getPreviousHash()).build();
    }

    @GetMapping("/chain")
    public ChainResponse fullChain() throws JsonProcessingException {
        return ChainResponse.builder().chain(blockChain.getChain()).length(blockChain.getChain().size()).build();
    }

    @PostMapping("/transactions")
    public TransactionResponse newTransaction(@RequestBody @Valid Transaction trans) throws JsonProcessingException {

        Long index = blockChain.addTransaction(trans.getSender(), trans.getRecipient(), trans.getAmount());

        return TransactionResponse.builder().index(index).build();
    }

}