/**
 * (c) Copyright 2013 WibiData, Inc.
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kiji.rest.resources;


import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.kiji.rest.core.ContentReturnable;
import org.kiji.rest.core.ElementReturnable;
import org.kiji.rest.core.Returnable;

import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;

/**
 * This REST resource interacts with Kiji instances.
 * This resource is served whenever requests are made using the following
 * resource identifiers: /v1/instances/, /v1/instances/&lt;singleton&gt;,
 * /v1/instances/&lt;instance&gt;.
 */
@Path(KijiRESTResource.API_ENTRY_POINT + InstanceResource.INSTANCES)
@Produces(MediaType.APPLICATION_JSON)
public class InstanceResource {
  /** The namespace for instances. */
  public static final String INSTANCES = "instances/";

  /**
   * Called when the terminal resource element is not identified.
   * @return a Returnable message indicating the landing point.
   */
  @GET
  @Timed
  public Returnable namespace() {
    ContentReturnable message = new ContentReturnable("Instances");
    message.add(new ElementReturnable("Kiji"));
    return message;
  }

  /**
   * Called when the terminal resource element is the singleton 'version'.
   * @return a Returnable message indicating the version.
   */
  @Path("version")
  @GET
  @Timed
  public Returnable version() {
    ContentReturnable version = new ContentReturnable("Version");
    version.add(new ElementReturnable("0.0.1"));
    return version;
  }

  /**
   * Called when the terminal resource element is the instance name.
   * @return a Returnable message indicating the landing point.
   */
  @Path("{instance}")
  @GET
  @Timed
  public Returnable instance(final @PathParam("instance") String instance) {
    ContentReturnable message = new ContentReturnable("instance: " + instance);
    message.add(new ElementReturnable("0.0.1"));
    return message;
  }
}


