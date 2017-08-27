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

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import org.jclouds.Constants;
import org.jclouds.aws.lambda.compute.AWSLambdaComputeServiceContext;
import org.jclouds.aws.lambda.compute.config.AWSEC2ComputeServiceContextModule;
import org.jclouds.aws.lambda.config.AWSEC2HttpApiModule;
import org.jclouds.rest.internal.BaseHttpApiMetadata;

import java.net.URI;
import java.util.Properties;

public final class AWSLambdaApiMetadata extends BaseHttpApiMetadata<AWSLambdaApi> {

   @Override
   public Builder toBuilder() {
      return new Builder().fromApiMetadata(this);
   }

   public AWSLambdaApiMetadata() {
      super(new Builder());
   }

   protected AWSLambdaApiMetadata(Builder builder) {
      super(builder);
   }

   public static Properties defaultProperties() {
      Properties properties = BaseHttpApiMetadata.defaultProperties();
      // auth fail sometimes happens in EC2, as the rc.local script that injects the
      // authorized key executes after ssh has started.  
      properties.setProperty("jclouds.ssh.max-retries", "7");
      properties.setProperty("jclouds.ssh.retry-auth", "true");
      // required for custom retry handler 
      properties.setProperty(Constants.PROPERTY_IDEMPOTENT_METHODS, "DELETE,GET,HEAD,OPTIONS,PUT,POST");
      return properties;
   }

   public static final class Builder extends BaseHttpApiMetadata.Builder<AWSLambdaApi, Builder> {
      public Builder() {
         id("aws-ec2")
         .version("2012-06-01")
         .name("Amazon-specific EC2 API")
         .identityName("Access Key ID")
         .credentialName("Secret Access Key")
         .defaultEndpoint("https://ec2.us-east-1.amazonaws.com")
         .documentation(URI.create("http://docs.amazonwebservices.com/AWSEC2/latest/APIReference"))
         .defaultProperties(AWSLambdaApiMetadata.defaultProperties())
         .defaultModules(ImmutableSet.<Class<? extends Module>>of(AWSEC2HttpApiModule.class, AWSEC2ComputeServiceContextModule.class));
      }

      @Override
      public AWSLambdaApiMetadata build() {
         return new AWSLambdaApiMetadata(this);
      }

      @Override
      protected Builder self() {
         return this;
      }
   }
}
