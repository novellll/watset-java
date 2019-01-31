/*
 * Copyright 2018 Dmitry Ustalov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.nlpub.watset.graph;

import org.jgrapht.Graph;

import java.util.Collection;
import java.util.Collections;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * A trivial clustering algorithm that puts every node in a separate cluster.
 *
 * @param <V> node class.
 * @param <E> edge class.
 */
public class SingletonClustering<V, E> implements Clustering<V> {
    private final Graph<V, E> graph;

    /**
     * Sets up a trivial clustering algorithm.
     *
     * @param graph an input graph.
     */
    public SingletonClustering(Graph<V, E> graph) {
        this.graph = requireNonNull(graph);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Collection<V>> getClusters() {
        return graph.vertexSet().stream().map(Collections::singleton).collect(toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
    }
}
