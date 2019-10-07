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

public interface SlizaaArtifactList {
    
    String[] FILES_TO_ANALYZE = new String[]{
            "io.codekontor.mvnresolver:mvnresolver-uber:$VERSION_MVNRESOLVER$",
            "io.codekontor.slizaa:slizaa-server-service-configuration:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-server-service-extensions:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-server-staticcontent:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-server-command:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-hierarchicalgraph-graphdb-model:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-hierarchicalgraph-graphdb-mapping-service:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-hierarchicalgraph-core-model:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-hierarchicalgraph-core-selection:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-scanner-cypherregistry:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-scanner-contentdefinition:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-server-service-selection:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-core-progressmonitor:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-hierarchicalgraph-core-algorithms:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-server-spec:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-server-main:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-scanner-spi-api:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-hierarchicalgraph-graphdb-mapping-spi:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-server-service-svg:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-core-boltclient:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-hierarchicalgraph-graphdb-mapping-cypher:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-server-service-backend:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-server-graphql:$VERSION_SLIZAA$",
            "io.codekontor.slizaa:slizaa-server-service-slizaa:$VERSION_SLIZAA$"
    };
}
