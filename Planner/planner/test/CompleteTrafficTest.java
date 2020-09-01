package planner.test;

import planner.*;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

/**
 * Tests for the {@link Traffic} implementation class.
 */
public class CompleteTrafficTest {

    // Correct line separator for executing machine (used in toString method)
    private final static String LINE_SEPARATOR = System.getProperty(
            "line.separator");
    // locations to test with
    private Location[] locations;
    // corridors to test with
    private Corridor[] corridors;

    /**
     * This method is run by JUnit before each test to initialise instance
     * variables locations and corridors.
     */
    @Before
    public void setUp() throws Exception {
        // locations to test with
        locations = new Location[7];
        locations[0] = new Location("l0");
        locations[1] = new Location("l1");
        locations[2] = new Location("l2");
        locations[3] = new Location("l3");
        locations[4] = new Location("l4");
        locations[5] = new Location("l5");
        locations[6] = new Location("l6");

        // corridors to test with
        corridors = new Corridor[5];
        corridors[0] = new Corridor(locations[0], locations[1], 100);
        corridors[1] = new Corridor(locations[1], locations[2], 200);
        corridors[2] = new Corridor(locations[2], locations[3], 300);
        corridors[3] = new Corridor(locations[3], locations[4], 400);
        corridors[4] = new Corridor(locations[4], locations[5], 500);
    }

    /**
     * Test from handout:
     * 
     * Basic test of the empty constructor of the class.
     */
    @Test(timeout = 5000)
    public void testEmptyConstructor() throws Exception {
        // the Traffic object under test
        Traffic traffic = new Traffic();

        // expected results
        Set<Corridor> expectedCorridorsWithTraffic = new HashSet<>();
        String expectedString = "";

        // check the traffic on a non-null corridor
        Assert.assertEquals(0, traffic.getTraffic(corridors[0]));
        // check that there are no corridors with traffic
        Assert.assertEquals(expectedCorridorsWithTraffic, traffic
                .getCorridorsWithTraffic());
        // check the string representation
        Assert.assertEquals(expectedString, traffic.toString());
        // check that the class invariant holds
        Assert.assertTrue(traffic.checkInvariant());
    }

    /**
     * Test from handout:
     * 
     * Basic test of the updateTraffic method.
     */
    @Test(timeout = 5000)
    public void testUpdateTraffic1() throws Exception {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        traffic.updateTraffic(corridors[0], 10);
        traffic.updateTraffic(corridors[1], 50);
        traffic.updateTraffic(corridors[0], 20);
        traffic.updateTraffic(corridors[1], -10);

        // expected results
        Set<Corridor> expectedCorridorsWithTraffic = new HashSet<>();
        expectedCorridorsWithTraffic.add(corridors[0]);
        expectedCorridorsWithTraffic.add(corridors[1]);
        String expectedString = "Corridor l0 to l1 (100): 30" + LINE_SEPARATOR
                + "Corridor l1 to l2 (200): 40" + LINE_SEPARATOR;

        // check the traffic on some corridors
        Assert.assertEquals(30, traffic.getTraffic(corridors[0]));
        Assert.assertEquals(40, traffic.getTraffic(corridors[1]));
        Assert.assertEquals(0, traffic.getTraffic(corridors[2]));
        // check the corridors with traffic
        Assert.assertEquals(expectedCorridorsWithTraffic, traffic
                .getCorridorsWithTraffic());
        // check the string representation
        Assert.assertEquals(expectedString, traffic.toString());
        // check that the class invariant holds
        Assert.assertTrue(traffic.checkInvariant());
    }

