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

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.kiji.rest.core.ContentReturnable;
import org.kiji.rest.core.ElementReturnable;
import org.kiji.rest.core.Returnable;
import static org.kiji.rest.resources.ResourceConstants.API_ENTRY_POINT;
import static org.kiji.rest.resources.ResourceConstants.INSTANCES;
import static org.kiji.rest.resources.ResourceConstants.TABLES;
import org.kiji.schema.Kiji;
import org.kiji.schema.KijiURI;
import org.kiji.schema.KijiURI.KijiURIBuilder;

import com.yammer.metrics.annotation.Timed;

/**
 * This REST resource interacts with Kiji tables..
 * This resource is served whenever requests are made using the following
 * resource identifiers: /v1/instances/&lt;instance&gt/tables,
 * /v1/instances/&lt;instance&gt/tables/&lt;singleton&gt;,
 * /v1/instances/&lt;instance&gt/tables/&lt;table&gt;.
 */
@Path(API_ENTRY_POINT + INSTANCES + "{instance}/" + TABLES)
@Produces(MediaType.APPLICATION_JSON)
public class TableResource {
  private final KijiURI mCluster;
  private final List<KijiURI> mInstances;

  /**
   * Construct the InstanceResource with a partially constructed URI for the cluster.
   *
   * @param clusterURI The builder for the cluster's URI.
   */
  public TableResource(KijiURI cluster, List<KijiURI> instances) {
    super();
    mCluster = cluster;
    mInstances = instances;
  }

  /**
   * Called when the terminal resource element is not identified.
   * @return a Returnable message indicating the landing point.
   */
  @GET
  @Timed
  public Returnable namespace() {
    ContentReturnable message = new ContentReturnable("Tables");
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
   * Called when the terminal resource element is the table name.
   * @return a Returnable message indicating the landing point.
   */
  @Path("{table}")
  @GET
  @Timed
  public Returnable table(final @PathParam("instance") String instance,
      final @PathParam("table") String table) {
    ContentReturnable message = new ContentReturnable("table: " + instance + "/" + table);
    message.add(new ElementReturnable("0.0.1"));
    return message;
  }

  /**
   * Called when the terminal resource element is the 'layout' of the table.
   * @return a JSON table layout.
   */
  @Path("{table}/layout")
  @GET
  @Timed
  public String layout(final @PathParam("instance") String instance,
      final @PathParam("table") String table) throws IOException {
    final KijiURIBuilder kijiURIBuilder = KijiURI.newBuilder(mCluster).withInstanceName(instance);
    if (!mInstances.contains(kijiURIBuilder.build())) {
      throw new IOException("No such resource.");
    }
    final KijiURI kijiURI = kijiURIBuilder.withTableName(table).build();
    final Kiji kiji = Kiji.Factory.open(kijiURI);
    final String layout = kiji
        .getMetaTable()
        .getTableLayout(kijiURI.getTable())
        .getDesc()
        .toString();
    kiji.release();
    return layout;
  }
}

