/*
 * Copyright 2019 Dmitry Ustalov
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

package org.nlpub.watset.cli;

import com.beust.jcommander.Parameter;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.nlpub.watset.graph.EmptyClustering;
import org.nlpub.watset.graph.SimplifiedWatset;
import org.nlpub.watset.graph.Watset;
import org.nlpub.watset.util.AlgorithmProvider;
import org.nlpub.watset.util.CosineContextSimilarity;
import org.nlpub.watset.wsi.IndexedSense;
import org.nlpub.watset.wsi.Sense;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

class CommandSenses {
    final Application application;

    @Parameter(required = true, description = "Local clustering algorithm", names = {"-l", "--local"})
    String local;

    @Parameter(description = "Local clustering algorithm parameters", names = {"-lp", "--local-params"})
    String localParams;

    @Parameter(description = "Use Simplified Watset", names = {"-s", "--simplified"})
    boolean simplified = false;

    public CommandSenses(Application application) {
        this.application = application;
    }

    public void run() {
        requireNonNull(local);

        final AlgorithmProvider<String, DefaultWeightedEdge> algorithm = new AlgorithmProvider<>(local, localParams);

        final Graph<String, DefaultWeightedEdge> graph = application.getGraph();

        final Map<Sense<String>, Map<Sense<String>, Number>> contexts = simplified ? getSimplifiedWatsetContexts(algorithm, graph) : getWatsetContexts(algorithm, graph);

        try {
            write(application.output, contexts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void write(Path path, Map<Sense<String>, Map<Sense<String>, Number>> contexts) throws IOException {
        try (final BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (final Map.Entry<Sense<String>, Map<Sense<String>, Number>> context : contexts.entrySet()) {
                final IndexedSense<String> sense = ((IndexedSense<String>) context.getKey());

                final String contextRecord = context.getValue().entrySet().stream().
                        map(e -> String.format(Locale.ROOT, "%s#%d:%f",
                                e.getKey().get(),
                                ((IndexedSense<String>) e.getKey()).getSense(),
                                e.getValue().doubleValue())).
                        collect(joining(","));

                writer.write(String.format(Locale.ROOT, "%s\t%d\t%s%n", sense.get(), sense.getSense(), contextRecord));
            }
        }
    }

    public Map<Sense<String>, Map<Sense<String>, Number>> getWatsetContexts(AlgorithmProvider<String, DefaultWeightedEdge> algorithm, Graph<String, DefaultWeightedEdge> graph) {
        final Watset<String, DefaultWeightedEdge> watset = new Watset<>(graph, algorithm, EmptyClustering.provider(), new CosineContextSimilarity<>());

        watset.fit();

        return watset.getContexts();
    }

    public Map<Sense<String>, Map<Sense<String>, Number>> getSimplifiedWatsetContexts(AlgorithmProvider<String, DefaultWeightedEdge> algorithm, Graph<String, DefaultWeightedEdge> graph) {
        final SimplifiedWatset<String, DefaultWeightedEdge> watset = new SimplifiedWatset<>(graph, algorithm, EmptyClustering.provider());

        watset.fit();

        return watset.getContexts();
    }
}
