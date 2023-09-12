package org.firstinspires.ftc.teamcode.internals.registration;

/**
 * Logs {@link OperationMode} registration information. Do not instantiate.
 */
public class OperationModeRegistrationLogger {

    private static final String KEY = "a7216e0b6a49850c6092991040467037d0fc899960bba2c08c4afafeb8b3bf1bfd748fe8050348f614c7e4421af2449a47db9d01de07b13a8c2fb060dac3e1ed5053643c4739479ff3fc665a9dba57e47d65803838d1617de4b1658a9e022e9bc0eaf71b";

    /**
     * Private constructor to prevent instantiation.
     */
    private OperationModeRegistrationLogger() {}

    /**
     * Logs the information requested to log.
     * @param s The string to log.
     * @param k The key, or password, required to log information. Simply exists to prevent people from calling things they shouldn't, not to be secure or anything.
     * @throws SecurityException The exception thrown when the key is incorrect.
     */
    public static void log(String s, String k) throws SecurityException {
        if(!KEY.equals(k)) {
            throw new SecurityException("Authentication key incorrect!");
        }else{
            System.out.println(" [ EPOCH " + System.nanoTime() + " ] Operation Mode Registration Service - " + s);
        }
    }

}