/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.tools.crepecake.cache;

import com.google.cloud.tools.crepecake.blob.Blob;
import com.google.cloud.tools.crepecake.blob.BlobDescriptor;
import com.google.cloud.tools.crepecake.image.DescriptorDigest;
import com.google.common.io.Resources;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/** Tests for {@link CachedLayer}. */
@RunWith(MockitoJUnitRunner.class)
public class CachedLayerTest {

  @Mock private File mockFile;
  @Mock private BlobDescriptor mockBlobDescriptor;
  @Mock private DescriptorDigest mockDiffId;

  @Test
  public void testNew() {
    CachedLayer layer = new CachedLayer(mockFile, mockBlobDescriptor, mockDiffId);

    Assert.assertEquals(mockFile, layer.getContentFile());
    Assert.assertEquals(mockBlobDescriptor, layer.getBlobDescriptor());
    Assert.assertEquals(mockDiffId, layer.getDiffId());
  }

  @Test
  public void testGetBlob() throws URISyntaxException, IOException {
    File fileA = new File(Resources.getResource("fileA").toURI());
    String expectedFileAString =
        new String(Files.readAllBytes(fileA.toPath()), StandardCharsets.UTF_8);

    CachedLayer cachedLayer = new CachedLayer(fileA, mockBlobDescriptor, mockDiffId);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Blob fileBlob = cachedLayer.getBlob();
    fileBlob.writeTo(outputStream);

    Assert.assertEquals(
        expectedFileAString, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    Assert.assertEquals(mockBlobDescriptor, cachedLayer.getBlobDescriptor());
    Assert.assertEquals(mockDiffId, cachedLayer.getDiffId());
  }
}