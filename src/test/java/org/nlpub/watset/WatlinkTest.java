/*
 * Copyright 2017 Dmitry Ustalov
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

package org.nlpub.watset;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.Before;
import org.junit.Test;
import org.nlpub.vsm.ContextCosineSimilarity;
import org.nlpub.watset.sense.IndexedSense;
import org.nlpub.watset.sense.Sense;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.nlpub.watset.SenseInductionTest.WORDS;
import static org.nlpub.watset.WatsetTest.globalClusteringProvider;
import static org.nlpub.watset.WatsetTest.localClusteringProvider;

public class WatlinkTest {
    final static Watset<String, DefaultWeightedEdge> watset = new Watset<>(WORDS, localClusteringProvider, globalClusteringProvider, new ContextCosineSimilarity<>());

    final static Map<String, Collection<String>> hypernyms = new HashMap<String, Collection<String>>() {{
        put("a", Arrays.asList("e", "f"));
        put("b", Arrays.asList("e", "g"));
    }};

    private Watlink<String> watlink;

    @Before
    public void setup() {
        watset.run();
        watlink = new Watlink<>(watset.getInventory(), new ContextCosineSimilarity<>(), 1);
    }

    @Test
    public void testRetrieval() {
        final Map<Sense<String>, Number> context = watlink.retrieve(Arrays.asList("a", "b", "c"), hypernyms);
        final Sense<String> gold = IndexedSense.of("e", 0);
        assertEquals(Collections.singleton(gold), context.keySet());
        assertEquals(2, context.get(gold));
    }
}