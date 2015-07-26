#!/bin/bash

ARGS=$@
FOTILO_HOME="$(dirname "$(readlink -f "$0")")"
FOTILO_CP="$FOTILO_HOME/lib/*:$FOTILO_HOME/conf"

java -client -Xms64m -Xmx64m $FOTILO_JAVA_OPTS -cp $FOTILO_CP net.xylophones.fotilo.cli.CommandLineApp $ARGS