    /**
     * Additional test of the updateTraffic method: check that corridors can
     * have their traffic set back to zero, and also set to 0.
     */
    @Test(timeout = 5000)
    public void testUpdateTraffic2() throws Exception {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        traffic.updateTraffic(corridors[1], 50);
        traffic.updateTraffic(corridors[0], 10);
        traffic.updateTraffic(corridors[2], 30);
        traffic.updateTraffic(corridors[0], 20);
        traffic.updateTraffic(corridors[1], -50);
        traffic.updateTraffic(corridors[1], 30);
        traffic.updateTraffic(corridors[1], 10);
        traffic.updateTraffic(corridors[2], -30);
        traffic.updateTraffic(corridors[3], 0);

        // expected results
        Set<Corridor> expectedCorridorsWithTraffic = new HashSet<>();
        expectedCorridorsWithTraffic.add(corridors[0]);
        expectedCorridorsWithTraffic.add(corridors[1]);
        String expectedString = "Corridor l0 to l1 (100): 30" + LINE_SEPARATOR
                + "Corridor l1 to l2 (200): 40" + LINE_SEPARATOR;

        // check the traffic on some corridors
        Assert.assertEquals(30, traffic.getTraffic(corridors[0]));
        Assert.assertEquals(40, traffic.getTraffic(corridors[1]));
        Assert.assertEquals(0, traffic.getTraffic(corridors[2]));
        Assert.assertEquals(0, traffic.getTraffic(corridors[3]));
        Assert.assertEquals(0, traffic.getTraffic(corridors[4]));
        // check the corridors with traffic
        Assert.assertEquals(expectedCorridorsWithTraffic, traffic
                .getCorridorsWithTraffic());
        // check the string representation
        Assert.assertEquals(expectedString, traffic.toString());
        // check that the class invariant holds
        Assert.assertTrue(traffic.checkInvariant());
    }

    /**
     * Augmented test from handout:
     * 
     * Check that the appropriate exception is thrown if a call to updateTraffic
     * would result in a negative amount of traffic on a corridor.
     */
    @Test(timeout = 5000)
    public void testUpdateTrafficInvalidTrafficException() throws Exception {
        // check subtraction of a negative amount from 0 traffic

        // the Traffic object under test
        Traffic traffic = new Traffic();
        try {
            traffic.updateTraffic(corridors[0], -10);
            Assert.fail("InvalidTrafficException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("InvalidTrafficException not thrown",
                    e instanceof InvalidTrafficException);
        }
        // check subtraction of a negative amount from positive traffic
        traffic = new Traffic();
        traffic.updateTraffic(corridors[0], 5);
        try {
            traffic.updateTraffic(corridors[0], -10);
            Assert.fail("InvalidTrafficException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("InvalidTrafficException not thrown",
                    e instanceof InvalidTrafficException);
        }
    }

    /**
     * Test from handout:
     * 
     * Basic test of the addTraffic method.
     */
    @Test(timeout = 5000)
    public void testAddTraffic1() throws Exception {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        traffic.updateTraffic(corridors[0], 10);
        traffic.updateTraffic(corridors[1], 30);

        // the extra traffic to add
        Traffic extraTraffic = new Traffic();
        extraTraffic.updateTraffic(corridors[1], 40);

        traffic.addTraffic(extraTraffic);

        // expected results
        Set<Corridor> expectedCorridorsWithTraffic = new HashSet<>();
        expectedCorridorsWithTraffic.add(corridors[0]);
        expectedCorridorsWithTraffic.add(corridors[1]);
        String expectedString = "Corridor l0 to l1 (100): 10" + LINE_SEPARATOR
                + "Corridor l1 to l2 (200): 70" + LINE_SEPARATOR;

        // check the traffic on some corridors
        Assert.assertEquals(10, traffic.getTraffic(corridors[0]));
        Assert.assertEquals(70, traffic.getTraffic(corridors[1]));
        Assert.assertEquals(0, traffic.getTraffic(corridors[2]));
        // check the corridors with traffic
        Assert.assertEquals(expectedCorridorsWithTraffic, traffic
                .getCorridorsWithTraffic());
        // check the string representation
        Assert.assertEquals(expectedString, traffic.toString());
        // check that the class invariant holds
        Assert.assertTrue(traffic.checkInvariant());
    }

