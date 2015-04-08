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
package at.illecker.sentistorm.commons.util.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializationUtils {
  private static final Logger LOG = LoggerFactory
      .getLogger(SerializationUtils.class);

  public static <T extends Serializable> void serializeCollection(
      Collection<T> objects, String fileName) {
    // Assume Collection is Serializable
    // e.g., LinkedList or ArrayList
    if (objects instanceof java.io.Serializable) {
      SerializationUtils.serialize((Serializable) objects, fileName);
    } else {
      LOG.error("Collection is not serializable!");
    }
  }

  public static <K extends Serializable, V extends Serializable> void serializeMap(
      Map<K, V> objects, String fileName) {
    // Assume Map is Serializable
    if (objects instanceof java.io.Serializable) {
      SerializationUtils.serialize((Serializable) objects, fileName);
    } else {
      LOG.error("Map is not serializable!");
    }
  }

  public static <T extends Serializable> void serialize(T object,
      String fileName) {
    try {
      if (object != null) {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
      }
      LOG.info("Serialized in " + fileName);
    } catch (FileNotFoundException fnfe) {
      LOG.error("FileNotFoundException: " + fnfe.getMessage());
    } catch (IOException ioe) {
      LOG.error("IOException: " + ioe.getMessage());
    }
  }

  public static <T extends Serializable> T deserialize(String fileName) {
    return deserialize(IOUtils.getInputStream(fileName));
  }

  public static <T extends Serializable> T deserialize(InputStream is) {
    T object = null;
    try {
      if (is != null) {
        ObjectInputStream ois = new ObjectInputStream(is);
        object = (T) ois.readObject();
        ois.close();
        is.close();
      }
    } catch (FileNotFoundException fnfe) {
      LOG.error("FileNotFoundException: " + fnfe.getMessage());
    } catch (IOException ioe) {
      LOG.error("IOException: " + ioe.getMessage());
    } catch (ClassNotFoundException c) {
      LOG.error("ClassNotFoundException: " + c.getMessage());
    }
    return object;
  }

}
