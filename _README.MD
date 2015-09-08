# Quick Overview
Simple facade to wrap executor services into SLF4J MDC aware proxies. See the <a href="http://logback.qos.ch/manual/mdc.html#managedThreads">logback documentation for more information about mapped diagnostic context. An executor proxy created through this facade will wrap any java.lang.Runnable or java.util.concurrent.Callable task into a MDC-aware wrapper. This wrapper will copy the MDC context from the master thread (the thread which committed the task to the executor) to the pool-thread (the thread which executes the task) before the original task is actually executed. After execution, the wrapper will cleanup the MDC context on the pool-thread to avoid memory leaks.

## What's new

### 1.0.0
- First public release

## Installation
The plugin is available in [Maven Central Repository](http://search.maven.org/#artifactdetails|ch.sourcepond.utils|mdcwrapper|1.0.0-SNAPSHOT|bundle). To use it in your project, add following plugin definition to the build part of your pom.xml:
```
<plugin>
	<groupId>ch.sourcepond.utils</groupId>
	<artifactId>mdcwrapper</artifactId>
	<version>1.0.0-SNAPSHOT</version>
</plugin>
```

## Examples
You can find the full source code of the examples below in the *examples* directory.

### Show help for create-job (examples/execute-from-cmd)
To show the help message for Jenkins command "create-job" from the command line add following snipped to your pom.xml:
```
<plugin>
	<groupId>ch.sourcepond.maven.plugins</groupId>
	<artifactId>jenkins-maven-plugin</artifactId>
	<executions>
		<execution>
			<!-- Special Maven executionId which allows to run the plugin from the 
				command line without defining a lifecycle phase. -->
			<id>default-cli</id>
		</execution>
	</executions>
</plugin>
```
And execute following command in your project folder (where the pom.xml is located):
```
mvn jenkins:cli -Djenkins.command="help create-job"
```
You can also define your Jenkins baseURL like this:
```
mvn jenkins:cli -Djenkins.baseUrl=http://[HOST]:[PORT]/[JENKINS PATH] -Djenkins.command=[CMD]
```

### Create a new job on Jenkins (examples/create-job)
```
<plugin>
	<groupId>ch.sourcepond.maven.plugins</groupId>
	<artifactId>jenkins-maven-plugin</artifactId>
	<executions>
		<execution>
			<id>create-job</id>
			<goals>
				<goal>cli</goal>
			</goals>
			<configuration>
				<!-- Create a Jenkins job and specify the artifactId of this project 
					as name -->
				<command>create-job ${project.artifactId}</command>

				<!-- Let stdin read from the job configuration template file -->
				<stdin>${configTemplate}</stdin>

				<!-- Specify the XSLT file to be used to transform the data read from 
					stdin -->
				<stdinXsltFile>${xslt}</stdinXsltFile>

				<!-- If your XSLT is available from a Maven repository it's better to 
					use its coordinates to let the plugin download the XSLT file for you instead 
					of specifying a custom file with "stdinXsltFile". Note: you cannot specify 
					both ("stdinXsltFile" and "stdinXsltCoords") -->
				<!-- <stdinXsltCoords>xslt.groupid:xslt.artifactid:1.0.0</stdinXsltCoords> -->

				<!-- Specify the parameters which are used by the XSLT -->
				<stdinXsltParams>
					<description>${project.description}</description>
					<giturl>${project.scm.connection}</giturl>
					<credentialsId>${credentialsId}</credentialsId>
					<groupId>${project.groupId}</groupId>
					<artifactId>${project.artifactId}</artifactId>
				</stdinXsltParams>
			</configuration>
		</execution>
	</executions>
</plugin>
```