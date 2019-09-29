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
package io.codekontor.slizaa.integrationtest.gettingstarted;

import io.codekontor.slizaa.integrationtest.AbstractSlizaaIntegrationTest;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;

public class GettingStartedTest extends AbstractSlizaaIntegrationTest {

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    @BeforeClass
    public static void configureSlizaaInstance() throws IOException {
        File workDir = folder.newFolder();
        System.setProperty("database.rootDirectory", workDir + File.separator + "dbs");
        System.setProperty("configuration.rootDirectory", workDir + File.separator + "cfg");
    }

    @Before
    public void prepare() {
        deleteGeneratedDocs();
        executeCommandAndWriteToResultFile("installExtensions io.codekontor.slizaa.extensions.jtype_1.0.0");
    }

    @Test
    public void testIt() {

        //
        executeCommandAndWriteToResultFile("help");

        //
        executeCommandAndWriteToResultFile("listDBs");

        //
        executeCommandAndWriteToResultFile("createDB exampleDB");

        //
        executeCommandAndWriteToResultFile("setContentDefinitionProvider exampleDB directory c:\\tmp\\example",
                result -> result
                        .replace("|setContentDefinitionProvider,|", "|setC...,         |")
                        .replace("            |\n", "|\n")
                        .replace("+-----------------------------+", "+-----------------+"));

        //
        executeCommandAndWriteToResultFile("parseDB exampleDB");

        //
        executeCommandAndWriteToResultFile("createHierarchicalGraph exampleDB hg01");

        //
        executeCommandAndWriteToResultFile("listDBs");
    }
}