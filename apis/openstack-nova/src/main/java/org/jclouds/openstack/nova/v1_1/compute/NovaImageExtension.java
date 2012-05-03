/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jclouds.openstack.nova.v1_1.compute;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.Constants;
import org.jclouds.compute.ImageExtension;
import org.jclouds.compute.domain.CloneImageTemplate;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.ImageTemplate;
import org.jclouds.compute.domain.ImageTemplateBuilder;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.concurrent.Futures;
import org.jclouds.logging.Logger;
import org.jclouds.openstack.nova.v1_1.NovaClient;
import org.jclouds.openstack.nova.v1_1.domain.Server;
import org.jclouds.openstack.nova.v1_1.domain.zonescoped.ImageInZone;
import org.jclouds.openstack.nova.v1_1.domain.zonescoped.ZoneAndId;
import org.jclouds.predicates.PredicateWithResult;
import org.jclouds.predicates.Retryables;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.ListenableFuture;

@Singleton
public class NovaImageExtension implements ImageExtension {

   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   protected Logger logger = Logger.NULL;

   private final NovaClient novaClient;
   private final Function<ImageInZone, Image> imageInZoneToImage;
   private final ExecutorService executor;
   @com.google.inject.Inject(optional = true)
   @Named("IMAGE_MAX_WAIT")
   long maxWait = 3600;
   @com.google.inject.Inject(optional = true)
   @Named("IMAGE_WAIT_PERIOD")
   long waitPeriod = 1;

   @Inject
   public NovaImageExtension(NovaClient novaClient, Function<ImageInZone, Image> imageInZoneToImage,
            @Named(Constants.PROPERTY_USER_THREADS) ExecutorService userThreads) {
      this.novaClient = checkNotNull(novaClient);
      this.imageInZoneToImage = imageInZoneToImage;
      this.executor = userThreads;
   }

   @Override
   public ImageTemplate buildImageTemplateFromNode(String name, final String id) {
      ZoneAndId zoneAndId = ZoneAndId.fromSlashEncoded(id);
      Server server = novaClient.getServerClientForZone(zoneAndId.getZone()).getServer(zoneAndId.getId());
      if (server == null)
         throw new NoSuchElementException("Cannot find server with id: " + zoneAndId);
      CloneImageTemplate template = new ImageTemplateBuilder.CloneImageTemplateBuilder().nodeId(id).name(name).build();
      return template;
   }

   @Override
   public ListenableFuture<Image> createImage(ImageTemplate template) {
      checkState(template instanceof CloneImageTemplate,
               " openstack-nova only supports creating images through cloning.");
      CloneImageTemplate cloneTemplate = (CloneImageTemplate) template;
      final ZoneAndId zoneAndId = ZoneAndId.fromSlashEncoded(cloneTemplate.getSourceNodeId());

      final String newImageId = novaClient.getServerClientForZone(zoneAndId.getZone()).createImageFromServer(
               cloneTemplate.getName(), zoneAndId.getId());
      logger.info(">> Registered new Image %s, waiting for it to become available.", newImageId);

      return Futures.makeListenable(executor.submit(new Callable<Image>() {
         @Override
         public Image call() throws Exception {
            return Retryables.retryGettingResultOrFailing(new PredicateWithResult<String, Image>() {

               org.jclouds.openstack.nova.v1_1.domain.Image result;
               RuntimeException lastFailure;

               @Override
               public boolean apply(String input) {
                  result = checkNotNull(findImage(ZoneAndId.fromZoneAndId(zoneAndId.getZone(), newImageId)));
                  switch (result.getStatus()) {
                     case ACTIVE:
                        logger.info("<< Image %s is available for use.", newImageId);
                        return true;
                     case UNKNOWN:
                     case SAVING:
                        logger.debug("<< Image %s is not available yet.", newImageId);
                        return false;
                     default:
                        lastFailure = new IllegalStateException("Image was not created: " + newImageId);
                        throw lastFailure;
                  }
               }

               @Override
               public Image getResult() {
                  return imageInZoneToImage.apply(new ImageInZone(result, zoneAndId.getZone()));
               }

               @Override
               public Throwable getLastFailure() {
                  return lastFailure;
               }
            }, newImageId, maxWait, waitPeriod, TimeUnit.SECONDS,
                     "Image was not created within the time limit, Giving up! [Limit: " + maxWait + " secs.]");
         }
      }), executor);
   }

   @Override
   public boolean deleteImage(String id) {
      ZoneAndId zoneAndId = ZoneAndId.fromSlashEncoded(id);
      try {
         this.novaClient.getImageClientForZone(zoneAndId.getZone()).deleteImage(zoneAndId.getId());
      } catch (Exception e) {
         return false;
      }
      return true;
   }

   private org.jclouds.openstack.nova.v1_1.domain.Image findImage(final ZoneAndId zoneAndId) {
      return Iterables.tryFind(novaClient.getImageClientForZone(zoneAndId.getZone()).listImagesInDetail(),
               new Predicate<org.jclouds.openstack.nova.v1_1.domain.Image>() {
                  @Override
                  public boolean apply(org.jclouds.openstack.nova.v1_1.domain.Image input) {
                     return input.getId().equals(zoneAndId.getId());
                  }
               }).orNull();

   }

}
