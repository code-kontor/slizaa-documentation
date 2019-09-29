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
import io.codekontor.slizaa.server.command.SlizaaPromptProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.commands.Help;

import java.io.IOException;

public abstract class AbstractSlizaasShellIntegrationTest extends AbstractSlizaaIntegrationTest {

    @Autowired
    private Help help;

    protected CharSequence getHelpForCommand(String commandName) throws IOException {
        CharSequence helpString = help.help(commandName);
        return helpString.subSequence(2, helpString.length() - 2);
    }
}