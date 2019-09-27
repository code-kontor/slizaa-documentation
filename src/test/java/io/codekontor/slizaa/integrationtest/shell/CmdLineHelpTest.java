package io.codekontor.slizaa.integrationtest.shell;

import org.junit.Test;
import org.springframework.shell.MethodTarget;

import java.io.IOException;
import java.util.Map;

public class CmdLineHelpTest extends AbstractSlizaaIntegrationTest {

    @Test
    public void generateHelp() {

        Map<String, MethodTarget> commandMap = shell().listCommands();

        // dump the commands
        commandMap.forEach((name, target) -> {

            StringBuilder cmdDescription = new StringBuilder();

            try {
                cmdDescription.append("[source,shell script]").append("\n");
                cmdDescription.append("----").append("\n");
                cmdDescription.append(prompt()).append("help ").append(name).append("\n");
                cmdDescription.append(getHelpForCommand(name));
                cmdDescription.append("----").append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            writeToResultFile(name, cmdDescription.toString());
        });
    }
}