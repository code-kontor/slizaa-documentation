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

import org.assertj.core.api.Assertions;
import org.w3c.dom.Document;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleVersionResolver {

    public static class SlizaaVersions {
        private String versionMvnResolver;
        private String versionProject;

        public SlizaaVersions(String versionMvnResolver, String versionProject) {
            assertThat(versionMvnResolver).isNotEmpty();
            assertThat(versionProject).isNotEmpty();
            this.versionMvnResolver = versionMvnResolver;
            this.versionProject = versionProject;
        }

        public String getMvnResolverVersion() {
            return versionMvnResolver;
        }

        public String getProjectVersion() {
            return versionProject;
        }

        @Override
        public String toString() {
            return "SlizaaVersions{" +
                    "versionMvnResolver='" + versionMvnResolver + '\'' +
                    ", versionProject='" + versionProject + '\'' +
                    '}';
        }
    }

    public static SlizaaVersions getSlizaaVersions() {
        try {

            File pom = new File("pom.xml");

            if (!pom.exists()) {
                throw new RuntimeException();
            }

            FileInputStream fileIS = new FileInputStream(pom);
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(fileIS);

            XPath xPath = XPathFactory.newInstance().newXPath();
            String version_mvnResolver = xPath.evaluate("/project/properties/mvnresolver.version/text()", xmlDocument);
            String version_project = xPath.evaluate("/project/version//text()", xmlDocument);
            return new SlizaaVersions(version_mvnResolver, version_project);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("'" + getSlizaaVersions() + "'");
    }
}
