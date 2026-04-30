public class Typist
{
    // Fields
    private String name;
    private char symbol;
    private int progress;
    private boolean burntOut;
    private int burnoutTurnsRemaining;
    private double accuracy;

    // Constructor
    public Typist(char typistSymbol, String typistName, double typistAccuracy)
    {
        this.symbol = typistSymbol;
        this.name = typistName;
        this.progress = 0;
        this.burntOut = false;
        this.burnoutTurnsRemaining = 0;
        setAccuracy(typistAccuracy); // ensures clamping
    }

    public void burnOut(int turns)
    {
        this.burntOut = true;
        this.burnoutTurnsRemaining = turns;
    }

    public void recoverFromBurnout()
    {
        if (burntOut)
        {
            burnoutTurnsRemaining--;

            if (burnoutTurnsRemaining <= 0)
            {
                burnoutTurnsRemaining = 0;
                burntOut = false;
            }
        }
    }

    public double getAccuracy()
    {
        return accuracy;
    }

    public int getProgress()
    {
        return progress;
    }

    public String getName()
    {
        return name;
    }

    public char getSymbol()
    {
        return symbol;
    }

    public int getBurnoutTurnsRemaining()
    {
        return burnoutTurnsRemaining;
    }

    public void resetToStart()
    {
        progress = 0;
        burntOut = false;
        burnoutTurnsRemaining = 0;
    }

    public boolean isBurntOut()
    {
        return burntOut;
    }

    public void typeCharacter()
    {
        if (!burntOut)
        {
            progress++;
        }
    }

    public void slideBack(int amount)
    {
        progress -= amount;

        if (progress < 0)
        {
            progress = 0;
        }
    }

    public void setAccuracy(double newAccuracy)
    {
        if (newAccuracy < 0.0)
        {
            accuracy = 0.0;
        }
        else if (newAccuracy > 1.0)
        {
            accuracy = 1.0;
        }
        else
        {
            accuracy = newAccuracy;
        }
    }

    public void setSymbol(char newSymbol)
    {
        this.symbol = newSymbol;
    }
}