# Distributed Systems Coursework with the following Major Requirements:
  - Implementing a Client-Server model for an Auctioning System
  - Enabling multiple clients to use the system simultaneously while maintaining a consistent view from the Server
  - Authenticating users through Login & Signup
  - Validating Server Identity using its Digital Certificate
  - Ensuring Fault Tolerance on the Server Side by employing multiple Server Replicas

# Languages & Technologies Used:
 - Java
 - Java RMI
 - JGroups

# How to run the Project:
  - ## Server Side:
     - Navigate to "src/Server" Folder
     - Ensure that the "jgroups-3.6.20.Final.jar" file is present
     - Compile the code: ```javac -cp jgroups-3.6.20.Final.jar;. *.java```
     - In a new terminal, start the RMI Registry with: ```rmiregistry```
     - Run Api-Gateway: ```java -cp jgroups-3.6.20.Final.jar;. ApiGateway```
     	- The Api-Gateway is responsible for receiving client requests
     - Run Server: ```java -cp jgroups-3.6.20.Final.jar;. ApiGateway```
       	- The Server manages the Auction System's business-logic
     - > **Note** <br />
       > For Fault Tolerance in the Auction System, run multiple instances of both the Api-Gateway and Server

  - ## Client Side:
     - Navigate to "src/Client" Folder
     - Compile the code: ```javac *.java```
     - Run Client: ```java Client```

# Project ScreenShoots:
  - Available Options in the Client
    ![image](https://github.com/youssef-gerges-ramzy-mokhtar/Distributed-Systems-Coursework/assets/113933501/d7204f0f-5ddc-46aa-91f1-6c91eaa1e413)

# Final Remarks
  - If you have multiple instances of the server running, you can terminate any of them without affecting the system's functionality, provided that at least one server instance remains running
  - Finally, for more info on the project requirements, and technical info please refer to the "coursework specification" folder and the ```Report.pdf```
