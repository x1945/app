package app.test;


import java.util.Date;
import com.kapowtech.robosuite.api.java.rql.*;
import com.kapowtech.robosuite.api.java.rql.construct.*;
import com.kapowtech.robosuite.api.java.repository.engine.*;

public class TestKapow {

    public static void main(String[] args) throws RQLException, RepositoryClientException, InterruptedException {

        // Get the cluster info through the repository API, and register the cluster for Requests
        String repositoryUserName = null;
        String repositoryPassword = null;
        String repositoryURL = "http://localhost:50080";
        String clusterName = "Non Production";
        RepositoryClient client = RepositoryClientFactory.createRepositoryClient(repositoryURL, repositoryUserName, repositoryPassword);
        Request.registerCluster(client, clusterName);

        // Create the Request
        Request request = new Request("Library://returnValue.robot");
        request.setStopOnConnectionLost(true);
        request.setStopRobotOnApiException(true);
        request.setMaxExecutionTime(3600); // maximum 1 hour


        // Use the repository as Robot Library
        RepositoryRobotLibrary lib = new RepositoryRobotLibrary(repositoryURL, "Training", 60000L, repositoryUserName, repositoryPassword);
        request.setRobotLibrary(lib);
        // execute the request, and process the result if one is returned
        RQLResult result = request.execute(clusterName);
        RQLObjects results = result.getOutputObjectsByName("result");
        if (results.size() != 0) {
            for (int i = 0; i < results.size(); i++) {
                // Get the values for each of the attributes in this output object
                RQLObject rolObject = results.getObject(i);
                String url = (String) rolObject.get("url");
                String title = (String) rolObject.get("title");
                String content = (String) rolObject.get("content");
                System.out.println("url:"+url);
                System.out.println("title:"+title);
                System.out.println("content:"+content);
                // Do something with the extracted data
            }
        }
    }
}