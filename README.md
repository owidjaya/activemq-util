##Message Broker(s) Utility 


How to use
1. run `mvn package`
2. copy application.yml in src/main/java/resources to application root folder
3. configure the application.yml, most interesting part is the transfer section in the file
4. run `java -jar target/message-transfer-1.0.0.jar <COMMAND>` from target

Available `<COMMAND>`s:
1. FILE_TO_QUEUE: this will ingest the file line by line to push to the queue 
2. QUEUE_TO_FILE: this will ingest the queue to put in to a file as configured
3. QUEUE_TO_QUEUE: this will move messages from one broker to another 

**Caveat**:
currently the QUEUE_TO_FILE and QUEUE_TO_QUEUE needs to be Ctrl+c at the end to disconnect.
