package planner.test;

import planner.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link Corridor} implementation class.
 * 
 */
public class CompleteCorridorTest {

    /**
     * Test construction of a typical corridor.
     */
    @Test(timeout = 5000)
    public void testTypicalCorridor() throws Exception {
        // parameters of the corridor under test
        Location start = new Location("l1");
        Location end = new Location("l2");
        int capacity = 200;

        // the corridor under test
        Corridor corridor = new Corridor(start, end, capacity);

        // expected results
        String expectedString = "Corridor l1 to l2 (200)";

        // check the accessor methods
        Assert.assertEquals(start, corridor.getStart());
        Assert.assertEquals(end, corridor.getEnd());
        Assert.assertEquals(capacity, corridor.getCapacity());
        Assert.assertEquals(expectedString, corridor.toString());
        Assert.assertTrue(corridor.checkInvariant());
    }

    /**
     * Check that the appropriate exception is thrown if a corridor is created
     * with either a null start or end location.
     */
    @Test(timeout = 5000)
    public void testNullStartEndLocation() throws Exception {
        // parameters of the corridor under test
        Location start = new Location("l1");
        Location end = new Location("l2");
        int capacity = 200;

        // check that start parameter cannot be null
        try {
            new Corridor(null, end, capacity);
            Assert.fail("NullPointerException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("NullPointerException not thrown",
                    e instanceof NullPointerException);
        }
        // check that end parameter cannot be null
        try {
            new Corridor(start, null, capacity);
            Assert.fail("NullPointerException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("NullPointerException not thrown",
                    e instanceof NullPointerException);
        }
    }

    /**
     * Check that the appropriate exception is thrown if a corridor is created
     * with an invalid capacity (one which is less than or equal to zero).
     */
    @Test(timeout = 5000)
    public void testInvalidCapacity() throws Exception {
        // parameters of the corridor under test
        Location start = new Location("l1");
        Location end = new Location("l2");

        // check a zero capacity
        try {
            new Corridor(start, end, 0);
            Assert.fail("IllegalArgumentException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("IllegalArgumentException not thrown",
                    e instanceof IllegalArgumentException);
        }
        // check a negative capacity
        try {
            new Corridor(start, end, -10);
            Assert.fail("IllegalArgumentException not thrown");
        } catch (Exception e) {
            Assert.assertTrue("IllegalArgumentException not thrown",
                    e instanceof IllegalArgumentException);
        }
    }

    /**
     * Check that the appropriate exception is thrown if a corridor is created
     * with equivalent start and end locations.
     */
    @Test(timeout = 5000, expected = IllegalArgumentException.class)
    public void testEqualLocations() throws Exception {
        // parameters of the corridor under test
        Location start = new Location("l1");
        Location end = new Location("l1");
        int capacity = 200;
        // the corridor under test
        new Corridor(start, end, capacity);
    }

    /** Test of the equals method */
    @Test(timeout = 5000)
    public void testEquals() throws Exception {
        // corridors under test
        Corridor[] corridors = new Corridor[6];
        corridors[0] = new Corridor(new Location(new String("l1")),
                new Location(new String("l2")), 10);
        corridors[1] = new Corridor(new Location(new String("l1")),
                new Location(new String("l2")), 10);
        corridors[2] = new Corridor(new Location(new String("l2")),
                new Location(new String("l3")), 5);
        corridors[3] = new Corridor(new Location(new String("l3")),
                new Location(new String("l2")), 10);
        corridors[4] = new Corridor(new Location(new String("l1")),
                new Location(new String("l3")), 10);
        corridors[5] = new Corridor(new Location(new String("l1")),
                new Location(new String("l2")), 5);

        // test equal corridors
        Assert.assertTrue(corridors[0].equals(corridors[1]));
        // hash codes for equal objects should be equal
        Assert.assertEquals(corridors[0].hashCode(), corridors[1].hashCode());

        // test unequal corridors
        Assert.assertFalse(corridors[0].equals(corridors[2])); // unequal all
        Assert.assertFalse(corridors[0].equals(corridors[3])); // unequal start
        Assert.assertFalse(corridors[0].equals(corridors[4])); // unequal end
        Assert.assertFalse(corridors[0].equals(corridors[5])); // unequal cap.
        Assert.assertFalse(corridors[0].equals(null)); // null case
        Assert.assertFalse(corridors[0].equals("Wrong type")); // wrong type
    }

    /** Test of the comparison method */
    @Test(timeout = 5000)
    public void testCompareTo() throws Exception {
        // corridors under test
        Corridor[] corridors = new Corridor[6];
        corridors[0] = new Corridor(new Location(new String("aa")),
                new Location(new String("cc")), 10);
        corridors[1] = new Corridor(new Location(new String("aa")),
                new Location(new String("cc")), 10);
        corridors[2] = new Corridor(new Location(new String("ac")),
                new Location(new String("bb")), 10);
        corridors[3] = new Corridor(new Location(new String("aa")),
                new Location(new String("cd")), 5);
        corridors[4] = new Corridor(new Location(new String("aa")),
                new Location(new String("cc")), 20);

        // test equal corridors
        Assert.assertTrue(corridors[0].compareTo(corridors[1]) == 0);

        // test less than

        // ordered by start
        Assert.assertTrue(corridors[0].compareTo(corridors[2]) < 0);
        // ordered by end
        Assert.assertTrue(corridors[0].compareTo(corridors[3]) < 0);
        // ordered by capacity
        Assert.assertTrue(corridors[0].compareTo(corridors[4]) < 0);

        // test greater than

        // ordered by start
        Assert.assertTrue(corridors[2].compareTo(corridors[0]) > 0);
        // ordered by end
        Assert.assertTrue(corridors[3].compareTo(corridors[0]) > 0);
        // ordered by capacity
        Assert.assertTrue(corridors[4].compareTo(corridors[0]) > 0);
    }
}
