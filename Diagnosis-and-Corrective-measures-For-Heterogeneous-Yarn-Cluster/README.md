 Diagnosis And Corrective measures For Heterogeneous Yarn Cluster
 
    This repository contains following files :
        - Base hadoop 2.6.0 source code.
        - Changes made for diagnosis and corrective measures required for this project.
     
   
   To compile and package all the source code inside hadoop folder use ,
   
   ``` mvn package -Pdist,native -DskipTests=true -Dtar ``` 
   
   This will create required ready-to-use hadoop code distribution in 
   
   " GIT_CLONED_DIRECTORY/hadoop/hadoop-dist/target/hadoop-2.6.0.tar.gz "
   
   
