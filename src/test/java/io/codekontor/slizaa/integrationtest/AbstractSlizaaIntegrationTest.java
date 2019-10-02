/**
 * Slizaa Documentation - Slizaa Static Software Analysis Tools
 * Copyright © 2019 Code-Kontor GmbH and others (slizaa@codekontor.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.codekontor.slizaa.integrationtest;

import io.codekontor.slizaa.server.SlizaaServerConfiguration;
import io.codekontor.slizaa.server.command.SlizaaPromptProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.standard.commands.Help;
import org.springframework.shell.table.Table;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(properties = {InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=" + false, "graphql.servlet.websocket.enabled=false"})
@ContextConfiguration(classes = SlizaaServerConfiguration.class)
@DirtiesContext
public abstract class AbstractSlizaaIntegrationTest {

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    @BeforeClass
    public static void configureSlizaaInstance() throws IOException {
        File workDir = folder.newFolder();
        System.setProperty("database.rootDirectory", workDir + File.separator + "dbs");
        System.setProperty("configuration.rootDirectory", workDir + File.separator + "cfg");
    }

    @FunctionalInterface
    public interface CommandResultInterceptor {
        public String interceptCommandResult(String commandResult);
    }

    @Autowired
    private Shell shell;

    @Autowired
    private Help help;

    @Autowired
    private SlizaaPromptProvider slizaaPromptProvider;

    private File documentationTargetDirectory;

    protected CharSequence getHelpForCommand(String commandName) throws IOException {
        CharSequence helpString = help.help(commandName);
        return helpString.subSequence(2, helpString.length() - 2);
    }

    @Before
    public void prepare() {
        deleteGeneratedDocs();
        executeCommandAndWriteToResultFile("installExtensions io.codekontor.slizaa.extensions.jtype_1.0.0");
    }

    protected Shell shell() {
        return this.shell;
    }

    protected String prompt() {
        return slizaaPromptProvider.getPrompt().toAnsi();
    }

    protected String executeCommand(String command) {
        return formatExecutionResult(shell.evaluate(() -> command));
    }

    protected String executeCommandAndWriteToResultFile(String command) {
        return executeCommandAndWriteToResultFile(command, null);
    }

    protected String executeCommandAndWriteToResultFile(String command, CommandResultInterceptor commandResultInterceptor) {
        int i = command.indexOf(' ');
        String fileName = i != -1 ? command.substring(0, i) : command;
        return executeCommandAndWriteToResultFile(fileName, command, commandResultInterceptor);
    }

    protected String executeCommandAndWriteToResultFile(String fileName, String command, CommandResultInterceptor commandResultInterceptor) {

        Object result = shell.evaluate(() -> command);

        StringBuilder fileContent = new StringBuilder();
        fileContent.append("[source]").append("\n");
        fileContent.append("----").append("\n");
        fileContent.append(prompt()).append(command).append("\n");

        String commandFormat = formatExecutionResult(result);
        if (commandResultInterceptor != null) {
            commandFormat = commandResultInterceptor.interceptCommandResult(commandFormat);
        }
        fileContent.append(commandFormat);

        fileContent.append("----").append("\n");

        writeToResultFile(fileName, fileContent.toString());
        return fileContent.toString();
    }

    protected void writeToResultFile(String fileName, String content) {
        writeToResultFile(fileName, "", content);
    }

    protected void writeToResultFile(String fileName, String postFix, String content) {

        postFix = postFix != null && !postFix.isEmpty() ? "-" + postFix : "";

        String className = this.getClass().getSimpleName().replace("Test", "");
        String finalFileNameWithoutExtenion = className.toLowerCase().concat("-").concat(
                fileName
                        .replace(" - ", "-")
                        .replace(' ', '-')
                        .toLowerCase()
                        .concat(postFix));

        File asciiDocFile = new File(getOrCreateTargetDirectory(), finalFileNameWithoutExtenion + ".adoc");
        int appendix = 1;
        while (asciiDocFile.exists()) {
            appendix++;
            asciiDocFile = new File(getOrCreateTargetDirectory(), finalFileNameWithoutExtenion + String.format("_%02d", appendix) + ".adoc");
        }

        try (FileWriter writer = new FileWriter(asciiDocFile)) {
            writer.append(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    protected String formatExecutionResult(Object result) {
        String resultAsString = null;
        if (result instanceof Table) {
            resultAsString = ((Table) result).render(80);
        } else {
            resultAsString = String.valueOf(result);
        }
        return resultAsString;
    }

    protected void deleteGeneratedDocs() {
        for (File file : getOrCreateTargetDirectory().listFiles(((dir, name) -> name.startsWith(this.getClass().getSimpleName().replace("Test", "").toLowerCase())))) {
            file.delete();
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