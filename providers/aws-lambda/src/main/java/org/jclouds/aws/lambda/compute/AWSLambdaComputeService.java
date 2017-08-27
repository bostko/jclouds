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
package org.jclouds.aws.lambda.compute;


import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.util.concurrent.ListenableFuture;
import org.jclouds.aws.lambda.AWSLambdaApi;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.RunScriptOnNodesException;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.ExecResponse;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.compute.extensions.ImageExtension;
import org.jclouds.compute.extensions.SecurityGroupExtension;
import org.jclouds.compute.options.RunScriptOptions;
import org.jclouds.compute.options.TemplateOptions;
import org.jclouds.domain.Location;
import org.jclouds.scriptbuilder.domain.Statement;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Set;

@Singleton
public class AWSLambdaComputeService implements ComputeService {

   private final AWSLambdaApi client;

   @Inject
   protected AWSLambdaComputeService(AWSLambdaApi client) {
      this.client = client;
   }

   @Override
   public ComputeServiceContext getContext() {
      return null;
   }

   @Override
   public TemplateBuilder templateBuilder() {
      return null;
   }

   @Override
   public TemplateOptions templateOptions() {
      return null;
   }

   @Override
   public Set<? extends Hardware> listHardwareProfiles() {
      return null;
   }

   @Override
   public Set<? extends Image> listImages() {
      return null;
   }

   @Override
   public Image getImage(String id) {
      return null;
   }

   @Override
   public Set<? extends ComputeMetadata> listNodes() {
      return null;
   }

   @Override
   public Set<? extends ComputeMetadata> listNodesByIds(Iterable<String> ids) {
      return null;
   }

   @Override
   public Set<? extends Location> listAssignableLocations() {
      return null;
   }

   @Override
   public Set<? extends NodeMetadata> createNodesInGroup(String group, int count, Template template) throws RunNodesException {
      return null;
   }

   @Override
   public Set<? extends NodeMetadata> createNodesInGroup(String group, int count, TemplateOptions templateOptions) throws RunNodesException {
      return null;
   }

   @Override
   public Set<? extends NodeMetadata> createNodesInGroup(String group, int count) throws RunNodesException {
      return null;
   }

   @Override
   public void resumeNode(String id) {

   }

   @Override
   public Set<? extends NodeMetadata> resumeNodesMatching(Predicate<? super NodeMetadata> filter) {
      return null;
   }

   @Override
   public void suspendNode(String id) {

   }

   @Override
   public Set<? extends NodeMetadata> suspendNodesMatching(Predicate<? super NodeMetadata> filter) {
      return null;
   }

   @Override
   public void destroyNode(String id) {

   }

   @Override
   public Set<? extends NodeMetadata> destroyNodesMatching(Predicate<? super NodeMetadata> filter) {
      return null;
   }

   @Override
   public void rebootNode(String id) {

   }

   @Override
   public Set<? extends NodeMetadata> rebootNodesMatching(Predicate<? super NodeMetadata> filter) {
      return null;
   }

   @Override
   public NodeMetadata getNodeMetadata(String id) {
      return null;
   }

   @Override
   public Set<? extends NodeMetadata> listNodesDetailsMatching(Predicate<? super NodeMetadata> filter) {
      return null;
   }

   @Override
   public Map<? extends NodeMetadata, ExecResponse> runScriptOnNodesMatching(Predicate<? super NodeMetadata> filter, String runScript) throws RunScriptOnNodesException {
      return null;
   }

   @Override
   public Map<? extends NodeMetadata, ExecResponse> runScriptOnNodesMatching(Predicate<? super NodeMetadata> filter, Statement runScript) throws RunScriptOnNodesException {
      return null;
   }

   @Override
   public Map<? extends NodeMetadata, ExecResponse> runScriptOnNodesMatching(Predicate<? super NodeMetadata> filter, String runScript, RunScriptOptions options) throws RunScriptOnNodesException {
      return null;
   }

   @Override
   public Map<? extends NodeMetadata, ExecResponse> runScriptOnNodesMatching(Predicate<? super NodeMetadata> filter, Statement runScript, RunScriptOptions options) throws RunScriptOnNodesException {
      return null;
   }

   @Override
   public ExecResponse runScriptOnNode(String id, Statement runScript, RunScriptOptions options) {
      return null;
   }

   @Override
   public ListenableFuture<ExecResponse> submitScriptOnNode(String id, String runScript, RunScriptOptions options) {
      return null;
   }

   @Override
   public ListenableFuture<ExecResponse> submitScriptOnNode(String id, Statement runScript, RunScriptOptions options) {
      return null;
   }

   @Override
   public ExecResponse runScriptOnNode(String id, Statement runScript) {
      return null;
   }

   @Override
   public ExecResponse runScriptOnNode(String id, String runScript, RunScriptOptions options) {
      return null;
   }

   @Override
   public ExecResponse runScriptOnNode(String id, String runScript) {
      return null;
   }

   @Override
   public Optional<ImageExtension> getImageExtension() {
      return null;
   }

   @Override
   public Optional<SecurityGroupExtension> getSecurityGroupExtension() {
      return Optional.absent();
   }

}
