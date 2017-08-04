package com.gepardec.openshift.restclient.java.sample;

import java.util.Date;

import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IDeploymentConfig;
import com.openshift.restclient.model.IProject;
import com.openshift.restclient.model.IResource;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Begin: "+ new Date().toString() );
        IClient client = new ClientBuilder("https://manage.cloud.itandtel.at")
        		//.withUserName("openshiftdev")
        		//.withPassword("wouldntUlik3T0kn0w")
        		.build();
        
        client.getAuthorizationContext().setToken("<fill in here>");
        
        //IResource request = client.getResourceFactory().stub(ResourceKind.PROJECT, "57-hogarama-old-template");
        //client.getResourceFactory().
        //IProject project =  (IProject)client.get(request);
        
        //IResource req = client.getResourceFactory().stub(ResourceKind.DEPLOYMENT_CONFIG, "hogajama", "57-hogarama-old-template");
        IDeploymentConfig depconfig = (IDeploymentConfig) client.get(ResourceKind.DEPLOYMENT_CONFIG, "hogajama", "57-hogarama-old-template");
        System.out.println( "Get DepConfig: "+ new Date().toString() );
        
        depconfig.setDesiredReplicaCount(3);
        System.out.println( "Set count: "+ new Date().toString() );
        
        client.update(depconfig);
        System.out.println( "Client update: "+ new Date().toString() );
        
        
    }
}
