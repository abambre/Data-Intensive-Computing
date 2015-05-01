/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.yarn.api.protocolrecords;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.hadoop.classification.InterfaceAudience.Public;
import org.apache.hadoop.classification.InterfaceStability.Stable;
import org.apache.hadoop.yarn.api.ApplicationMasterProtocol;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;
import org.apache.hadoop.yarn.api.records.ResourceBlacklistRequest;
import org.apache.hadoop.yarn.api.records.ContainerResourceIncreaseRequest;
import org.apache.hadoop.yarn.api.records.ResourceRequest;
import org.apache.hadoop.yarn.util.Records;
import org.mortbay.log.Log;

/**
 * <p>The core request sent by the <code>ApplicationMaster</code> to the 
 * <code>ResourceManager</code> to obtain resources in the cluster.</p> 
 *
 * <p>The request includes:
 *   <ul>
 *     <li>A response id to track duplicate responses.</li>
 *     <li>Progress information.</li>
 *     <li>
 *       A list of {@link ResourceRequest} to inform the 
 *       <code>ResourceManager</code> about the application's 
 *       resource requirements.
 *     </li>
 *     <li>
 *       A list of unused {@link Container} which are being returned. 
 *     </li>
 *   </ul>
 * </p>
 * 
 * @see ApplicationMasterProtocol#allocate(AllocateRequest)
 */
@Public
@Stable
public abstract class AllocateRequest {

  static String filePath="/usr/local/hadoop/logs/nodedetails.properties";
  @Public
  @Stable
  public static AllocateRequest newInstance(int responseID, float appProgress,
      List<ResourceRequest> resourceAsk,
      List<ContainerId> containersToBeReleased,
      ResourceBlacklistRequest resourceBlacklistRequest) {
    return newInstance(responseID, appProgress, resourceAsk,
        containersToBeReleased, resourceBlacklistRequest, null);
  }
  
  public static Object[] readFromFile()
  {	
	  Log.info("\n\n\n Inside the readfromfile method \n\n");
	  File f = new File(filePath);
	  Object  nodeinfo[]=null; 
	  if(f.exists() && !f.isDirectory()) 
	  {
		Log.info("Inside the file exist condition before populating file");
	  	Properties prop = new Properties();
	  	InputStream input = null;

	  	try 
	  	{
	  		
	  		input = new FileInputStream(filePath);
	  		nodeinfo=new Object[2];
	  		// load a properties file
	  		prop.load(input);
	  		nodeinfo[0]=prop.getProperty("host");
	  		nodeinfo[1]=prop.getProperty("port");
	  		
	  		Log.info("\n\nPrinting Host in readfile "+nodeinfo[0]+"Port :" +nodeinfo[1]);
	  	 
	  	}
	  	catch (IOException ex) 
	  	{
	  		ex.printStackTrace();
	  	}
	  	finally 
	  	{
	  		if (input != null) 
	  		{
	  			try 
	  			{
	  				input.close();
	  			}
	  			catch (IOException e)
	  			{
	  				e.printStackTrace();
	  			}
	  		}
	  	}
	  }
	  
	  Log.info("\n\n\nExisiting from fileread function\n\n");
	  return nodeinfo;
	  
  }
  
  public static void deleteFile(String strFileName)
  {
    Log.info("\n\n isndie the delete file \n");
    
  	try
  	{
  		File file = new File(strFileName);
  		if(file.delete())
  			Log.info("\n\n File deleted succeess \n\n");
  		
  	}
  	catch(Exception e)
  	{
  		Log.info("Priitng erro stack");
  	}
  }
  
  @Public
  @Stable
  public static AllocateRequest newInstance(int responseID, float appProgress,
      List<ResourceRequest> resourceAsk,
      List<ContainerId> containersToBeReleased,
      ResourceBlacklistRequest resourceBlacklistRequest,
      List<ContainerResourceIncreaseRequest> increaseRequests) {
	
    AllocateRequest allocateRequest = Records.newRecord(AllocateRequest.class);
    allocateRequest.setResponseId(responseID);
    allocateRequest.setProgress(appProgress);
    allocateRequest.setAskList(resourceAsk);
    allocateRequest.setReleaseList(containersToBeReleased);
    allocateRequest.setResourceBlacklistRequest(resourceBlacklistRequest);
    allocateRequest.setIncreaseRequests(increaseRequests);
    
    Object nodeinfo[]=readFromFile();
    if(nodeinfo !=null)
    {
    	Log.info("\n\n\nFound file in newinstance \n\n");
    	allocateRequest.setSpeculativeNode((String)nodeinfo[0]);
        allocateRequest.setSpeculativeNodePort(Integer.parseInt((String)nodeinfo[1]));
        
        Log.info("\n\nInside trhe allocaterequest newinstance after setting values\n\n");
        Log.info(" Node name"+allocateRequest.getSpeculativeNode()+"Port :" + allocateRequest.getSpeculativeNodePort());
        
        deleteFile(filePath);
    }
    else
    {
    	Log.info("\n\n\nFile not found  \n\n");
    	allocateRequest.setSpeculativeNode("");
    	allocateRequest.setSpeculativeNodePort(-1);
    }
    
    //allocateRequest.setSpeculativeInd(25000);
    Log.info("PRiting allocaterequest in newinstance"+ allocateRequest.getResponseId()+allocateRequest.getProgress());  
    
    return allocateRequest; 
  }
  
