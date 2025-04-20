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
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void solve() {
        initializePopulation();
        int generation = 0;
        int maxFitness = -1;
        int[] bestSolution = null;
        long startTime = System.currentTimeMillis();

        while (maxFitness != N && generation < 1000) {
            int[][] newPopulation = new int[populationSize][N];
            List<Future<Integer>> fitnessFutures = new ArrayList<>();
            for (int i = 0; i < populationSize; i++) {
                int[] solution = population[i];
                Future<Integer> future = executor.submit(() -> calculateFitness(solution));
                fitnessFutures.add(future);
            }

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

            // Selection, crossover, and mutation
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

    private void initializePopulation() {
        population = new int[populationSize][N];
        for (int i = 0; i < populationSize; i++) {
            int queensPlaced = random.nextInt(N) + 1; // Place 1 to N queens randomly
            for (int j = 0; j < queensPlaced; j++) {
                int position = random.nextInt(N);
                population[i][position] = 1; // Place queen at random position
            }
        }
    }

    private int[] selectParent() {
        return population[random.nextInt(populationSize)];
    }

    private int[] crossover(int[] parent1, int[] parent2) {
        int[] child = new int[N];
        int crossoverPoint = random.nextInt(N);

        System.arraycopy(parent1, 0, child, 0, crossoverPoint);
        System.arraycopy(parent2, crossoverPoint, child, crossoverPoint, N - crossoverPoint);

        return child;
    }

    private void mutate(int[] child) {
        for (int i = 0; i < N; i++) {
            if (random.nextDouble() < mutationRate) {
                child[i] = random.nextInt(N);
            }
        }
    }

    private int calculateFitness(int[] solution) {
        int conflicts = 0;
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (solution[i] == solution[j] || Math.abs(solution[i] - solution[j]) == Math.abs(i - j)) {
                    conflicts++;
                }
            }
        }
        return N - conflicts;
    }

    private void displaySolution(int[] solution) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(solution[j] == i ? " Q " : " _ ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueExecution = true;
        while (continueExecution) {
            System.out.print("Enter the value of N (4 <= N <= 20): ");
            int N = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
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
