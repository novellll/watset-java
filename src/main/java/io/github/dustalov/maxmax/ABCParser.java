/*
 * Copyright 2016 Dmitry Ustalov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.github.dustalov.maxmax;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.graph.builder.UndirectedWeightedGraphBuilderBase;

import java.util.stream.Stream;

interface ABCParser {
    static UndirectedGraph<String, DefaultWeightedEdge> parse(Stream<String> stream) {
        final UndirectedWeightedGraphBuilderBase<String, DefaultWeightedEdge, ? extends SimpleWeightedGraph<String, DefaultWeightedEdge>, ?> builder = SimpleWeightedGraph.builder(DefaultWeightedEdge.class);

        stream.forEach(line -> {
            final String[] split = line.split("\t");
            if (split.length != 3 || split[0].equals(split[1])) return;
            builder.addVertices(split[0], split[1]);
            builder.addEdge(split[0], split[1], Double.valueOf(split[2]));
        });

        return builder.buildUnmodifiable();
    }
}
