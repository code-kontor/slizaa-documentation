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
package io.codekontor.slizaa.integrationtest.shell;

import io.codekontor.slizaa.integrationtest.AbstractSlizaaIntegrationTest;
import org.junit.Test;
import org.springframework.shell.MethodTarget;

import java.io.IOException;
import java.util.Map;

public class CmdLineHelpTest extends AbstractSlizaasShellIntegrationTest {

    @Test
    public void generateHelp() {

        Map<String, MethodTarget> commandMap = shell().listCommands();

        // dump the commands
        commandMap.forEach((name, target) -> {

            StringBuilder cmdDescription = new StringBuilder();

            try {
                cmdDescription.append("[source]").append("\n");
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