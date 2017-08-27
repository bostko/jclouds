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
package org.jclouds.aws.lambda.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import org.jclouds.javax.annotation.Nullable;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 
 * @see <a href=
 *      "http://docs.aws.amazon.com/lambda/latest/dg/API_GetFunction.html"
 *      />
 */
public class FunctionCode {
   
   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return new Builder().fromFunctionCode(this);
   }
   
   public static class Builder {
      protected String region;
      protected String instanceId;
      protected String ipAddress;

      protected String runtime;

      protected String role;

      protected String handler;

      protected Number memorySize;

      protected Number timeout;

      public Builder region(String region) {
         this.region = region;
         return this;
      }

      public Builder instanceId(String instanceId) {
         this.instanceId = instanceId;
         return this;
      }

      public Builder ipAddress(String ipAddress) {
         this.ipAddress = ipAddress;
         return this;
      }

      public Builder runtime(String runtime) {
         this.runtime = runtime;
         return this;
      }

      public Builder role(String role) {
         this.role = role;
         return this;
      }

      public Builder handler(String handler) {
         this.handler = handler;
         return this;
      }

      public Builder memorySize(Number memorySize) {
         this.memorySize = memorySize;
         return this;
      }

      public Builder timeout(Number timeout) {
         this.timeout = timeout;
         return this;
      }

      public FunctionCode build() {
         return new FunctionCode(region, null, instanceId,
                 ipAddress, runtime, role, timeout, memorySize, handler);
      }

      public Builder fromFunctionCode(FunctionCode in) {
         return region(in.region)
                 .instanceId(in.instanceId)
                 .ipAddress(in.ipAddress)
                 .runtime(in.runtime)
                 .memorySize(in.memorySize)
                 .handler(in.handler)
                 .role(in.role)
                 .timeout(in.timeout);
      }
   }

   protected final String region;
   @Nullable
   protected final String instanceId;

   @Nullable
   protected final String ipAddress;

   protected final String runtime;

   protected final String role;

   protected final String handler;

   protected final Number memorySize;

   @Nullable
   protected final Number timeout;

   private final Map<String, String> securityGroupIdToNames;

   protected FunctionCode(String region, Map<String, String> securityGroupIdToNames, String instanceId,
                          String ipAddress, String runtime, String role, Number timeout,
                          Number memorySize, String handler) {
      this.region = region;
      this.instanceId = instanceId;
      this.ipAddress = ipAddress;
      this.runtime = runtime;
      this.memorySize = memorySize;
      this.handler = handler;
      this.role = role;
      this.timeout = timeout;
      this.securityGroupIdToNames = ImmutableMap.<String, String> copyOf(checkNotNull(securityGroupIdToNames,
            "securityGroupIdToNames"));
   }

   public Map<String, String> getSecurityGroupIdToNames() {
      return securityGroupIdToNames;
   }

   // TODO
   @Override
   public int hashCode() {
      return Objects.hashCode(region, instanceId);
   }

   // TODO
   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null || getClass() != obj.getClass())
         return false;
      FunctionCode that = FunctionCode.class.cast(obj);
      return Objects.equal(this.region, that.region) && Objects.equal(this.instanceId, that.instanceId);
   }

   public String getRegion() {
      return region;
   }

   public String getInstanceId() {
      return instanceId;
   }

   public String getIpAddress() {
      return ipAddress;
   }

   public String getRuntime() {
      return runtime;
   }

   public String getRole() {
      return role;
   }

   public String getHandler() {
      return handler;
   }

   public Number getMemorySize() {
      return memorySize;
   }

   public Number getTimeout() {
      return timeout;
   }

   protected ToStringHelper string() {
      return MoreObjects.toStringHelper(this).add("region", region).add("instanceId", instanceId)
              .add("ipAddress", ipAddress)
              .add("runtime", runtime)
              .add("role", role)
              .add("handler", handler)
              .add("memorySize", memorySize)
              .add("timeout", timeout)
              .add("memorySize", memorySize);
   }

   @Override
   public String toString() {
      return string().toString();
   }
}
