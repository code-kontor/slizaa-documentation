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

import java.util.HashMap;
import java.util.Map;

public class CommandGroupTest extends AbstractSlizaasShellIntegrationTest {

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