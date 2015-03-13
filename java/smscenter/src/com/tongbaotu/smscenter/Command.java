package com.tongbaotu.smscenter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
/**
 * Command
 * @author Tim
 */
public class Command {
    public static Options options = new Options();

    public boolean parseOption(String[] args) {
        List<String> argList = Arrays.asList(args);
        Iterator<String> it = argList.iterator();
        while (it.hasNext()) {
            String opt = it.next();
            if (opt.equals("-configPath")) {
                options.configPath = it.next();
            }
        }
        return true;
    }
}
