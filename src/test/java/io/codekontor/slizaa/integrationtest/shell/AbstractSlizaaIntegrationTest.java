package io.codekontor.slizaa.integrationtest.shell;

import io.codekontor.slizaa.server.SlizaaServerConfiguration;
import io.codekontor.slizaa.server.command.SlizaaPromptProvider;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.MethodTarget;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.standard.commands.Help;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(properties = {InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=" + false, "graphql.servlet.websocket.enabled=false"})
@ContextConfiguration(classes = SlizaaServerConfiguration.class)
public class AbstractSlizaaIntegrationTest {

    @Autowired
    private Shell shell;

    @Autowired
    private Help help;

    @Autowired
    private SlizaaPromptProvider slizaaPromptProvider;

    private File documentationTargetDirectory;

    protected Shell shell() {
        return this.shell;
    }

    protected String prompt() {
        return slizaaPromptProvider.getPrompt().toAnsi();
    }

    protected String executeCommand(String command) {
        return String.valueOf(shell.evaluate(() -> command));
    }

    protected CharSequence getHelpForCommand(String commandName) throws IOException {
        CharSequence helpString = help.help(commandName);
        return helpString.subSequence(2, helpString.length() - 2);
    }

    protected void writeToResultFile(String fileName, String content) {
        writeToResultFile(fileName, "", content);
    }

    protected void writeToResultFile(String fileName, String postFix, String content) {

        fileName = fileName
                .replace(" - ", "-")
                .replace(' ', '-')
                .toLowerCase()
                .concat("-")
                .concat(postFix)
                .concat(".adoc");

        String className =  this.getClass().getSimpleName().replace("Test", "");
        fileName = className.toLowerCase().concat("-").concat(fileName);

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
        if (documentationTargetDirectory == null) {
            documentationTargetDirectory = new File("target" + File.separator + "slizaa-asciidoc");
            if (!documentationTargetDirectory.exists()) {
                documentationTargetDirectory.mkdirs();
            }
        }
        return documentationTargetDirectory;
    }
}