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

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  app-ktor startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and APP_KTOR_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-Dio.ktor.development=false"

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

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

set CLASSPATH=%APP_HOME%\lib\app-ktor-0.0.1.jar;%APP_HOME%\lib\ktor-server-auth-jwt-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-auth-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-sessions-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-auto-head-response-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-default-headers-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-swagger-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-call-logging-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-content-negotiation-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-openapi-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-config-yaml-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-html-builder-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-cio-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-host-common-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-server-core-jvm-2.3.1.jar;%APP_HOME%\lib\repo-postgresql-0.0.1.jar;%APP_HOME%\lib\common-0.0.1.jar;%APP_HOME%\lib\ktor-serialization-kotlinx-json-jvm-2.3.1.jar;%APP_HOME%\lib\exposed-dao-0.41.1.jar;%APP_HOME%\lib\exposed-jdbc-0.41.1.jar;%APP_HOME%\lib\exposed-java-time-0.41.1.jar;%APP_HOME%\lib\kotest-runner-junit5-jvm-5.5.5.jar;%APP_HOME%\lib\log-0.0.1.jar;%APP_HOME%\lib\exposed-core-0.41.1.jar;%APP_HOME%\lib\ktor-serialization-jackson-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-serialization-kotlinx-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-client-core-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-websocket-serialization-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-serialization-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-events-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-http-cio-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-websockets-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-http-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-network-jvm-2.3.1.jar;%APP_HOME%\lib\ktor-utils-jvm-2.3.1.jar;%APP_HOME%\lib\kotest-extensions-jvm-5.5.5.jar;%APP_HOME%\lib\kotest-framework-engine-jvm-5.5.5.jar;%APP_HOME%\lib\kotest-framework-concurrency-jvm-5.5.5.jar;%APP_HOME%\lib\kotest-framework-api-jvm-5.5.5.jar;%APP_HOME%\lib\kotest-framework-discovery-jvm-5.5.5.jar;%APP_HOME%\lib\kotest-assertions-core-jvm-5.5.5.jar;%APP_HOME%\lib\kotest-assertions-shared-jvm-5.5.5.jar;%APP_HOME%\lib\kotest-common-jvm-5.5.5.jar;%APP_HOME%\lib\ktor-io-jvm-2.3.1.jar;%APP_HOME%\lib\kotlinx-coroutines-jdk8-1.6.4.jar;%APP_HOME%\lib\kotlinx-coroutines-debug-1.6.4.jar;%APP_HOME%\lib\kotlinx-coroutines-test-jvm-1.6.4.jar;%APP_HOME%\lib\mockk-jvm-1.13.1.jar;%APP_HOME%\lib\mockk-dsl-jvm-1.13.1.jar;%APP_HOME%\lib\kotlinx-coroutines-core-jvm-1.6.4.jar;%APP_HOME%\lib\kotlinx-coroutines-slf4j-1.6.4.jar;%APP_HOME%\lib\uuid-jvm-0.7.0.jar;%APP_HOME%\lib\kotlinx-html-jvm-0.8.1.jar;%APP_HOME%\lib\kotest-assertions-api-jvm-5.5.5.jar;%APP_HOME%\lib\mockk-agent-jvm-1.13.1.jar;%APP_HOME%\lib\mockk-agent-api-jvm-1.13.1.jar;%APP_HOME%\lib\mockk-core-jvm-1.13.1.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.8.22.jar;%APP_HOME%\lib\telegrambots-6.3.0.jar;%APP_HOME%\lib\httpmime-4.5.13.jar;%APP_HOME%\lib\swagger-codegen-generators-1.0.38.jar;%APP_HOME%\lib\swagger-codegen-3.0.41.jar;%APP_HOME%\lib\swagger-codegen-2.4.30.jar;%APP_HOME%\lib\swagger-parser-2.1.13.jar;%APP_HOME%\lib\swagger-parser-v2-converter-2.1.13.jar;%APP_HOME%\lib\swagger-compat-spec-parser-1.0.65.jar;%APP_HOME%\lib\httpclient-4.5.14.jar;%APP_HOME%\lib\commons-codec-1.15.jar;%APP_HOME%\lib\postgresql-42.5.1.jar;%APP_HOME%\lib\HikariCP-5.0.1.jar;%APP_HOME%\lib\logback-classic-1.4.7.jar;%APP_HOME%\lib\emoji-java-5.1.1.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.8.22.jar;%APP_HOME%\lib\java-jwt-4.4.0.jar;%APP_HOME%\lib\jwks-rsa-0.22.0.jar;%APP_HOME%\lib\telegrambots-meta-6.3.0.jar;%APP_HOME%\lib\jersey-media-json-jackson-2.35.jar;%APP_HOME%\lib\logstash-logback-encoder-7.3.jar;%APP_HOME%\lib\swagger-parser-v3-2.1.13.jar;%APP_HOME%\lib\swagger-parser-1.0.65.jar;%APP_HOME%\lib\swagger-core-1.6.10.jar;%APP_HOME%\lib\swagger-core-2.2.9.jar;%APP_HOME%\lib\jackson-dataformat-yaml-2.15.1.jar;%APP_HOME%\lib\jackson-jaxrs-json-provider-2.15.1.jar;%APP_HOME%\lib\jackson-module-jaxb-annotations-2.15.1.jar;%APP_HOME%\lib\json-patch-1.13.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.15.1.jar;%APP_HOME%\lib\jackson-jaxrs-base-2.15.1.jar;%APP_HOME%\lib\json-schema-validator-2.2.14.jar;%APP_HOME%\lib\json-schema-core-1.2.14.jar;%APP_HOME%\lib\jackson-coreutils-equivalence-1.0.jar;%APP_HOME%\lib\jackson-coreutils-2.0.jar;%APP_HOME%\lib\jackson-databind-2.15.1.jar;%APP_HOME%\lib\swagger-parser-core-2.1.13.jar;%APP_HOME%\lib\swagger-models-2.2.9.jar;%APP_HOME%\lib\swagger-models-1.6.10.jar;%APP_HOME%\lib\jackson-annotations-2.15.1.jar;%APP_HOME%\lib\jackson-core-2.15.1.jar;%APP_HOME%\lib\jackson-module-kotlin-2.15.1.jar;%APP_HOME%\lib\kotlin-reflect-1.8.10.jar;%APP_HOME%\lib\kotlinx-datetime-jvm-0.4.0.jar;%APP_HOME%\lib\yamlkt-jvm-0.12.0.jar;%APP_HOME%\lib\kotlinx-serialization-core-jvm-1.5.1.jar;%APP_HOME%\lib\kotlinx-serialization-json-jvm-1.5.1.jar;%APP_HOME%\lib\mordant-1.2.1.jar;%APP_HOME%\lib\colormath-1.2.0.jar;%APP_HOME%\lib\kotlin-stdlib-1.8.22.jar;%APP_HOME%\lib\slf4j-ext-1.7.36.jar;%APP_HOME%\lib\handlebars-4.3.1.jar;%APP_HOME%\lib\slf4j-api-2.0.4.jar;%APP_HOME%\lib\config-1.4.2.jar;%APP_HOME%\lib\kotlin-stdlib-common-1.8.22.jar;%APP_HOME%\lib\jansi-2.4.0.jar;%APP_HOME%\lib\uri-template-0.10.jar;%APP_HOME%\lib\guava-31.1-jre.jar;%APP_HOME%\lib\checker-qual-3.12.0.jar;%APP_HOME%\lib\logback-core-1.4.7.jar;%APP_HOME%\lib\json-20220924.jar;%APP_HOME%\lib\jersey-hk2-2.35.jar;%APP_HOME%\lib\jersey-container-grizzly2-http-2.35.jar;%APP_HOME%\lib\jersey-server-2.35.jar;%APP_HOME%\lib\commons-io-2.11.0.jar;%APP_HOME%\lib\junit-jupiter-5.8.2.jar;%APP_HOME%\lib\junit-jupiter-params-5.8.2.jar;%APP_HOME%\lib\junit-jupiter-engine-5.8.2.jar;%APP_HOME%\lib\junit-jupiter-api-5.8.2.jar;%APP_HOME%\lib\junit-platform-suite-api-1.8.2.jar;%APP_HOME%\lib\junit-platform-commons-1.8.2.jar;%APP_HOME%\lib\junit-platform-launcher-1.8.2.jar;%APP_HOME%\lib\junit-platform-engine-1.8.2.jar;%APP_HOME%\lib\annotations-13.0.jar;%APP_HOME%\lib\jakarta.xml.bind-api-2.3.3.jar;%APP_HOME%\lib\jakarta.activation-api-1.2.2.jar;%APP_HOME%\lib\jersey-client-2.35.jar;%APP_HOME%\lib\jersey-common-2.35.jar;%APP_HOME%\lib\hk2-locator-2.6.1.jar;%APP_HOME%\lib\javassist-3.25.0-GA.jar;%APP_HOME%\lib\jersey-entity-filtering-2.35.jar;%APP_HOME%\lib\hk2-api-2.6.1.jar;%APP_HOME%\lib\hk2-utils-2.6.1.jar;%APP_HOME%\lib\jakarta.inject-2.6.1.jar;%APP_HOME%\lib\grizzly-http-server-2.4.4.jar;%APP_HOME%\lib\jakarta.ws.rs-api-2.1.6.jar;%APP_HOME%\lib\jakarta.annotation-api-1.3.5.jar;%APP_HOME%\lib\jakarta.validation-api-2.0.2.jar;%APP_HOME%\lib\httpcore-4.4.16.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\opentest4j-1.2.0.jar;%APP_HOME%\lib\jmustache-1.15.jar;%APP_HOME%\lib\commons-lang3-3.12.0.jar;%APP_HOME%\lib\commons-cli-1.5.0.jar;%APP_HOME%\lib\snakeyaml-2.0.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\msg-simple-1.2.jar;%APP_HOME%\lib\btf-1.3.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\error_prone_annotations-2.11.0.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar;%APP_HOME%\lib\osgi-resource-locator-1.0.3.jar;%APP_HOME%\lib\aopalliance-repackaged-2.6.1.jar;%APP_HOME%\lib\grizzly-http-2.4.4.jar;%APP_HOME%\lib\classgraph-4.8.154.jar;%APP_HOME%\lib\commonmark-0.17.0.jar;%APP_HOME%\lib\grizzly-framework-2.4.4.jar;%APP_HOME%\lib\jna-platform-5.9.0.jar;%APP_HOME%\lib\jna-5.9.0.jar;%APP_HOME%\lib\byte-buddy-1.12.10.jar;%APP_HOME%\lib\byte-buddy-agent-1.12.10.jar;%APP_HOME%\lib\validation-api-1.1.0.Final.jar;%APP_HOME%\lib\swagger-annotations-2.2.9.jar;%APP_HOME%\lib\java-diff-utils-4.12.jar;%APP_HOME%\lib\junit-4.13.2.jar;%APP_HOME%\lib\swagger-annotations-1.6.10.jar;%APP_HOME%\lib\mailapi-1.6.2.jar;%APP_HOME%\lib\joda-time-2.10.5.jar;%APP_HOME%\lib\libphonenumber-8.11.1.jar;%APP_HOME%\lib\jopt-simple-5.0.4.jar;%APP_HOME%\lib\hamcrest-core-1.3.jar;%APP_HOME%\lib\rhino-1.7.7.2.jar;%APP_HOME%\lib\objenesis-3.2.jar


@rem Execute app-ktor
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %APP_KTOR_OPTS%  -classpath "%CLASSPATH%" io.ktor.server.cio.EngineMain %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable APP_KTOR_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%APP_KTOR_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
