public class TypistTest
{
    public static void main(String[] args)
    {
        Typist t = new Typist('A', "TEST_TYPIST", 0.5);

        System.out.println("Test 1: typeCharacter moves progress forward");
        t.typeCharacter();
        System.out.println("Expected: 1");
        System.out.println("Actual: " + t.getProgress());

        System.out.println("\nTest 2: slideBack cannot go below zero");
        t.slideBack(10);
        System.out.println("Expected: 0");
        System.out.println("Actual: " + t.getProgress());

        System.out.println("\nTest 3: burnout counts down and clears");
        t.burnOut(2);
        System.out.println("Burnt out? " + t.isBurntOut());
        t.recoverFromBurnout();
        System.out.println("Turns left after 1 recover: " + t.getBurnoutTurnsRemaining());
        t.recoverFromBurnout();
        System.out.println("Burnt out after 2 recovers? " + t.isBurntOut());

        System.out.println("\nTest 4: accuracy clamps below 0");
        t.setAccuracy(-5.0);
        System.out.println("Expected: 0.0");
        System.out.println("Actual: " + t.getAccuracy());

        System.out.println("\nTest 5: accuracy clamps above 1");
        t.setAccuracy(2.0);
        System.out.println("Expected: 1.0");
        System.out.println("Actual: " + t.getAccuracy());

        System.out.println("\nTest 6: resetToStart clears progress and burnout");
        t.typeCharacter();
        t.burnOut(3);
        t.resetToStart();
        System.out.println("Progress expected: 0, actual: " + t.getProgress());
        System.out.println("Burnt out expected: false, actual: " + t.isBurntOut());
        System.out.println("Burnout turns expected: 0, actual: " + t.getBurnoutTurnsRemaining());
    }
}