public class TypingRaceTest
{
    public static void main(String[] args)
    {
        TypingRace race = new TypingRace(10);

        race.addTypist(new Typist('1', "FAST", 1.0), 1);
        race.addTypist(new Typist('2', "MEDIUM", 0.6), 2);
        race.addTypist(new Typist('3', "SLOW", 0.2), 3);

        race.startRace();
    }
}