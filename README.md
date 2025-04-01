# 🎰 Scratch Game CLI

A lightweight, fully testable CLI-based scratch game written in **pure Java** (JDK 17+).  
No Spring, no frameworks — just Java, Jackson, SLF4J, and solid testing.
I built this as a take-home challenge to show I can write clean, maintainable code without the crutches of Spring.

---

## 🚀 How to Run

### Build the Executable JAR

```bash
mvn clean package
```

This will create:

```
target/scratch-game-cli-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Run the Game

```bash
java -jar target/scratch-game-cli-1.0-SNAPSHOT-jar-with-dependencies.jar \
  --config path/to/config.json \
  --betting-amount 100
```

---

## 📄 Input Configuration

The game is driven by a JSON config that describes:

- Matrix size (`columns`, `rows`)
- Standard and bonus symbols
- Per-cell probabilities for each symbol
- Win conditions (repeat count or covered positions)

🔧 See `src/main/resources/config.json` for an example.

---

## 🧠 Example Playthrough

### Input Matrix:
```json
[
  [ "F", "F", "F" ],
  [ "E", "E", "C" ],
  [ "+1000", "E", "10x" ]
]
```

### Config:
- F = 1×
- E = 1.2×
- `same_symbol_3_times` and `same_symbols_horizontally` give x1 and x2 respectively
- Bonus `+1000` adds 1000
- Bonus `10x` multiplies result by 10

### Reward Calculation:

```
F: 100 * 1 (F) * 1 (3 times) * 2 (horizontal) = 200
E: 100 * 1.2 * 1 (3 times) = 120
Total before bonus: 320
With 10x and +1000: (320 * 10) + 1000 = 4200
```

🎉 Final reward: `4200`

---

## 📦 Output Format

```json
{
  "matrix": [
    ["F", "F", "F"],
    ["E", "E", "C"],
    ["+1000", "E", "10x"]
  ],
  "reward": 4200.0,
  "appliedWinningCombinations": {
    "F": ["same_symbol_3_times", "same_symbols_horizontally"],
    "E": ["same_symbol_3_times"]
  },
  "appliedBonusSymbols": ["+1000", "10x"]
}
```

---

## 🧪 Testing

Uses `JUnit 5` and `Mockito` for complete test coverage:

- ✅ RewardCalculator (combos, multipliers, edge cases)
- ✅ BonusApplier (add/multiply/ignore logic)
- ✅ MatrixGenerator (probabilistic generation)
- ✅ CLI interaction (Main class argument parsing)

Most logic is tested using `@ParameterizedTest + @MethodSource`.

```java
@ParameterizedTest
@MethodSource("provideMatrixAndExpectedReward")
void shouldCalculateRewardCorrectly(TestCase testCase) {
    // Arrange & Act & Assert
}
```

---

## 📚 Project Structure

- `MatrixGenerator` – builds the symbol matrix based on probabilities
- `RewardCalculator` – applies winning conditions to calculate base reward
- `BonusApplier` – applies additional logic for bonus symbols
- `GameResult` – aggregates output for user
- `Main` – handles CLI arguments and invokes components
- `config.json` – externalized config file for extensibility

---

## ⚙ Dependencies

| Library         | Purpose              |
|----------------|----------------------|
| Jackson         | JSON (de)serialization |
| JUnit 5         | Testing              |
| Mockito         | Mocking              |
| SLF4J + Logback | Logging              |

No frameworks. No magic. All testable.

---

## 👨‍💻 Author

**Babich Ilia**  
Senior Java Developer
