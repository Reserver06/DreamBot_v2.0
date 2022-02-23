@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  discord startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and DISCORD_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\discord-1.0.jar;%APP_HOME%\lib\JDA-4.2.1_255.jar;%APP_HOME%\lib\google-api-services-sheets-v4-rev581-1.25.0.jar;%APP_HOME%\lib\google-api-client-1.30.4.jar;%APP_HOME%\lib\google-oauth-client-jetty-1.30.6.jar;%APP_HOME%\lib\cassandra-all-0.8.1.jar;%APP_HOME%\lib\twilio-8.10.0.jar;%APP_HOME%\lib\google-http-client-jackson2-1.32.1.jar;%APP_HOME%\lib\jackson-annotations-2.12.1.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.12.1.jar;%APP_HOME%\lib\jackson-databind-2.12.1.jar;%APP_HOME%\lib\jackson-core-2.12.1.jar;%APP_HOME%\lib\slf4j-log4j12-1.7.29.jar;%APP_HOME%\lib\spark-core-2.7.1.jar;%APP_HOME%\lib\dotenv-java-2.2.0.jar;%APP_HOME%\lib\json-simple-1.1.1.jar;%APP_HOME%\lib\mysql-connector-java-8.0.28.jar;%APP_HOME%\lib\trove4j-3.0.3.jar;%APP_HOME%\lib\google-oauth-client-java6-1.30.6.jar;%APP_HOME%\lib\google-oauth-client-1.30.6.jar;%APP_HOME%\lib\google-http-client-1.34.2.jar;%APP_HOME%\lib\opencensus-contrib-http-util-0.24.0.jar;%APP_HOME%\lib\guava-28.2-android.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\annotations-16.0.1.jar;%APP_HOME%\lib\avro-1.4.0-cassandra-1.jar;%APP_HOME%\lib\cassandra-thrift-0.8.1.jar;%APP_HOME%\lib\libthrift-0.6.1.jar;%APP_HOME%\lib\slf4j-api-1.7.30.jar;%APP_HOME%\lib\nv-websocket-client-2.14.jar;%APP_HOME%\lib\okhttp-3.13.0.jar;%APP_HOME%\lib\opus-java-1.1.0.pom;%APP_HOME%\lib\commons-collections4-4.1.jar;%APP_HOME%\lib\commons-cli-1.1.jar;%APP_HOME%\lib\httpclient-4.5.13.jar;%APP_HOME%\lib\commons-codec-1.15.jar;%APP_HOME%\lib\commons-collections-3.2.1.jar;%APP_HOME%\lib\commons-lang-2.5.jar;%APP_HOME%\lib\concurrentlinkedhashmap-lru-1.1.jar;%APP_HOME%\lib\antlr-3.2.jar;%APP_HOME%\lib\jackson-mapper-asl-1.4.2.jar;%APP_HOME%\lib\jackson-core-asl-1.4.2.jar;%APP_HOME%\lib\jline-0.9.94.jar;%APP_HOME%\lib\high-scale-lib-1.1.2.jar;%APP_HOME%\lib\snakeyaml-1.6.jar;%APP_HOME%\lib\log4j-1.2.17.jar;%APP_HOME%\lib\jamm-0.2.2.jar;%APP_HOME%\lib\commons-io-2.7.jar;%APP_HOME%\lib\jjwt-jackson-0.11.2.jar;%APP_HOME%\lib\jjwt-impl-0.11.2.jar;%APP_HOME%\lib\jjwt-api-0.11.2.jar;%APP_HOME%\lib\httpcore-4.4.13.jar;%APP_HOME%\lib\jaxb-api-2.3.1.jar;%APP_HOME%\lib\jetty-webapp-9.4.6.v20170531.jar;%APP_HOME%\lib\websocket-server-9.4.6.v20170531.jar;%APP_HOME%\lib\jetty-servlet-9.4.6.v20170531.jar;%APP_HOME%\lib\jetty-security-9.4.6.v20170531.jar;%APP_HOME%\lib\jetty-server-9.4.6.v20170531.jar;%APP_HOME%\lib\websocket-servlet-9.4.6.v20170531.jar;%APP_HOME%\lib\junit-4.13.1.jar;%APP_HOME%\lib\protobuf-java-3.11.4.jar;%APP_HOME%\lib\okio-1.17.2.jar;%APP_HOME%\lib\opus-java-api-1.1.0.jar;%APP_HOME%\lib\opus-java-natives-1.1.0.jar;%APP_HOME%\lib\antlr-runtime-3.2.jar;%APP_HOME%\lib\jetty-6.1.22.jar;%APP_HOME%\lib\servlet-api-2.5.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\javax.activation-api-1.2.0.jar;%APP_HOME%\lib\javax.servlet-api-3.1.0.jar;%APP_HOME%\lib\websocket-client-9.4.6.v20170531.jar;%APP_HOME%\lib\jetty-client-9.4.6.v20170531.jar;%APP_HOME%\lib\jetty-http-9.4.6.v20170531.jar;%APP_HOME%\lib\websocket-common-9.4.6.v20170531.jar;%APP_HOME%\lib\jetty-io-9.4.6.v20170531.jar;%APP_HOME%\lib\jetty-xml-9.4.6.v20170531.jar;%APP_HOME%\lib\websocket-api-9.4.6.v20170531.jar;%APP_HOME%\lib\hamcrest-core-1.3.jar;%APP_HOME%\lib\jna-4.4.0.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar;%APP_HOME%\lib\opencensus-api-0.24.0.jar;%APP_HOME%\lib\stringtemplate-3.2.jar;%APP_HOME%\lib\jetty-util-6.1.22.jar;%APP_HOME%\lib\servlet-api-2.5-20081211.jar;%APP_HOME%\lib\jetty-util-9.4.6.v20170531.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\checker-compat-qual-2.5.5.jar;%APP_HOME%\lib\error_prone_annotations-2.3.4.jar;%APP_HOME%\lib\antlr-2.7.7.jar;%APP_HOME%\lib\grpc-context-1.22.1.jar


@rem Execute discord
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %DISCORD_OPTS%  -classpath "%CLASSPATH%" bot.Bot %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable DISCORD_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%DISCORD_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
