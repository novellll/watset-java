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

package org.nlpub.watset.cli;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.nlpub.watset.graph.Clustering;
import org.nlpub.watset.util.AlgorithmProvider;

import java.util.Collections;

class CommandMaxMax extends ClusteringCommand {
    public CommandMaxMax(Application application) {
        super(application);
    }

    @Override
    public Clustering<String> getClustering() {
        final AlgorithmProvider<String, DefaultWeightedEdge> algorithm = new AlgorithmProvider<>("maxmax", Collections.emptyMap());
        return algorithm.apply(application.getGraph());
    }
}
