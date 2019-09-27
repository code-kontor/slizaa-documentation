package io.codekontor.slizaa.integrationtest;

import io.codekontor.slizaa.server.SlizaaServerConfiguration;
import io.codekontor.slizaa.server.command.SlizaaPromptProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Input;
import org.springframework.shell.InputProvider;
import org.springframework.shell.MethodTarget;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.result.DefaultResultHandler;
import org.springframework.shell.standard.commands.Help;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(properties = {InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=" + false, "graphql.servlet.websocket.enabled=false"})
@ContextConfiguration(classes = SlizaaServerConfiguration.class)
public class SlizaaIntegrationTest {

    @Autowired
    private Shell shell;

    @Autowired
    private Help help;

    @Autowired
    private SlizaaPromptProvider slizaaPromptProvider;

    private File resultDir;

    @Test
    public void gc() throws IOException {

        String prompt = slizaaPromptProvider.getPrompt().toAnsi();
        String cmd = "gc";
        String cmdName = "gc";

        Object result = shell.evaluate(() -> cmd);

        StringBuilder cmdExample = new StringBuilder();
        cmdExample.append("[source,shell script]").append("\n");
        cmdExample.append("----").append("\n");
        cmdExample.append(prompt).append(cmd).append("\n");
        cmdExample.append(String.valueOf(result));
        cmdExample.append("\n");
        cmdExample.append("----").append("\n");

        writeToResultFile(cmdName, "example", cmdExample.toString());
    }

    @Test
    public void generateHelp() {

        Map<String, MethodTarget> commandMap = shell.listCommands();

        String prompt = slizaaPromptProvider.getPrompt().toAnsi();

        // dump the commands
        commandMap.forEach((name, target) -> {

            StringBuilder cmdDescription = new StringBuilder();

            try {
                // cmdDescription.append("=== ").append(target.getHelp()).append("\n");
                cmdDescription.append("[source,shell script]").append("\n");
                cmdDescription.append("----").append("\n");
                cmdDescription.append(prompt).append("help ").append(name).append("\n");
                // cmdDescription.append("\n");
                CharSequence helpString = help.help(name);
                helpString = helpString.subSequence(2, helpString.length() - 2);
                cmdDescription.append(helpString);
                cmdDescription.append("----").append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }



            writeToResultFile(name, "help", cmdDescription.toString());
        });
    }

/*    @Test
    public void test() {

        Map<String, MethodTarget> commandMap = shell.listCommands();
        Map<String, Map<String, MethodTarget>> sortedCommandMap = new HashMap<>();

        String prompt = slizaaPromptProvider.getPrompt().toAnsi();

        commandMap.forEach((name, methodTarget) -> sortedCommandMap.
                computeIfAbsent(methodTarget.getGroup(), group -> new HashMap<>()).
                put(name, methodTarget));

        sortedCommandMap.forEach((group, map) -> {

            StringBuilder groupDescription = new StringBuilder();
            groupDescription.append(group);
            groupDescription.append("\n\n");

            // dump the commands
            map.forEach((name, target) -> {

                try {
                    groupDescription.append("=== ").append(target.getHelp()).append("\n");
                    groupDescription.append("[source,shell script]").append("\n");;
                    groupDescription.append("----").append("\n");
                    groupDescription.append(prompt).append("help ").append(name).append("\n");
                    groupDescription.append("\n");
                    CharSequence helpString = help.help(name);
                    helpString = helpString.subSequence(2, helpString.length() - 2);
                    groupDescription.append(helpString);
                    groupDescription.append("----").append("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            String fileName = group
                    .replace(" - ", "-")
                    .replace(' ', '-')
                    .toLowerCase().concat(".adoc");
            writeToResultFile(fileName, groupDescription.toString());
        });
    }*/

    private void writeToResultFile(String commandName, String postFix, String content) {

        String fileName = commandName
                .replace(" - ", "-")
                .replace(' ', '-')
                .toLowerCase()
                .concat("-")
                .concat(postFix)
                .concat(".adoc");

        File asciiDocFile = new File(getOrCreateTargetDirectory(), fileName);
        try (FileWriter writer = new FileWriter(asciiDocFile)) {
            writer.append(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    private File getOrCreateTargetDirectory() {
        if (resultDir == null) {
            resultDir = new File("target" + File.separator + "slizaa-asciidoc");
            if (!resultDir.exists()) {
                resultDir.mkdirs();
            }
        }
        return resultDir;
    }
}