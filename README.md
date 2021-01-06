# GWTPopupDemo
_A sample project to showcase the GWT client Open ID Connect Flow_

#### This is a maven project and a web-app war can be generated from this project to be deployed.

## To run in tomcat, add the following properties

1. settings.xml => `C:<Tomcat_Download_location>\apache-tomcat-8.5.60\conf\settings.xml`
```xml
<server>
	<id>tomcatserver</id>
	<username>admin</username>
	<password>password</password>
</server>
```

2. tomcat-users.xml =>  `C:<Tomcat_Download_location>\apache-tomcat-8.5.60\conf\tomcat-users.xml`
```xml
<user username="admin" password="password" roles="manager-gui, manager-script"/>
```

3. Then go to the root of this project where pom.xml is availalble and generate the war
```
mvn clean install
```

4. From the same folder path deploy it to the tomcat with
```
mvn tomcat7:deploy
```
Once the war has been deployed, you can access the web-app @ http://localhost:8080/login

![Alt text](src/Client_example.png?raw=true "Sample Screen")
