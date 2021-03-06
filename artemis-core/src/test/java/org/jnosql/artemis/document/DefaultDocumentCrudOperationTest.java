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
package org.jnosql.artemis.document;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.jnosql.artemis.WeldJUnit4Runner;
import org.jnosql.artemis.model.Person;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCollectionEntity;
import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;


@RunWith(WeldJUnit4Runner.class)
public class DefaultDocumentCrudOperationTest {

    private Person person = Person.builder().
            withAge(10).
            withPhones(Arrays.asList("234", "432")).
            withName("Name")
            .withId(19)
            .withIgnore("Just Ignore").build();

    private Document[] documents = new Document[]{
            Document.of("age", 10),
            Document.of("phones", Arrays.asList("234", "432")),
            Document.of("name", "Name"),
            Document.of("id", 19L),
    };


    @Inject
    private DocumentEntityConverter converter;

    private DocumentCollectionManager managerMock;

    private DefaultDocumentCrudOperation subject;

    private ArgumentCaptor<DocumentCollectionEntity> captor;

    private DocumentEventPersistManager documentEventPersistManager;

    @Before
    public void setUp() {
        managerMock = Mockito.mock(DocumentCollectionManager.class);
        documentEventPersistManager = Mockito.mock(DocumentEventPersistManager.class);
        captor = ArgumentCaptor.forClass(DocumentCollectionEntity.class);
        Instance<DocumentCollectionManager> instance = Mockito.mock(Instance.class);
        Mockito.when(instance.get()).thenReturn(managerMock);
        this.subject = new DefaultDocumentCrudOperation(converter, instance, documentEventPersistManager);
    }

    @Test
    public void shouldSave() {
        DocumentCollectionEntity document = DocumentCollectionEntity.of("Person");
        document.addAll(Stream.of(documents).collect(Collectors.toList()));

        Mockito.when(managerMock
                .save(Mockito.any(DocumentCollectionEntity.class)))
                .thenReturn(document);

        subject.save(this.person);
        verify(managerMock).save(captor.capture());
        verify(documentEventPersistManager).firePostEntity(Mockito.any(Person.class));
        verify(documentEventPersistManager).firePreEntity(Mockito.any(Person.class));
        verify(documentEventPersistManager).firePreDocument(Mockito.any(DocumentCollectionEntity.class));
        verify(documentEventPersistManager).firePostDocument(Mockito.any(DocumentCollectionEntity.class));
        DocumentCollectionEntity value = captor.getValue();
        assertEquals("Person", value.getName());
        assertEquals(4, value.getDocuments().size());
    }

}