    /**
     * Additional test of the addTraffic method:
     * 
     * - extraTraffic contains corridors with more than 1 person on them that
     * currently have no people on them in traffic.
     * 
     * - check independence of traffic and extraTraffic.
     */
    @Test(timeout = 5000)
    public void testAddTraffic2() throws Exception {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        traffic.updateTraffic(corridors[0], 10);
        traffic.updateTraffic(corridors[1], 30);

        // the extra traffic to add
        Traffic extraTraffic = new Traffic();
        extraTraffic.updateTraffic(corridors[1], 40);
        extraTraffic.updateTraffic(corridors[2], 20);

        traffic.addTraffic(extraTraffic);

        // update extraTraffic -- should not affect traffic
        extraTraffic.updateTraffic(corridors[3], 30);
        // update traffic -- should not affect extraTraffic
        traffic.updateTraffic(corridors[2], 5);

        // expected results of traffic
        Set<Corridor> expectedCorridorsWithTraffic = new HashSet<>();
        expectedCorridorsWithTraffic.add(corridors[0]);
        expectedCorridorsWithTraffic.add(corridors[1]);
        expectedCorridorsWithTraffic.add(corridors[2]);
        String expectedString = "Corridor l0 to l1 (100): 10" + LINE_SEPARATOR
                + "Corridor l1 to l2 (200): 70" + LINE_SEPARATOR
                + "Corridor l2 to l3 (300): 25" + LINE_SEPARATOR;
        // check the traffic on some corridors
        Assert.assertEquals(10, traffic.getTraffic(corridors[0]));
        Assert.assertEquals(70, traffic.getTraffic(corridors[1]));
        Assert.assertEquals(25, traffic.getTraffic(corridors[2]));
        Assert.assertEquals(0, traffic.getTraffic(corridors[3]));
        // check the corridors with traffic
        Assert.assertEquals(expectedCorridorsWithTraffic, traffic
                .getCorridorsWithTraffic());
        // check the string representation
        Assert.assertEquals(expectedString, traffic.toString());
        // check that the class invariant holds
        Assert.assertTrue(traffic.checkInvariant());

        // expected results of extraTraffic
        expectedCorridorsWithTraffic = new HashSet<>();
        expectedCorridorsWithTraffic.add(corridors[1]);
        expectedCorridorsWithTraffic.add(corridors[2]);
        expectedCorridorsWithTraffic.add(corridors[3]);
        expectedString = "Corridor l1 to l2 (200): 40" + LINE_SEPARATOR
                + "Corridor l2 to l3 (300): 20" + LINE_SEPARATOR
                + "Corridor l3 to l4 (400): 30" + LINE_SEPARATOR;
        // check the traffic on some corridors
        Assert.assertEquals(0, extraTraffic.getTraffic(corridors[0]));
        Assert.assertEquals(40, extraTraffic.getTraffic(corridors[1]));
        Assert.assertEquals(20, extraTraffic.getTraffic(corridors[2]));
        Assert.assertEquals(30, extraTraffic.getTraffic(corridors[3]));
        // check the corridors with traffic
        Assert.assertEquals(expectedCorridorsWithTraffic, extraTraffic
                .getCorridorsWithTraffic());
        // check the string representation
        Assert.assertEquals(expectedString, extraTraffic.toString());
        // check that the class invariant holds
        Assert.assertTrue(extraTraffic.checkInvariant());
    }

    /**
     * Test from handout:
     * 
     * Basic test of the copy constructor.
     */
    @Test(timeout = 5000)
    public void testCopyConstructor1() throws Exception {
        // the Traffic object that will be copied
        Traffic initialTraffic = new Traffic();
        initialTraffic.updateTraffic(corridors[0], 10);
        initialTraffic.updateTraffic(corridors[1], 70);
        initialTraffic.updateTraffic(corridors[2], 20);

        // the Traffic object under test
        Traffic traffic = new Traffic(initialTraffic);

        // expected results
        Set<Corridor> expectedCorridorsWithTraffic = new HashSet<>();
        expectedCorridorsWithTraffic.add(corridors[0]);
        expectedCorridorsWithTraffic.add(corridors[1]);
        expectedCorridorsWithTraffic.add(corridors[2]);
        String expectedString = "Corridor l0 to l1 (100): 10" + LINE_SEPARATOR
                + "Corridor l1 to l2 (200): 70" + LINE_SEPARATOR
                + "Corridor l2 to l3 (300): 20" + LINE_SEPARATOR;

        // check the traffic on some corridors
        Assert.assertEquals(10, traffic.getTraffic(corridors[0]));
        Assert.assertEquals(70, traffic.getTraffic(corridors[1]));
        Assert.assertEquals(20, traffic.getTraffic(corridors[2]));
        Assert.assertEquals(0, traffic.getTraffic(corridors[3]));
        // check the corridors with traffic
        Assert.assertEquals(expectedCorridorsWithTraffic, traffic
                .getCorridorsWithTraffic());
        // check the string representation
        Assert.assertEquals(expectedString, traffic.toString());
        // check that the class invariant holds
        Assert.assertTrue(traffic.checkInvariant());
    }

