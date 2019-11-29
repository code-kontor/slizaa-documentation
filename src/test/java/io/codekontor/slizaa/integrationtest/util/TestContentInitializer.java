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
package io.codekontor.slizaa.integrationtest.util;

import io.codekontor.mvnresolver.MvnResolverServiceFactoryFactory;
import io.codekontor.mvnresolver.api.IMvnResolverService;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class TestContentInitializer {

    // TODO: unix support
    private static final String EXAMPLE_DIRECTORY = "C:\\tmp\\sl";

    public static TestContentInitializer instance() throws IOException {
        if (_instance == null) {
            _instance = new TestContentInitializer();
            _instance.initialize();
        }
        return _instance;
    }

    public String getExampleContentPath() {
        return _exampleDirectory.getAbsolutePath();
    }

    public File getExampleContentDirectory() {
        return _exampleDirectory;
    }

    private void initialize() throws IOException {

            // setup the mvm resolver service
            _mvnResolverService = MvnResolverServiceFactoryFactory
                    .createNewResolverServiceFactory().newMvnResolverService().withMavenCentralRepo().create();

            // create the example directory
            _exampleDirectory = new File(EXAMPLE_DIRECTORY);
            if (_exampleDirectory.exists()) {
                FileUtils.deleteDirectory(_exampleDirectory);
            } else {
                _exampleDirectory.mkdirs();
            }

            // copy the files to the example directory
            File[] resolvedFiles = _mvnResolverService.resolve(false, contentList());
            for (File resolvedFile : resolvedFiles) {
                FileUtils.copyFileToDirectory(resolvedFile, _exampleDirectory);
            }
    }

    private static TestContentInitializer _instance;

    private IMvnResolverService _mvnResolverService;

    private File _exampleDirectory;

    private TestContentInitializer() {
        // empty
    }

    private String[] contentList() {

        SimpleVersionResolver.SlizaaVersions slizaaVersions = SimpleVersionResolver.getSlizaaVersions();

        String[] contentList = new String[SlizaaArtifactList.FILES_TO_ANALYZE.length];
        for (int i = 0; i < contentList.length; i++) {
            contentList[i] = SlizaaArtifactList.FILES_TO_ANALYZE[i]
                    .replace("$VERSION_MVNRESOLVER$", slizaaVersions.getMvnResolverVersion())
                    .replace("$VERSION_SLIZAA$", slizaaVersions.getProjectVersion());

        }
        return contentList;
    }
}
