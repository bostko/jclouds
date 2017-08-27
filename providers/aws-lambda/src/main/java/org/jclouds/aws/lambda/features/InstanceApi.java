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
package org.jclouds.aws.lambda.features;

import static org.jclouds.aws.reference.FormParameters.ACTION;

import javax.inject.Named;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.jclouds.Fallbacks.EmptySetOnNotFoundOr404;
import org.jclouds.aws.lambda.binders.BindInstanceIdsToIndexedFormParams;
import org.jclouds.aws.lambda.domain.FunctionCode;
import org.jclouds.aws.lambda.xml.AWSDescribeInstancesResponseHandler;
import org.jclouds.aws.lambda.xml.AWSRunInstancesResponseHandler;
import org.jclouds.aws.filters.FormSigner;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.location.functions.RegionToEndpointOrProviderIfNull;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.EndpointParam;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.FormParams;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.VirtualHost;
import org.jclouds.rest.annotations.XMLResponseParser;

import com.google.common.collect.Multimap;

/**
 * Provides access to EC2 Instance Services via their REST API.
 * <p/>
 */
@RequestFilters(FormSigner.class)
@VirtualHost
public interface InstanceApi {

   @Named("DescribeInstances")
   @POST
   @Path("/")
   @FormParams(keys = ACTION, values = "DescribeInstances")
   @XMLResponseParser(AWSDescribeInstancesResponseHandler.class)
   @Fallback(EmptySetOnNotFoundOr404.class)
   Set<? extends Reservation<? extends FunctionCode>> describeInstancesInRegion(
            @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region,
            @BinderParam(BindInstanceIdsToIndexedFormParams.class) String... instanceIds);

   @Named("DescribeInstances")
   @POST
   @Path("/")
   @FormParams(keys = ACTION, values = "DescribeInstances")
   @XMLResponseParser(AWSDescribeInstancesResponseHandler.class)
   @Fallback(EmptySetOnNotFoundOr404.class)
   Set<? extends Reservation<? extends FunctionCode>> describeInstancesInRegionWithFilter(
           @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region,
           @BinderParam(BindInstanceIdsToIndexedFormParams.class) Multimap<String, String> filter);

   @Named("RunInstances")
   @Override
   @POST
   @Path("/")
   @FormParams(keys = ACTION, values = "RunInstances")
   @XMLResponseParser(AWSRunInstancesResponseHandler.class)
   Reservation<? extends FunctionCode> runInstancesInRegion(
            @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region,
            @Nullable @BinderParam(IfNotNullBindAvailabilityZoneToFormParam.class) String nullableAvailabilityZone,
            @FormParam("ImageId") String imageId, @FormParam("MinCount") int minCount,
            @FormParam("MaxCount") int maxCount, RunInstancesOptions... options);

}
