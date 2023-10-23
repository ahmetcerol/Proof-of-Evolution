package com.blockchain.poe.util;

import java.nio.charset.StandardCharsets;
import com.google.common.hash.Hashing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/*
 *
 * A genetic algorithm is a computational optimization technique inspired by the process of natural selection and biological evolution.
 * It is used to find solutions or optimize parameters for complex problems where traditional, deterministic approaches might be impractical.
 * In a genetic algorithm, a population of potential solutions is evolved over multiple generations. Each individual solution, represented as a "chromosome,"
 * undergoes processes such as selection, crossover (recombination), and mutation. These processes simulate the mechanisms of natural selection,
 * genetic recombination, and random genetic mutations.Through this iterative process, the algorithm continually refines and improves the solutions,
 * converging towards an optimal or near-optimal solution for the given problem
 *
 */

/**
 * This class implements a genetic algorithm to generate a "proof of evolution" that meets certain criteria.
 * The genetic algorithm evolves a population of proofs over generations to find a valid proof with the required number of leading zeros.
 *
 * @author Ahmet Can Erol
 */
public class BlockProofOfEvolutionGenerator {
    private static final int PROOF_LENGTH = 8; // The length of the proof
    private static final int TARGET_ZEROS = 4; // The number of leading zeros for a valid proof
    private static final int POPULATION_SIZE = 20; // The size of the population for the genethic algorithm
    private static final double MUTATION_RATE = 0.2; // The previous proof used as a basis for the new proof
    private static Long previousProof;

    public BlockProofOfEvolutionGenerator(Long previousProof) {
        this.previousProof = previousProof;
    }

    public static Long proofOfEvolution(Long previousProof) {
        List<Long> population = generateInitialPopulation();
        int generation = 0;

        while (true) {
            List<Long> nextGeneration = new ArrayList<>();
            for (Long proof : population) {
                if (isProofValid(previousProof, proof)) {
                    return proof;  // Return a valid proof
                }
            }

            population = selectBestIndividuals(population);
            nextGeneration.add(population.get(0));

            while (nextGeneration.size() < POPULATION_SIZE) {
                Long parent1 = selectRandomParent(population);
                Long parent2 = selectRandomParent(population);
                Long child = crossover(parent1, parent2);
                child = mutate(child);
                nextGeneration.add(child);
            }
            population = nextGeneration;
            generation++;
        }
    }

    // Check if the proof is valid by ensuring it has the required leading zeros

    private static boolean isProofValid(Long previousProof, Long currentProof) {
        String combinedProof = String.valueOf(previousProof) + String.valueOf(currentProof);
        String sha256 = Hashing.sha256().hashString(combinedProof, StandardCharsets.UTF_8).toString();
        return sha256.startsWith("0".repeat(TARGET_ZEROS));
    }

    // Generate a random long number
    private static long generateRandomLong() {
        Random random = new Random();
        long range = (long) Math.pow(10, PROOF_LENGTH);
        return random.nextLong() % range;
    }

    // Initialize the initial population with random proofs
    private static List<Long> generateInitialPopulation() {
        List<Long> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(generateRandomLong());
        }
        return population;
    }

    // Select the best individual based on their fitness

    private static List<Long> selectBestIndividuals(List<Long> population) {
        Long bestIndividual = Collections.max(population, (p1, p2) -> Double.compare(calculateFitness(p1), calculateFitness(p2)));
        return List.of(bestIndividual);
    }


    // Select a random parent from the population

    private static Long selectRandomParent(List<Long> population) {
        return population.get(new Random().nextInt(population.size()));
    }

    // Select a random parent from the population

    private static Long crossover(Long parent1, Long parent2) {
        String parent1Str = String.valueOf(parent1);
        String parent2Str = String.valueOf(parent2);
        int crossoverPoint = new Random().nextInt(PROOF_LENGTH);
        String childStr = parent1Str.substring(0, crossoverPoint) + parent2Str.substring(crossoverPoint);
        return Long.parseLong(childStr);
    }

    // Mutate a child by changing a digit with a certain probability

    private static Long mutate(Long child) {
        Long mutatedChild = child;
        for (int i = 0; i < PROOF_LENGTH; i++) {
            if (Math.random() < MUTATION_RATE) {
                int randomInt = new Random().nextInt(10); // Rastgele bir tamsayı al
                mutatedChild = mutateDigit(mutatedChild, i, randomInt);
            }
        }
        return mutatedChild;
    }

    // Mutate a child by changing a digit with a certain probability

    private static Long mutateDigit(Long value, int position, int newDigit) {
        long factor = (long) Math.pow(10, position);
        long oldValue = (value / factor) % 10;
        long difference = newDigit - oldValue;
        return value + (difference * factor);
    }

    // Calculate the fitness of a proof based on the number of leading zeros
    private static double calculateFitness(Long proof) {
        String combinedProof = String.valueOf(previousProof) + String.valueOf(proof);
        String sha256 = Hashing.sha256().hashString(combinedProof, StandardCharsets.UTF_8).toString();
        int leadingZeroCount = 0;
        while (leadingZeroCount < sha256.length() && sha256.charAt(leadingZeroCount) == '0') {
            leadingZeroCount++;
        }
        return leadingZeroCount;
    }
}
