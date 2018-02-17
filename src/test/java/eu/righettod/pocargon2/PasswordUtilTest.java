package eu.righettod.pocargon2;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Test suites for the class "PasswordUtil".
 */
public class PasswordUtilTest {

    /**
     * Test to validate that the computation of a password's hash is functional:
     * - Hash is generated and is valid
     * - Password is wiped
     */
    @Test
    public void testHashCorrectComputation() {
        char[] password = "test".toCharArray();
        String hash = PasswordUtil.hash(password, Charset.forName("UTF-8"));
        Assert.assertNotNull(hash);
        String[] hashParts = hash.split("\\$");
        Assert.assertEquals(6, hashParts.length);
        Assert.assertEquals("argon2i", hashParts[1]);
        for (char c : password) {
            Assert.assertEquals('\u0000', c);
        }
    }

    /**
     * Test to validate that the verification of a password's hash is functional when password match is expected:
     * - Hash is verified and return true
     * - Password is wiped
     */
    @Test
    public void testHashCorrectVerificationCaseOK() {
        String hash = PasswordUtil.hash("test".toCharArray(), Charset.forName("UTF-8"));
        char[] password = "test".toCharArray();
        boolean isMatching = PasswordUtil.verify(hash, password, Charset.forName("UTF-8"));
        Assert.assertTrue(isMatching);
        for (char c : password) {
            Assert.assertEquals('\u0000', c);
        }
    }

    /**
     * Test to validate that the verification of a password's hash is functional when password match is not expected:
     * - Hash is verified and return false
     * - Password is wiped
     */
    @Test
    public void testHashCorrectVerificationCaseKO() {
        String hash = PasswordUtil.hash("test".toCharArray(), Charset.forName("UTF-8"));
        char[] password = "testBadPassword".toCharArray();
        boolean isMatching = PasswordUtil.verify(hash, password, Charset.forName("UTF-8"));
        Assert.assertFalse(isMatching);
        for (char c : password) {
            Assert.assertEquals('\u0000', c);
        }
    }

    /**
     * Test that the computation time of a hash take at least 2 seconds
     */
    @Test
    public void testComputationDelay() {
        Instant start = Instant.now();
        PasswordUtil.hash("test".toCharArray(), Charset.forName("UTF-8"));
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        Assert.assertTrue(timeElapsed.getSeconds() >= 2);
    }

    /**
     * Test that the options chosen for Argon2 keep the following characteristic over parallel computations with a random password at each round:
     * - Computation time of a hash take at least 2 seconds
     * - Do not cause Out Of Memory with default Java 8 JVM without any customization
     */
    @Test
    public void testOverallStabilityOfOptionsParallelProcessing() {
        int tryCount = 20;
        //Prepare the list of passwords based on iteration counter
        List<String> passwords = new ArrayList<>(tryCount);
        for (int i = 0; i < tryCount; i++) {
            passwords.add(RandomStringUtils.randomAlphanumeric(8));
        }
        //Run the test in parallel using JVM built-in parallel feature
        passwords.parallelStream().forEach(p -> {
            Instant start = Instant.now();
            PasswordUtil.hash(p.toCharArray(), Charset.forName("UTF-8"));
            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
            Assert.assertTrue(timeElapsed.getSeconds() >= 2);
        });
    }


}
