package org.gooseapple.politiks.util;

import org.gooseapple.politiks.Politiks;

import java.util.logging.Level;

public class Errors {
    public static void LogError(String error) {
        Politiks.logger.log(Level.SEVERE, error);
    }
}
