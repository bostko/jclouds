/*
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
package org.jclouds.aws.lambda.compute.config;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.jclouds.aws.lambda.reference.AWSEC2Constants.PROPERTY_EC2_AMI_QUERY;
import static org.jclouds.aws.lambda.reference.AWSEC2Constants.PROPERTY_EC2_CC_AMI_QUERY;
import static org.jclouds.ec2.reference.EC2Constants.PROPERTY_EC2_AMI_OWNERS;
import static org.jclouds.util.Predicates2.retry;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Named;
import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import org.jclouds.aws.lambda.compute.AWSLambdaComputeService;
import org.jclouds.aws.lambda.predicates.PlacementGroupAvailable;
import org.jclouds.aws.lambda.predicates.PlacementGroupDeleted;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.compute.extensions.ImageExtension;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.config.ValueOfConfigurationKeyOrNull;
import org.jclouds.domain.LoginCredentials;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

public class AWSEC2ComputeServiceDependenciesModule extends AbstractModule {
   @Override
   protected void configure() {
      bind(TemplateBuilder.class).to(EC2TemplateBuilderImpl.class);
      bind(TemplateOptions.class).to(AWSEC2TemplateOptions.class);
      bind(ComputeService.class).to(AWSLambdaComputeService.class);
      bind(new TypeLiteral<CacheLoader<RunningInstance, Optional<LoginCredentials>>>() {
      }).to(CredentialsForInstance.class);
      bind(new TypeLiteral<CacheLoader<RegionAndName, String>>() {
      }).annotatedWith(Names.named("SECURITY")).to(AWSEC2CreateSecurityGroupIfNeeded.class);
      bind(new TypeLiteral<CacheLoader<RegionAndName, String>>() {
      }).annotatedWith(Names.named("ELASTICIP")).to(LoadPublicIpForInstanceOrNull.class);
      bind(new TypeLiteral<Function<String, String>>() {
      }).annotatedWith(Names.named("SECGROUP_NAME_TO_ID")).to(EC2SecurityGroupIdFromName.class);
      bind(new TypeLiteral<ImageExtension>() {
      }).to(EC2ImageExtension.class);
   }

   @Provides
   @Singleton
   @ImageQuery
   protected final Map<String, String> imageQuery(ValueOfConfigurationKeyOrNull config) {
      String amiQuery = Strings.emptyToNull(config.apply(PROPERTY_EC2_AMI_QUERY));
      String owners = config.apply(PROPERTY_EC2_AMI_OWNERS);
      if ("".equals(owners)) {
         amiQuery = null;
      } else if (owners != null) {
         StringBuilder query = new StringBuilder();
         if ("*".equals(owners))
            query.append("state=available;image-type=machine");
         else
            query.append("owner-id=").append(owners).append(";state=available;image-type=machine");
         Logger.getAnonymousLogger().warning(
               String.format("Property %s is deprecated, please use new syntax: %s=%s", PROPERTY_EC2_AMI_OWNERS,
                     PROPERTY_EC2_AMI_QUERY, query.toString()));
         amiQuery = query.toString();
      }
      Builder<String, String> builder = ImmutableMap.<String, String> builder();
      if (amiQuery != null)
         builder.put(PROPERTY_EC2_AMI_QUERY, amiQuery);
      String ccQuery = Strings.emptyToNull(config.apply(PROPERTY_EC2_CC_AMI_QUERY));
      if (ccQuery != null)
         builder.put(PROPERTY_EC2_CC_AMI_QUERY, ccQuery);
      return builder.build();
   }

   @Provides
   @Singleton
   @Named("AVAILABLE")
   protected final Predicate<PlacementGroup> placementGroupAvailable(PlacementGroupAvailable available) {
      return retry(available, 60, 1, SECONDS);
   }

   @Provides
   @Singleton
   @Named("DELETED")
   protected final Predicate<PlacementGroup> placementGroupDeleted(PlacementGroupDeleted deleted) {
      return retry(deleted, 60, 1, SECONDS);
   }

   @Provides
   @Singleton
   @Named("PLACEMENT")
   protected final LoadingCache<RegionAndName, String> placementGroupMap(CreatePlacementGroupIfNeeded in) {
      return CacheBuilder.newBuilder().build(in);
   }

   @Provides
   @ClusterCompute
   @Singleton
   protected final Set<String> provideClusterComputeIds() {
      return Sets.newLinkedHashSet();
   }

}
