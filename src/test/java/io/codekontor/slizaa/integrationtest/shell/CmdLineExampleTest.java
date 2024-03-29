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
import io.codekontor.slizaa.integrationtest.util.TestContentInitializer;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CmdLineExampleTest extends AbstractSlizaaIntegrationTest {

    @Test
    public void commandExamples() throws IOException {

        assertThat(executeCommandAndWriteToResultFile("help"))
                .contains("AVAILABLE COMMANDS");

        assertThat(executeCommandAndWriteToResultFile("memUsage"))
                .contains("Current Memory Usage:");
        assertThat(executeCommandAndWriteToResultFile("gc"))
                .contains("Current Memory Usage:");

        assertThat(executeCommandAndWriteToResultFile("listDBs"))
                .contains("No database configured.");
        assertThat(executeCommandAndWriteToResultFile("createDB exampleDB"))
                .contains("|exampleDB |INITIAL|");

        assertThat(executeCommandAndWriteToResultFile("setContentDefinitionProvider exampleDB directory " + TestContentInitializer.instance().getExampleContentPath()))
                .contains("|exampleDB |CONFIGURED|");
        assertThat(executeCommandAndWriteToResultFile("listContentDefinitionProviderFactories"))
                .contains("Content Definition Provider Factories:");
        assertThat(executeCommandAndWriteToResultFile("showResolvedContentDefinitions exampleDB", 10))
                .contains("Content Definitions:");

        assertThat(executeCommandAndWriteToResultFile("parseDB exampleDB"))
                .contains("|exampleDB |RUNNING|");
        assertThat(executeCommandAndWriteToResultFile("stopDB exampleDB"))
                .contains("|exampleDB |NOT_RUNNING|");
        assertThat(executeCommandAndWriteToResultFile("startDB exampleDB"))
                .contains("|exampleDB |RUNNING|");
        assertThat(executeCommandAndWriteToResultFile("listDBs"))
                .contains("|exampleDB |RUNNING|");

        assertThat(executeCommandAndWriteToResultFile("createHierarchicalGraph exampleDB hg01"))
                .contains("|[hg01]");

        assertThat(executeCommandAndWriteToResultFile("deleteHierarchicalGraph exampleDB hg01"))
                .doesNotContain("|[hg01]");

        assertThat(executeCommandAndWriteToResultFile("stopDB exampleDB"))
                .contains("|exampleDB |NOT_RUNNING|");
        assertThat(executeCommandAndWriteToResultFile("deleteDB exampleDB"))
                .contains("No database configured.");
    }
}