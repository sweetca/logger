# [Logs Monitor](https://github.com/sweetca/logger)

## License 
**MIT License**

This application can be usefull for reading logs on server in real time.
Main possibilities here : fast set up and start on server, friendly UI with real time update support!

If you work in "single file mode" it can easily monitoring changes in log that even faster that 100ms 

## Requirements :
**1.** java 8

**2.** node.js version > 7

**3.** npm version > 4

**4.** maven3

## Tech Stack :

**1.** Spring boot

**2.** Apache Tomcat (out of the box)

**3.** WebSockets

**4.** React

**5.** Redux

## Set up:

**1.** Update up backend in file `~/src/main/resources/application.properties`:

- **server.port** is a host port here Tomcat will start
- **bufferSize** default buffer size for back end in memory storage of logs
- **files.to.watch** log files real path - comma separated
- **rw.enabled** if you want to test height load of system you can enable writer bean **`RwFile.java`**

**2.** Update frontend in file `~/frontend/.env`:

- **REACT_APP_API_URL** is a full URL of backend host
- **REACT_APP_DEBUG** if you want enable redux `redux-logger` to debug in UI set true

**3.** Customize log parser strategy:

- **com.logger.model.Log.parseLog(String data)** method should be updated

## How to build:
**`sh build.sh`**

## How to run (after build only):
**`sh run.sh`**