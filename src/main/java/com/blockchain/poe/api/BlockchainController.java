package com.blockchain.poe.api;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

import javax.validation.Valid;

import com.blockchain.poe.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.blockchain.poe.domain.Block;
import com.blockchain.poe.domain.Transaction;
import com.blockchain.poe.model.ChainResponse;
import com.blockchain.poe.model.MineResponse;
import com.blockchain.poe.model.TransactionResponse;
import com.blockchain.poe.service.Blockchain;

/**
 * Exposes Basic Blockchain related APIs.
 * You can access all API's with : <a href="http://localhost:8080/blockchain/">API'S url</a>
 *
 * @author Ahmet Can EROL
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/blockchain")
public class BlockchainController {

    @Autowired
    private Blockchain blockChain;

    @Autowired
    private ObjectMapper mapper;

    public static final String NODE_ID = UUID.randomUUID().toString().replace("-", "");
    public static final String NODE_ACCOUNT_ADDRESS = "0";
    public static final BigDecimal MINING_CASH_AWARD = BigDecimal.ONE;

    /*This API enables us to mine using a genetic algorithm, and you can access it by using 'http://localhost:8080/blockchain/poeMine'.*/
    @GetMapping("/poeMine")
    public MineResponse poeMine() throws Exception {

        // Start time
        long startTime = (System.currentTimeMillis());

        // (1) - Calculate the Proof of Evolution with Genetic Algorithm
        Block lastBlock = blockChain.lastBlock();
        Long previousProof = lastBlock.getProof();

        Long proofString = BlockProofOfEvolutionGenerator.proofOfEvolution(previousProof);
        //System.out.println(proofString); (Used for control)

        // (2) - Reward the miner (us) by adding a transaction granting us 1
        // coin
        blockChain.addTransaction(NODE_ACCOUNT_ADDRESS, NODE_ID, MINING_CASH_AWARD);

        // (3) - Forge the new Block by adding it to the chain
        Block newBlock = blockChain.createBlock(proofString, lastBlock.hash(mapper));

        double cpuUsage = cpuUsages.getProcessCpuLoad();
        System.out.println(cpuUsage);

        // Stop time and return execution time.
        long executionTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("Time taken for mining is  (Genetic Algorithm):" + executionTime +" seconds");

        return MineResponse.builder().message("New Block Forged").index(newBlock.getIndex()).transactions(newBlock.getTransactions()).proof(newBlock.getProof()).previousHsh(newBlock.getPreviousHash()).build();
    }

    /*This API enables us to mine using a Proof of Work , and you can access it by using 'http://localhost:8080/blockchain/mine'.*/
    @GetMapping("/Mine")
    public MineResponse mine() throws Exception {

        // Start time
        long startTime = (System.currentTimeMillis());

        // (1) - Calculate the Proof of Work
        Block lastBlock = blockChain.lastBlock();

        Long lastProof = lastBlock.getProof();

        Long proof = BlockProofOfWorkGenerator.proofOfWork(lastProof);

        // (2) - Reward the miner (us) by adding a transaction granting us 1
        // coin
        blockChain.addTransaction(NODE_ACCOUNT_ADDRESS, NODE_ID, MINING_CASH_AWARD);

        // (3) - Forge the new Block by adding it to the chain
        Block newBlock = blockChain.createBlock(proof, lastBlock.hash(mapper));

        // (4) - Measure CPU usage
        double cpuUsage = cpuUsages.getProcessCpuLoad();
        System.out.println(cpuUsage);

        // Stop time and return executionTime
        long executionTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("Time taken for mining is : (Proof of Work)" + executionTime +" seconds");

        // (5) - Return enameled block
        return MineResponse.builder().message("New Block Forged").index(newBlock.getIndex()).transactions(newBlock.getTransactions()).proof(newBlock.getProof()).previousHsh(newBlock.getPreviousHash()).build();

    }

    /*This API enables us to mine using a ant colony algorithm, and you can access it by using 'http://localhost:8080/blockchain/acoMine'.*/
    @GetMapping("/acoMine")
    public MineResponse acoMine() throws Exception {

        // Start time
        long startTime = (System.currentTimeMillis());
        // (1) - Calculate the Proof of Evolution with Ant Colony Algorithm
        Block lastBlock = blockChain.lastBlock();
        Long previousProof = lastBlock.getProof();
        Long proofString = AntColonyProofOfEvolutionGenerator.proofOfEvolution(previousProof);
        // (2) - Reward the miner (us) by adding a transaction granting us 1
        // coin
        //System.out.println(proofString);(used for control)
        blockChain.addTransaction(NODE_ACCOUNT_ADDRESS, NODE_ID, MINING_CASH_AWARD);

        // (3) - Forge the new Block by adding it to the chain
        Block newBlock = blockChain.createBlock(proofString, lastBlock.hash(mapper));

        // (4) - Measure CPU usage
        double cpuUsage = cpuUsages.getProcessCpuLoad();
        System.out.println(cpuUsage);

        // Stop time and return execution time
        long executionTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("Time taken for mining is : (Ant Colony Optimization)" + executionTime +" seconds");

        // (5) - Return enameled block
        return MineResponse.builder().message("New Block Forged").index(newBlock.getIndex()).transactions(newBlock.getTransactions()).proof(newBlock.getProof()).previousHsh(newBlock.getPreviousHash()).build();
    }

    /*This API enables us to mine using  artificial bee algorithm, and you can access it by using 'http://localhost:8080/blockchain/abcMine'.*/
    @GetMapping("/abcMine")
    public MineResponse abcMine() throws Exception {

        // Start time
        long startTime = (System.currentTimeMillis());
        // (1) - Calculate the Proof of Evolution with Artificial Bee Algorithm

        Block lastBlock = blockChain.lastBlock();
        Long previousProof = lastBlock.getProof();

        Long proofString = ArtificialBeeColonyProofOfEvolutionGenerator.proofOfEvolution(previousProof);

        // (2) - Reward the miner (us) by adding a transaction granting us 1
        // coin
        blockChain.addTransaction(NODE_ACCOUNT_ADDRESS, NODE_ID, MINING_CASH_AWARD);

        // (3) - Forge the new Block by adding it to the chain
        Block newBlock = blockChain.createBlock(proofString, lastBlock.hash(mapper));


        // (4) - Measure CPU usage
        double cpuUsage = cpuUsages.getProcessCpuLoad();
        System.out.println(cpuUsage);

        //Stop time and return execution time
        long executionTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("Time taken for calculation is : (Artificial Bee Colony Optimization)" + executionTime +" seconds");

        // (5) - Return enameled block
        return MineResponse.builder().message("New Block Forged").index(newBlock.getIndex()).transactions(newBlock.getTransactions()).proof(newBlock.getProof()).previousHsh(newBlock.getPreviousHash()).build();
    }

    /*This API enables us to see chain of blockchain , and you can access it by using 'http://localhost:8080/blockchain/chain'.*/
    @GetMapping("/chain")
    @CrossOrigin(origins = "http://localhost:3000")
    public ChainResponse fullChain() throws Exception {

        // - Measure CPU usage
        double cpuUsage = cpuUsages.getProcessCpuLoad();
        System.out.println(cpuUsage);
        return ChainResponse.builder().chain(blockChain.getChain()).length(blockChain.getChain().size()).build();
    }

    /*This API enables us to add transaction of blockchain , and you can access it by using 'http://localhost:8080/blockchain/transactions'.*/
    @PostMapping("/transactions")
    @CrossOrigin(origins = "http://localhost:3000")
    public TransactionResponse newTransaction(@RequestBody @Valid Transaction trans) throws Exception {

        // (1) - Transaction function
        Long index = blockChain.addTransaction(trans.getSender(), trans.getRecipient(), trans.getAmount());

        // (2) - Measure CPU usage
        double cpuUsage = cpuUsages.getProcessCpuLoad();
        System.out.println(cpuUsage);
        return TransactionResponse.builder().index(index).build();
    }

}