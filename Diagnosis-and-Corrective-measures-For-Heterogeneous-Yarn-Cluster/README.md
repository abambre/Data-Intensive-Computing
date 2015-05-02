 Diagnosis And Corrective measures For Heterogeneous Yarn Cluster
 
    This repository contains following files :
        - Base hadoop 2.6.0 source code.
        - Changes made for diagnosis and corrective measures required for this project.
     
   
   To compile and package all the source code inside hadoop folder use ,
   
   ``` mvn package -Pdist,native -DskipTests=true -Dtar ``` 
   
   This will create required ready-to-use hadoop code [hadoop-2.6.0.tar.gz] distribution in 
   
   " GIT_CLONED_DIRECTORY/hadoop/hadoop-dist/target/ "
   
   
   Currently following configuration details (Need to provide / Hard-coded ) :
  
    Threshold which triggers decommissioning at Resource manager : 1
      
    To dynamically add datanode inside the cluster , VM specific information need to be provided in,
         hadoop/bringNewNodeUp.sh 
      
  On Resource manager, this script need to be placed in /usr/local/hadoop/logs/ directory with proper permissions.
