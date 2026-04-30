# TypingRaceSimulator

Object Oriented Programming Project — ECS414U

---

## Project Structure

TypingRaceSimulator/
├── Part1/    # Text-based typing race simulation
├── Part2/    # GUI-based typing race simulation
└── README.md

---

## Part 1 — Textual Simulation

### Compile

cd Part1  
javac *.java  

### Run

java TypingRace  

### Tests

java TypistTest  
java TypingRaceTest  

---

## Part 2 — GUI Simulation

This part implements a full graphical typing race system using Java Swing.

### Features

- Predefined and custom passages  
- 2–6 typists supported  
- Live typing race display (TypeRacer-style)  
- Passage text displayed and progresses character-by-character  
- Completed characters highlighted  
- Difficulty modifiers:
  - Autocorrect (reduces slide-back amount)
  - Caffeine Mode (early speed boost, later burnout risk)
  - Night Shift (reduces accuracy)
- Typist customisation:
  - Name, symbol, colour
  - Typing style (Touch Typist, Hunt & Peck, etc.)
  - Keyboard type
  - Accessories (Wrist Support, Energy Drink, Headphones)
- Performance statistics:
  - Words Per Minute (WPM)
  - Accuracy %
  - Burnout count
  - Mistypes
- Reward systems:
  - Leaderboard (points, wins, titles)
  - Sponsor system (earnings and bonuses)

---

### Compile (from root folder)

javac -d out Part1/*.java Part2/*.java  

### Run GUI

java -cp out TypingRaceGUI  

---

## Dependencies

- Java Development Kit (JDK) 11 or higher  
- Java Swing (built-in, no external libraries required)  

---

## Notes

- Part 1 simulation logic is reused in Part 2 GUI  
- Starter code contained issues which were identified and fixed  
- Additional features were implemented beyond the base requirements  