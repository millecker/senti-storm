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
package at.illecker.sentistorm.commons.util.io.kyro;

import cmu.arktweetnlp.Tagger.TaggedToken;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class TaggedTokenSerializer extends Serializer<TaggedToken> {

  @Override
  public TaggedToken read(Kryo kryo, Input input, Class<TaggedToken> type) {
    return new TaggedToken(input.readString(), input.readString());
  }

  @Override
  public void write(Kryo kryo, Output output, TaggedToken taggedToken) {
    output.writeString(taggedToken.token);
    output.writeString(taggedToken.tag);
  }

}
