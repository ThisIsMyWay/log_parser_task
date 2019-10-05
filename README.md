# log_parser_task

The goal was to write an application, which can parse large log file. 

Each log has:
- id 
- state (can have values "STARTED" or "FINISHED" )
- timestamp.

Optionally, log can also have:
- type 
- host

Snippet of examplary log file:
```
{"id":"321dad4242", "state":"STARTED", "type":"BACKEND_LOG","host":"10.10.10.10", "timestamp":1364524565666}
{"id":"dasd3214223", "state":"FINISHED", "timestamp":1364524565687}
{"id":"dasd3214223", "state":"STARTED", "timestamp":1364524565686}
{"id":"321dad4242", "state":"FINISHED", "type":"BACKEND_LOG","host":"10.10.10.10", "timestamp":1364524565672}
```

The events, expressed in the log file, which are longer then 4ms should be flagged.  

The result of parsing should be written to file-based HSQLDB.

Application can be run from command line, by typing:
```
gradlew run --args="<here path to log file which sould be parsed>"
```