    /**
     * Test of the copy constructor: check for independence of parameter and new
     * instance.
     */
    @Test(timeout = 5000)
    public void testCopyConstructor2() throws Exception {
        // the Traffic object that will be copied
        Traffic initialTraffic = new Traffic();
        initialTraffic.updateTraffic(corridors[0], 10);
        initialTraffic.updateTraffic(corridors[1], 70);
        initialTraffic.updateTraffic(corridors[2], 20);

        // the Traffic object under test
        Traffic traffic = new Traffic(initialTraffic);

        // modifications to initialTraffic should not change traffic
        initialTraffic.updateTraffic(corridors[0], 5);

        // modifications to traffic should not change initialTraffic
        traffic.updateTraffic(corridors[1], -10);

        // expected results for traffic
        Set<Corridor> expectedCorridorsWithTraffic = new HashSet<>();
        expectedCorridorsWithTraffic.add(corridors[0]);
        expectedCorridorsWithTraffic.add(corridors[1]);
        expectedCorridorsWithTraffic.add(corridors[2]);
        String expectedString = "Corridor l0 to l1 (100): 10" + LINE_SEPARATOR
                + "Corridor l1 to l2 (200): 60" + LINE_SEPARATOR
                + "Corridor l2 to l3 (300): 20" + LINE_SEPARATOR;
        // check the traffic on some corridors
        Assert.assertEquals(10, traffic.getTraffic(corridors[0]));
        Assert.assertEquals(60, traffic.getTraffic(corridors[1]));
        Assert.assertEquals(20, traffic.getTraffic(corridors[2]));
        Assert.assertEquals(0, traffic.getTraffic(corridors[3]));
        // check the corridors with traffic
        Assert.assertEquals(expectedCorridorsWithTraffic, traffic
                .getCorridorsWithTraffic());
        // check the string representation
        Assert.assertEquals(expectedString, traffic.toString());
        // check that the class invariant holds
        Assert.assertTrue(traffic.checkInvariant());

        // expected results for initialTraffic
        expectedCorridorsWithTraffic = new HashSet<>();
        expectedCorridorsWithTraffic.add(corridors[0]);
        expectedCorridorsWithTraffic.add(corridors[1]);
        expectedCorridorsWithTraffic.add(corridors[2]);
        expectedString = "Corridor l0 to l1 (100): 15" + LINE_SEPARATOR
                + "Corridor l1 to l2 (200): 70" + LINE_SEPARATOR
                + "Corridor l2 to l3 (300): 20" + LINE_SEPARATOR;
        // check the traffic on some corridors
        Assert.assertEquals(15, initialTraffic.getTraffic(corridors[0]));
        Assert.assertEquals(70, initialTraffic.getTraffic(corridors[1]));
        Assert.assertEquals(20, initialTraffic.getTraffic(corridors[2]));
        Assert.assertEquals(0, initialTraffic.getTraffic(corridors[3]));
        // check the corridors with traffic
        Assert.assertEquals(expectedCorridorsWithTraffic, initialTraffic
                .getCorridorsWithTraffic());
        // check the string representation
        Assert.assertEquals(expectedString, initialTraffic.toString());
        // check that the class invariant holds
        Assert.assertTrue(initialTraffic.checkInvariant());
    }

    @Test(timeout = 5000)
    public void testSameTrafficMethod() throws Exception {
        testSameTrafficMethodHandout();
        testSameTrafficMethodMore1();
        testSameTrafficMethodMore2();
        testSameTrafficMethodMore3();
    }

