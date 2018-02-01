# Java Mojang API
This repository provides a full Java-wrapper for the Mojang Web-API.
<br/>
More information on the API may be found on the [Wiki page](http://wiki.vg/Mojang_API).

## Getting Started
* Clone the repository and compile it with Maven and add it to your build path.
```
$    git clone https://github.com/SparklingComet/java-mojang-api.git
$    mvn clean install
```

* Alternatively, you can use Maven to include the dependency in your project:
```xml
<project>
    <!-- Your stuff here... -->
    <repositories>
        <!-- More repositories... -->
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
    </repositories>
	
    <dependencies>
	<!-- More dependencies... -->
	<dependency>
    	    <groupId>com.github.SparklingComet</groupId>
    	    <artifactId>java-mojang-api</artifactId>
    	    <version>-SNAPSHOT</version>
    	</dependency>
    </dependencies>
</project>
```

* You can also use Gradle as a dependency manager:
```gradle
allprojects {
    repositories {
	...
	maven { url 'https://jitpack.io' }
    }
}
	
dependencies {
    compile 'com.github.SparklingComet:java-mojang-api:-SNAPSHOT'
}
```

* More information on using dependency managers with this repository may be found [on Jitpack](https://jitpack.io/#SparklingComet/java-mojang-api).
* Precompiled binaries will be made available on our CI server soon.

## Usage
Once you have imported the library, use the following to establish a connection with Mojang's servers.
```java
Mojang api = new Mojang().connect();
```
You can ping the api like this:
```java
if (api.getStatus(Mojang.ServiceType.AUTH_MOJANG_COM) != Mojang.ServiceStatus.GREEN) {
	System.err.println("The Auth Server is not available right now.");
}
```
to check whether authentication servers are currently down.

## Documentation
Javadocs are available for download in HTML-format in the `docs/` directory.
We will make them available online soon.

## License
The project is licensed under the Apache License v2.0 ([view](https://github.com/SparklingComet/java-mojang-api/blob/master/LICENSE)).

To license your own project with it, add the file its root and prefix your source files with
```java
/*
 *     Copyright 2016-2017 SparklingComet @ http://shanerx.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
```
