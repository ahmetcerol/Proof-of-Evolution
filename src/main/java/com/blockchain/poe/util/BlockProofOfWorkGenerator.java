package com.blockchain.poe.util;
import java.nio.charset.StandardCharsets;
import com.google.common.hash.Hashing;

/**
 A Proof of Work (PoW) algorithm is a crucial component
 of blockchain technology used to create or mine new blocks.
 The primary objective of PoW is to identify a specific number
 that can solve a complex computational problem.
 This number should be challenging to find, requiring substantial computational effort,
 but once discovered, it must be effortlessly verifiable by any participant on the network.
 This fundamental concept of PoW ensures that the process of creating new blocks is secure, fair, and transparent,
 as it demands work from miners, making it difficult for any single entity to dominate the network, while still allowing
 others to quickly confirm the validity of the solution.
 *
 * @author Ahmet Can Erol
 *
 */

public class BlockProofOfWorkGenerator {
    /**
      Simple Proof of Work Algorithm:

      - Find a number p` such that hash(pp`) contains leading 6 zeroes, where p
      is the previous p`

      - p is the previous proof, and p` is the new proof

      Find a number p that when hashed with the previous block’s solution a
      hash with 6 leading 0s is produced.
     */
    public static String PROOF_OF_WORK ="00000";
    public static Long proofOfWork(Long lastProof) {
        long proof = 0L;
        while (!validProof(lastProof, proof)) {
            proof +=1L;
        }
        return proof;
    }
    public static boolean validProof(Long lastProof, Long proof) {
        String s = ""+lastProof+""+proof;
        String sha256 = Hashing.sha256().hashString(s,StandardCharsets.UTF_8).toString();
        return sha256.endsWith(PROOF_OF_WORK);}
}
