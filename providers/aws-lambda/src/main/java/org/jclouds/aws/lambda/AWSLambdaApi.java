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
package org.jclouds.aws.lambda;

import org.jclouds.aws.lambda.features.AWSAMIApi;
import org.jclouds.aws.lambda.features.InstanceApi;
import org.jclouds.aws.lambda.features.AWSKeyPairApi;
import org.jclouds.aws.lambda.features.AWSSecurityGroupApi;
import org.jclouds.aws.lambda.features.AWSSubnetApi;
import org.jclouds.aws.lambda.features.InternetGatewayApi;
import org.jclouds.aws.lambda.features.MonitoringApi;
import org.jclouds.aws.lambda.features.PlacementGroupApi;
import org.jclouds.aws.lambda.features.RouteTableApi;
import org.jclouds.aws.lambda.features.SpotInstanceApi;
import org.jclouds.aws.lambda.features.VPCApi;
import org.jclouds.ec2.EC2Api;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.location.functions.RegionToEndpointOrProviderIfNull;
import org.jclouds.rest.annotations.Delegate;
import org.jclouds.rest.annotations.EndpointParam;

import com.google.common.base.Optional;

/**
 * Provides synchronous access to EC2 services.
 */
public interface AWSLambdaApi extends EC2Api {

   /**
    * {@inheritDoc}
    */
   @Delegate
   @Override
   Optional<? extends InstanceApi> getInstanceApi();

   @Delegate
   @Override
   Optional<? extends InstanceApi> getInstanceApiForRegion(
           @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region);

   /**
    * {@inheritDoc}
    */
   @Delegate
   @Override
   Optional<? extends AWSSecurityGroupApi> getSecurityGroupApi();

   @Delegate
   @Override
   Optional<? extends AWSSecurityGroupApi> getSecurityGroupApiForRegion(
           @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region);

   /**
    * {@inheritDoc}
    */
   @Delegate
   @Override
   Optional<? extends AWSAMIApi> getAMIApi();

   @Delegate
   @Override
   Optional<? extends AWSAMIApi> getAMIApiForRegion(
           @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region);


   /**
    * Provides synchronous access to PlacementGroup services.
    */
   @Delegate
   Optional<? extends PlacementGroupApi> getPlacementGroupApi();

   @Delegate
   Optional<? extends PlacementGroupApi> getPlacementGroupApiForRegion(
           @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region);

   /**
    * Provides synchronous access to Monitoring services.
    */
   @Delegate
   Optional<? extends MonitoringApi> getMonitoringApi();

   @Delegate
   Optional<? extends MonitoringApi> getMonitoringApiForRegion(
           @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region);

   /**
    * {@inheritDoc}
    */
   @Delegate
   @Override
   Optional<? extends AWSKeyPairApi> getKeyPairApi();

   @Delegate
   @Override
   Optional<? extends AWSKeyPairApi> getKeyPairApiForRegion(
           @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region);

   /**
    * Provides synchronous access to SpotInstance services.
    */
   @Delegate
   Optional<? extends SpotInstanceApi> getSpotInstanceApi();

   @Delegate
   Optional<? extends SpotInstanceApi> getSpotInstanceApiForRegion(
           @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region);

   /**
    * Provides synchronous access to VPC services.
    */
   @Delegate
   Optional<? extends VPCApi> getVPCApi();

   /**
    * Provides access to Subnet services.
    */
   @Delegate
   Optional<? extends AWSSubnetApi> getAWSSubnetApi();

   @Delegate
   Optional<? extends AWSSubnetApi> getAWSSubnetApiForRegion(
           @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region);


   /**
    * Provides synchronous access to InternetGateway services.
    */
   @Delegate
   Optional<? extends InternetGatewayApi> getInternetGatewayApi();

   /**
    * Provides synchronous access to Internet Gateway services in a given region.
    */
   @Delegate
   Optional<? extends InternetGatewayApi> getInternetGatewayApiForRegion(
      @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region
   );

   /**
    * Provides synchronous access to Route Table services.
    */
   @Delegate
   Optional<? extends RouteTableApi> getRouteTableApi();

   /**
    * Provides synchronous access to Route Table services in a given region.
    */
   @Delegate
   Optional<? extends RouteTableApi> getRouteTableApiForRegion(
      @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region
   );
}
