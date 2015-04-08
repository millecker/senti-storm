/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.illecker.sentistorm.commons.tfidf;

/**
 * Word count method used for term frequencies
 */
public enum TfType {
  /**
   * Raw frequency: tf(t,d) = f(t,d) The number of times that term t occurs in
   * document d.
   */
  RAW,

  /**
   * Log scaled frequency: tf(t,d) = 1 + log f(t,d), or zero if f(t,d) is zero;
   */
  LOG,

  /**
   * Boolean frequency: tf(t,d) = 1 if t occurs in d and 0 otherwise;
   */
  BOOL
}
