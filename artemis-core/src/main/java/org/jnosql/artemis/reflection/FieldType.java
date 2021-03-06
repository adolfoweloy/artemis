/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jnosql.artemis.reflection;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * enum that contains kinds of annotations to fields on java.
 */
public enum FieldType {
    LIST, SET, MAP, COLLECTION, DEFAULT;

    /**
     * find you the kind of annotation on field and then define a enum type, follow the sequences:
     * <ul>
     * <li>List</li>
     * <li>Set</li>
     * <li>Map</li>
     * <li>Collection</li>
     * </ul>.
     *
     * @param field - the field with annotation
     * @return the type
     */
    public static FieldType of(Field field) {
        if (List.class.isAssignableFrom(field.getType())) {
            return LIST;
        }
        if (Set.class.isAssignableFrom(field.getType())) {
            return SET;
        }
        if (Collection.class.isAssignableFrom(field.getType())) {
            return COLLECTION;
        }
        if (Map.class.isAssignableFrom(field.getType())) {
            return MAP;
        }

        return DEFAULT;
    }

}
