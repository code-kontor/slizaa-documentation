package io.codekontor.slizaa.integrationtest.shell;

import org.junit.Test;

import java.io.IOException;

public class AdminCommandTest extends AbstractSlizaaIntegrationTest {

    @Test
    public void gc() throws IOException {

        String cmd = "gc";
        String cmdName = "gc";

        String result = executeCommand(cmd);

        StringBuilder cmdExample = new StringBuilder();
        cmdExample.append("[source,shell script]").append("\n");
        cmdExample.append("----").append("\n");
        cmdExample.append(prompt()).append(cmd).append("\n");
        cmdExample.append(String.valueOf(result));
        cmdExample.append("\n");
        cmdExample.append("----").append("\n");

        writeToResultFile(cmdName, "example", cmdExample.toString());
    }
}