    /**
     * Test from handout:
     * 
     * Basic test for the sameTraffic method.
     **/
    public void testSameTrafficMethodHandout() throws Exception {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        traffic.updateTraffic(corridors[0], 10);
        traffic.updateTraffic(corridors[1], 20);

        // the Traffic object to compare
        Traffic other = new Traffic();
        other.updateTraffic(corridors[0], 10);

        // check that the objects are not currently the same
        Assert.assertFalse(traffic.sameTraffic(other));

        // update other so that they are now the same and check
        other.updateTraffic(corridors[1], 20);
        Assert.assertTrue(traffic.sameTraffic(other));
    }

    /**
     * More tests for the sameTraffic method: traffic not the same: same
     * corridors, different traffic on them
     **/
    public void testSameTrafficMethodMore1() throws Exception {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        traffic.updateTraffic(corridors[0], 10);
        traffic.updateTraffic(corridors[1], 20);

        // the Traffic object to compare
        Traffic other = new Traffic();
        other.updateTraffic(corridors[0], 5);
        other.updateTraffic(corridors[1], 25);

        // check that the objects are not the same
        Assert.assertFalse(traffic.sameTraffic(other));
    }

    /**
     * More tests for the sameTraffic method: traffic not the same: some
     * different corridors
     **/
    public void testSameTrafficMethodMore2() throws Exception {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        traffic.updateTraffic(corridors[0], 10);
        traffic.updateTraffic(corridors[1], 20);

        // the Traffic object to compare
        Traffic other = new Traffic();
        other.updateTraffic(corridors[1], 20);
        other.updateTraffic(corridors[2], 10);

        // check that the objects are not the same
        Assert.assertFalse(traffic.sameTraffic(other));
    }

    /**
     * More tests for the sameTraffic method: traffic the same: same corridors
     * with same traffic.
     **/
    public void testSameTrafficMethodMore3() throws Exception {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        traffic.updateTraffic(corridors[0], 10);
        traffic.updateTraffic(corridors[1], 20);

        // the Traffic object to compare
        Traffic other = new Traffic();
        other.updateTraffic(corridors[1], 20);
        other.updateTraffic(corridors[0], 5);
        other.updateTraffic(corridors[2], 0);
        other.updateTraffic(corridors[0], 5);

        // check that the objects are the same
        Assert.assertTrue(traffic.sameTraffic(other));
    }

    /**
     * Combined tests from handout:
     * 
     * Check the the appropriate NullPointerException is thrown if null
     * parameters are passed to the methods.
     */
    @Test(timeout = 5000)
    public void testNullArguments() throws Exception {
        testUpdateTrafficNullPointer();
        testAddNullTraffic();
        testCopyConstructorNullArgument();
        testSameTrafficMethodNullParameter();
    }

    /**
     * 
     * Test from handout:
     * 
     * Check that the appropriate exception is thrown if updateTraffic is called
     * with a null corridor.
     */
    public void testUpdateTrafficNullPointer() throws Exception {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        try {
            traffic.updateTraffic(null, 20);
            Assert.fail("NullPointerException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("NullPointerException not thrown",
                    e instanceof NullPointerException);
        }
    }

    /**
     * Test from handout:
     * 
     * Check that the appropriate exception is thrown if a call to addTraffic is
     * passed a null parameter.
     */
    public void testAddNullTraffic() throws Exception {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        try {
            traffic.addTraffic(null);
            Assert.fail("NullPointerException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("NullPointerException not thrown",
                    e instanceof NullPointerException);
        }
    }

    /**
     * Test from handout:
     * 
     * Check that the appropriate exception is thrown when a new instance of the
     * class Traffic is constructed with a null argument.
     */
    public void testCopyConstructorNullArgument() throws Exception {
        try {
            // the Traffic object under test
            new Traffic(null);
            Assert.fail("NullPointerException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("NullPointerException not thrown",
                    e instanceof NullPointerException);
        }
    }

    /**
     * Test from handout:
     * 
     * Test that the sameTraffic method throws the appropriate exception when
     * the parameter is null
     */
    public void testSameTrafficMethodNullParameter() throws Exception {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        traffic.updateTraffic(corridors[0], 10);
        traffic.updateTraffic(corridors[1], 20);
        try {
            traffic.sameTraffic(null);
            Assert.fail("NullPointerException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("NullPointerException not thrown",
                    e instanceof NullPointerException);
        }
    }

}