  /**
   * Get the <em>response id</em> used to track duplicate responses.
   * @return <em>response id</em>
   */
  @Public
  @Stable
  public abstract int getResponseId();

  /**
   * Set the <em>response id</em> used to track duplicate responses.
   * @param id <em>response id</em>
   */
  @Public
  @Stable
  public abstract void setResponseId(int id);

  /**
   * Get the <em>current speculative information</em> of application. 
   * @return <em>current speculative information</em> of application
   */
  
  
  @Public
  @Stable
  public abstract void setSpeculativeNode(String id);

  @Public
  @Stable
  public abstract String getSpeculativeNode(); 

  @Public
  @Stable
  public abstract void setSpeculativeNodePort(int id);

  @Public
  @Stable
  public abstract int getSpeculativeNodePort(); 
  
  
  
  /**
   * Get the <em>current progress</em> of application. 
   * @return <em>current progress</em> of application
   */
  @Public
  @Stable
  public abstract float getProgress();
  
  /**
   * Set the <em>current progress</em> of application
   * @param progress <em>current progress</em> of application
   */
  @Public
  @Stable
  public abstract void setProgress(float progress);

  /**
   * Get the list of <code>ResourceRequest</code> to update the 
   * <code>ResourceManager</code> about the application's resource requirements.
   * @return the list of <code>ResourceRequest</code>
   * @see ResourceRequest
   */
  @Public
  @Stable
  public abstract List<ResourceRequest> getAskList();
  
  /**
   * Set list of <code>ResourceRequest</code> to update the
   * <code>ResourceManager</code> about the application's resource requirements.
   * @param resourceRequests list of <code>ResourceRequest</code> to update the 
   *                        <code>ResourceManager</code> about the application's 
   *                        resource requirements
   * @see ResourceRequest
   */
  @Public
  @Stable
  public abstract void setAskList(List<ResourceRequest> resourceRequests);

  /**
   * Get the list of <code>ContainerId</code> of containers being 
   * released by the <code>ApplicationMaster</code>.
   * @return list of <code>ContainerId</code> of containers being 
   *         released by the <code>ApplicationMaster</code> 
   */
  @Public
  @Stable
  public abstract List<ContainerId> getReleaseList();

  /**
   * Set the list of <code>ContainerId</code> of containers being
   * released by the <code>ApplicationMaster</code>
   * @param releaseContainers list of <code>ContainerId</code> of 
   *                          containers being released by the 
   *                          <code>ApplicationMaster</code>
   */
  @Public
  @Stable
  public abstract void setReleaseList(List<ContainerId> releaseContainers);
  
  /**
   * Get the <code>ResourceBlacklistRequest</code> being sent by the 
   * <code>ApplicationMaster</code>.
   * @return the <code>ResourceBlacklistRequest</code> being sent by the 
   *         <code>ApplicationMaster</code>
   * @see ResourceBlacklistRequest
   */
  @Public
  @Stable
  public abstract ResourceBlacklistRequest getResourceBlacklistRequest();
  
  /**
   * Set the <code>ResourceBlacklistRequest</code> to inform the 
   * <code>ResourceManager</code> about the blacklist additions and removals
   * per the <code>ApplicationMaster</code>.
   * 
   * @param resourceBlacklistRequest the <code>ResourceBlacklistRequest</code>  
   *                         to inform the <code>ResourceManager</code> about  
   *                         the blacklist additions and removals
   *                         per the <code>ApplicationMaster</code>
   * @see ResourceBlacklistRequest
   */
  @Public
  @Stable
  public abstract void setResourceBlacklistRequest(
      ResourceBlacklistRequest resourceBlacklistRequest);
  
  /**
   * Get the <code>ContainerResourceIncreaseRequest</code> being sent by the
   * <code>ApplicationMaster</code>
   */
  @Public
  @Stable
  public abstract List<ContainerResourceIncreaseRequest> getIncreaseRequests();
  
  /**
   * Set the <code>ContainerResourceIncreaseRequest</code> to inform the
   * <code>ResourceManager</code> about some container's resources need to be
   * increased
   */
  @Public
  @Stable
  public abstract void setIncreaseRequests(
      List<ContainerResourceIncreaseRequest> increaseRequests);
}
