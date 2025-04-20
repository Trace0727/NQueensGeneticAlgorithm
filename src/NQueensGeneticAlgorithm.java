import java.util.*;
import java.util.concurrent.*;

public class NQueensGeneticAlgorithm {
    private int N;
    private int populationSize = 1000;
    private double mutationRate = 0.1;
    private int[][] population;
    private Random random = new Random();
    private ExecutorService executor;

    public NQueensGeneticAlgorithm(int N) {
        this.N = N;
        // Use available cores to parallelize fitness evaluation
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void solve() {
        initializePopulation();
        int generation = 0;
        int maxFitness = -1;
        int[] bestSolution = null;
        long startTime = System.currentTimeMillis();

        // Keep evolving until a perfect solution is found or max generations reached
        while (maxFitness != N && generation < 1000) {
            int[][] newPopulation = new int[populationSize][N];
            List<Future<Integer>> fitnessFutures = new ArrayList<>();

            // Parallel fitness evaluation for each chromosome
            for (int i = 0; i < populationSize; i++) {
                int[] solution = population[i];
                Future<Integer> future = executor.submit(() -> calculateFitness(solution));
                fitnessFutures.add(future);
            }

            // Track best solution in this generation
            for (int i = 0; i < populationSize; i++) {
                try {
                    int fitness = fitnessFutures.get(i).get();
                    if (fitness == N) {
                        maxFitness = N;
                        bestSolution = Arrays.copyOf(population[i], N);
                        break;
                    }
                    if (fitness > maxFitness) {
                        maxFitness = fitness;
                        bestSolution = Arrays.copyOf(population[i], N);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            if (maxFitness == N) break;

            // Create new population using genetic operators
            for (int i = 0; i < populationSize; i++) {
                int[] parent1 = selectParent();
                int[] parent2 = selectParent();
                int[] child = crossover(parent1, parent2);
                mutate(child);
                newPopulation[i] = child;
            }

            population = newPopulation;
            generation++;
        }

        executor.shutdown();
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;

        System.out.println("Final solution:\n");
        displaySolution(bestSolution);
        System.out.println("\nGeneration when solution was found: " + generation);
        System.out.println("Maximum fitness: " + maxFitness);
        System.out.println("Runtime: " + runTime + " milliseconds");
    }

    // Generates random permutations of rows for each column — valid initial queens
    private void initializePopulation() {
        population = new int[populationSize][N];
        for (int i = 0; i < populationSize; i++) {
            List<Integer> rows = new ArrayList<>();
            for (int j = 0; j < N; j++) {
                rows.add(j);
            }
            Collections.shuffle(rows);
            for (int j = 0; j < N; j++) {
                population[i][j] = rows.get(j); // Each index = column, value = row
            }
        }
    }

    // Random parent selection (can replace with tournament or roulette if needed)
    private int[] selectParent() {
        return population[random.nextInt(populationSize)];
    }

    // Crossover with duplicate avoidance (PMX-style) to preserve valid permutations
    private int[] crossover(int[] parent1, int[] parent2) {
        int[] child = new int[N];
        boolean[] used = new boolean[N];
        int crossoverPoint = random.nextInt(N);

        // Copy prefix from parent1
        for (int i = 0; i < crossoverPoint; i++) {
            child[i] = parent1[i];
            used[parent1[i]] = true;
        }

        // Fill remaining with values from parent2 avoiding duplicates
        int index = crossoverPoint;
        for (int i = 0; i < N; i++) {
            int val = parent2[i];
            if (!used[val]) {
                child[index++] = val;
                used[val] = true;
            }
        }

        // Fill remaining unused values if any spots are left
        for (int i = 0; i < N; i++) {
            if (!used[i]) {
                child[index++] = i;
            }
        }

        return child;
    }

    // Mutation by swapping two columns (maintains valid permutation)
    private void mutate(int[] child) {
        if (random.nextDouble() < mutationRate) {
            int i = random.nextInt(N);
            int j = random.nextInt(N);
            int temp = child[i];
            child[i] = child[j];
            child[j] = temp;
        }
    }

    // Fitness = N - number of diagonal conflicts (rows and columns are guaranteed unique)
    private int calculateFitness(int[] solution) {
        int conflicts = 0;
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                // Diagonal attack check
                if (Math.abs(solution[i] - solution[j]) == Math.abs(i - j)) {
                    conflicts++;
                }
            }
        }
        return N - conflicts; // Higher fitness means fewer conflicts
    }

    // Pretty-prints the board with Qs
    private void displaySolution(int[] solution) {
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                System.out.print(solution[col] == row ? " Q " : " _ ");
            }
            System.out.println();
        }
    }

    // Entry point: prompts user to repeatedly test different N values
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueExecution = true;
        while (continueExecution) {
            System.out.print("Enter the value of N (4 <= N <= 20): ");
            int N = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (N >= 4 && N <= 20) {
                NQueensGeneticAlgorithm ga = new NQueensGeneticAlgorithm(N);
                ga.solve();
            } else {
                System.out.println("Invalid value of N. Please enter a value between 4 and 20.");
            }
            System.out.print("Do you want to continue? (yes/no): ");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("yes")) {
                continueExecution = false;
            }
        }
        scanner.close();
    }
}
