# NQueensGeneticAlgorithm

A Java-based multithreaded implementation of a genetic algorithm for solving the classic **N-Queens Problem**. This project demonstrates clean object-oriented design, parallel fitness evaluation using Java's `ExecutorService`, and command-line interaction for dynamic testing. Ideal for exploring evolutionary computing techniques or as an educational showcase of multithreaded algorithms.

---

## Project Structure

```
NQueensGeneticAlgorithm/
├── src/
│   └── NQueensGeneticAlgorithm.java
└──
```

---



## Core Java Class

### `NQueensGeneticAlgorithm.java`
Handles all logic to:
- Initialize a randomized population of potential solutions
- Use a genetic algorithm (selection, crossover, mutation)
- Evaluate fitness scores in parallel using multithreading
- Output the best board solution with generation and runtime stats
- Prompt user for `N` value (4 ≤ N ≤ 20) and run multiple times

**Key Features:**
- Parallel fitness evaluation with `ExecutorService`
- Adjustable mutation rate and population size
- Maximum generation limit for termination
- Text-based board visualization

---



## How to Run

### IDE
- Open the project in an IDE like IntelliJ or Eclipse.
- Run `NQueensGeneticAlgorithm.java` as a Java application.

### Command Line
```bash
javac NQueensGeneticAlgorithm.java
java NQueensGeneticAlgorithm
```

---



## Sample Output

```
Enter the value of N (4 <= N <= 20): 8

Final solution:

 _  Q  _  _  _  _  _  _ 
 _  _  _  _  Q  _  _  _ 
 _  _  _  _  _  _  Q  _ 
 _  _  _  _  _  _  _  Q 
 _  _  Q  _  _  _  _  _ 
 _  _  _  _  _  Q  _  _ 
 Q  _  _  _  _  _  _  _ 
 _  _  _  Q  _  _  _  _ 

Generation when solution was found: 94  
Maximum fitness: 8  
Runtime: 254 milliseconds
```

---



## Future Enhancements

- Add GUI visualization using JavaFX or Swing  
- Accept genetic parameters as command-line flags  
- Log output results to a file  

---



## License
This project is licensed for personal, non-commercial use only. Redistribution, resale, or modification is prohibited without written permission from the author.  
See the [LICENSE] file for full details.



---



## Author  
**Trace Davis**  
- GitHub: [Trace0727](https://github.com/Trace0727)  
- LinkedIn: [Trace Davis](https://www.linkedin.com/in/trace-d-926380138/)
