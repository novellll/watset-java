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

package org.nlpub.watset.util;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * This functional interface computes a similarity measure between a pair of bag-of-words.
 * It maps a pair of bags to a number.
 *
 * @param <V> bag element class.
 * @see <a href="https://nlp.stanford.edu/IR-book/html/htmledition/dot-products-1.html">Dot products</a>
 */
public interface ContextSimilarity<V> extends BiFunction<Map<V, Number>, Map<V, Number>, Number> {
    /**
     * Returns a dummy similarity function that always returns zero.
     *
     * @param <V> bag element class.
     * @return dummy similarity function.
     */
    static <V> ContextSimilarity<V> dummy() {
        return (bag1, bag2) -> 0;
    }
}
