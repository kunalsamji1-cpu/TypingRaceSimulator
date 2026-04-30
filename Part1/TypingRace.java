import java.util.concurrent.TimeUnit;

public class TypingRace
{
    private int passageLength;
    private Typist seat1Typist;
    private Typist seat2Typist;
    private Typist seat3Typist;

    private static final double MISTYPE_BASE_CHANCE = 0.3;
    private static final int SLIDE_BACK_AMOUNT = 2;
    private static final int BURNOUT_DURATION = 3;

    public TypingRace(int passageLength)
    {
        this.passageLength = passageLength;
    }

    public void addTypist(Typist theTypist, int seatNumber)
    {
        if (seatNumber == 1)
            seat1Typist = theTypist;
        else if (seatNumber == 2)
            seat2Typist = theTypist;
        else if (seatNumber == 3)
            seat3Typist = theTypist;
    }

    public void startRace()
    {
        boolean finished = false;

        seat1Typist.resetToStart();
        seat2Typist.resetToStart();
        seat3Typist.resetToStart();

        while (!finished)
        {
            advanceTypist(seat1Typist);
            advanceTypist(seat2Typist);
            advanceTypist(seat3Typist);

            printRace();

            if (raceFinishedBy(seat1Typist) ||
                raceFinishedBy(seat2Typist) ||
                raceFinishedBy(seat3Typist))
            {
                finished = true;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (Exception e) {}
        }

        if (raceFinishedBy(seat1Typist))
            System.out.println("Winner: " + seat1Typist.getName());
        else if (raceFinishedBy(seat2Typist))
            System.out.println("Winner: " + seat2Typist.getName());
        else
            System.out.println("Winner: " + seat3Typist.getName());
    }

    public void advanceTypist(Typist theTypist)
    {
        if (theTypist.isBurntOut())
        {
            theTypist.recoverFromBurnout();
            return;
        }

        if (Math.random() < theTypist.getAccuracy())
        {
            theTypist.typeCharacter();
        }

        if (Math.random() < (1 - theTypist.getAccuracy()) * MISTYPE_BASE_CHANCE)
        {
            theTypist.slideBack(SLIDE_BACK_AMOUNT);
        }

        if (Math.random() < 0.05 * theTypist.getAccuracy() * theTypist.getAccuracy())
        {
            theTypist.burnOut(BURNOUT_DURATION);
        }
    }

    private boolean raceFinishedBy(Typist t)
    {
        return t.getProgress() >= passageLength;
    }

    private void printRace()
    {
        System.out.print('\u000C');

        System.out.println("TYPING RACE - length: " + passageLength);

        printSeat(seat1Typist);
        printSeat(seat2Typist);
        printSeat(seat3Typist);

        System.out.println("---------------------------");
    }

    private void printSeat(Typist t)
    {
        System.out.print(t.getSymbol() + " ");

        for (int i = 0; i < t.getProgress(); i++)
        {
            System.out.print("-");
        }

        if (t.isBurntOut())
        {
            System.out.print(" [ZZ]");
        }

        System.out.println(" " + t.getName());
    }

    public static void main(String[] args)
    {
        TypingRace race = new TypingRace(40);

        race.addTypist(new Typist('1', "TURBOFINGERS", 0.85), 1);
        race.addTypist(new Typist('2', "QWERTY_QUEEN", 0.60), 2);
        race.addTypist(new Typist('3', "HUNT_N_PECK", 0.30), 3);

        race.startRace();
    }
}