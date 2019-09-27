package io.codekontor.slizaa.integrationtest.shell;

import org.junit.Test;
import org.springframework.shell.MethodTarget;

import java.util.HashMap;
import java.util.Map;

public class CommandGroupTest extends AbstractSlizaaIntegrationTest {

    @Test
    public void commandGroupList() {

        Map<String, MethodTarget> commandMap = shell().listCommands();
        Map<String, Map<String, MethodTarget>> sortedCommandMap = new HashMap<>();

        commandMap.forEach((name, methodTarget) -> sortedCommandMap.
                computeIfAbsent(methodTarget.getGroup(), group -> new HashMap<>()).
                put(name, methodTarget));

        sortedCommandMap.forEach((group, map) -> {

            StringBuilder groupDescription = new StringBuilder();
            groupDescription.append(group);
            groupDescription.append("\n\n");

            // dump the commands
            map.forEach((name, target) -> {

                    groupDescription.append(name).append("\n");
/*                    groupDescription.append("=== ").append(target.getHelp()).append("\n");
                    groupDescription.append("[source,shell script]").append("\n");;
                    groupDescription.append("----").append("\n");
                    groupDescription.append(prompt).append("help ").append(name).append("\n");
                    groupDescription.append("\n");
                    CharSequence helpString = help.help(name);
                    helpString = helpString.subSequence(2, helpString.length() - 2);
                    groupDescription.append(helpString);
                    groupDescription.append("----").append("\n");*/
            });

            writeToResultFile(group, groupDescription.toString());
        });
    }
}