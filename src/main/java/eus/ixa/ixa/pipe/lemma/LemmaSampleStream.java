/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eus.ixa.ixa.pipe.lemma;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eus.ixa.ixa.pipe.pos.StringUtils;

import opennlp.tools.util.FilterObjectStream;
import opennlp.tools.util.ObjectStream;


public class LemmaSampleStream extends FilterObjectStream<String, LemmaSample> {

  public LemmaSampleStream(ObjectStream<String> samples) {
    super(samples);
  }

  public LemmaSample read() throws IOException {

    List<String> toks = new ArrayList<String>();
    List<String> tags = new ArrayList<String>();
    List<String> preds = new ArrayList<String>();

    for (String line = samples.read(); line != null && !line.equals(""); line = samples.read()) {
      String[] parts = line.split("\t");
      System.err.println(parts[0] + " " + parts[1] + " " + parts[2]);
      if (parts.length != 3) {
        System.err.println("Skipping corrupt line: " + line);
      }
      else {
        toks.add(parts[0]);
        tags.add(parts[1]);
        String ses = StringUtils.getShortestEditScript(parts[0], parts[2]);
        preds.add(ses);
      }
    }

    if (toks.size() > 0) {
      return new LemmaSample(toks.toArray(new String[toks.size()]), tags.toArray(new String[tags.size()]), preds.toArray(new String[preds.size()]));
    }
    else {
      return null;
    }
  }
}