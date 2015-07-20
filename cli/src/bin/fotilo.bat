@echo off

 setLocal EnableDelayedExpansion
 
 set ARGS=%*
 set FOTILO_HOME=%~dp0
 set FOTILO_CP=
 for /R %FOTILO_HOME%\lib %%a in (*.jar) do (
   set FOTILO_CP=!FOTILO_CP!;%%a
 )
 set FOTILO_CP=!FOTILO_CP!

java %FOTILO_JAVA_OPTS% -cp "%FOTILO_CP%;%FOTILO_HOME%\conf" net.xylophones.fotilo.cli.CommandLineApp %ARGS%
