# TypingRaceSimulator

Object Oriented Programming Project — ECS414U

## Project Structure

```
TypingRaceSimulator/
├── Part1/    # Textual simulation (Java, command-line)
└── Part2/    # GUI simulation (to be completed)
```

---

## Part 1 — Textual Simulation

### How to compile

cd Part1  
javac *.java

### How to run

The race is started by calling `startRace()` on a `TypingRace` object.

A `main` method has already been added to `TypingRace` for testing:

public static void main(String[] args) {
    TypingRace race = new TypingRace(40);
    race.addTypist(new Typist('1', "TURBOFINGERS", 0.85), 1);
    race.addTypist(new Typist('2', "QWERTY_QUEEN", 0.60), 2);
    race.addTypist(new Typist('3', "HUNT_N_PECK", 0.30), 3);
    race.startRace();
}

Run using:

java TypingRace

---

### Running Tests

java TypistTest  
java TypingRaceTest

---

## Part 2 — GUI Simulation

Not implemented yet. GUI functionality will be added in the Part2 folder and started using the `startRaceGUI()` method.

---

## Dependencies

- Java Development Kit (JDK) 11 or higher  
- No external libraries required for Part 1  
- Part 2 may use Java Swing or JavaFX  

---

## Notes

- All code compiles and runs using standard command-line tools  
- No IDE-specific configuration is required  
- Starter code contained issues which have been identified and fixed as part of the coursework  