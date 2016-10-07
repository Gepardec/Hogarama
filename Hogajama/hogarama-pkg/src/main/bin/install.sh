#!/bin/bash

# Install-Skript fuer Installationspaket

MY_PATH=$(readlink -f $0)
BIN_DIR=`dirname $MY_PATH`
BASE_DIR=$BIN_DIR/..
CONFIG_DIR=$BASE_DIR/config
PRG=`basename $0`

SAVE_DATE=`date +save_%Y_%m_%d-%H_%M_%S`
RM_ACTION=manual

#####################################################################
##                              print_usage
#####################################################################
print_usage(){
cat <<EOF 1>&2

usage: $PRG [-hrse] instanzname

Optionen:
    s: Vorhandener Server wird beendet und die Installation wird gesichert. (*_$SAVE_DATE)
    r: Vorhandener Server wird beendet und die Installation wird gelöscht.
    e: Verwende Environment fuer Einstellungen und nicht instanzname. Experimentell!
    h: Diese Hilfeseite

Funktion:
	Installiert das Paket in die durch instanzname gegebene Umgebung.
	Die Umgebung wird durch die in $HOME/.{instanzname}rc gesetzten Variablen
	definiert.

EOF
}

#####################################################################
##                              link_to_jboss7
#####################################################################
link_to_jboss7() {

	ln -s jboss7 $1
}

######################   Optionen bestimmen ###################

while getopts "rshe" option
do
    case $option in
      s)
        RM_ACTION="save";;
      r)
        RM_ACTION="remove";;
      e)
        USE_ENV="true";;
      *)
        print_usage
        exit 1
        ;;
    esac
done

shift `expr $OPTIND - 1`

INSTANZ=$1

##################### Beginn #########################

if [ x$USE_ENV = xtrue ]; then
	JBOSS_SKRIPT=$BIN_DIR/jboss7

elif [ x$INSTANZ = x ]; then
	print_usage
	echo "Instanzname ist nicht gesetzt!" 1>&2
	exit 1
else
	JBOSS_SKRIPT=$BIN_DIR/$INSTANZ
	link_to_jboss7 $JBOSS_SKRIPT
	JBOSS_INSTANZ_NAME=$INSTANZ
fi

echo "JBOSS_SKRIPT is at $JBOSS_SKRIPT" 

# set -x
# Einlesen der Hilfsfunktionen
. $JBOSS_SKRIPT status

SAVE_DIR=${JBOSS_HOME}_$SAVE_DATE
if is_running; then
	echo
    echo -n "Ein Server läuft in dieser Installation. "
    case $RM_ACTION in
      save)
    	echo "Server wird heruntergefahren."
        $JBOSS_SKRIPT stop
        ;;
      remove)
    	echo "Server wird heruntergefahren."
        $JBOSS_SKRIPT stop
		;;
      *)
    	echo "Stoppen Sie den Server und sichern Sie das JBoss-Verzeichnis!"
    	echo "$JBOSS_SKRIPT stop"
    	echo "mv $JBOSS_HOME $SAVE_DIR"
		exit 2
        ;;
    esac
fi

if [ -d $JBOSS_HOME ]; then
	echo
    echo -n "Das JBOSS_HOME Verzeichnis ($JBOSS_HOME) existiert. "
    case $RM_ACTION in
      save)
    	echo "Es wird gesichert auf: $SAVE_DIR"
        mv $JBOSS_HOME $SAVE_DIR
        ;;
      remove)
    	echo "Es wird geloescht."
        if [ -d $JBOSS_HOME/standalone ]; then
			rm -rf $JBOSS_HOME
		else
			echo "Das scheint kein JBoss-Verzeichnis zu sein: $JBOSS_HOME"
			echo "Bitte pruefen und manuell loeschen!"
			exit 3
		fi
		;;
      *)
    	echo "Loeschen oder sichern Sie das Verzeichnis."
		echo "rm -rf $JBOSS_HOME"
        echo "mv $JBOSS_HOME $SAVE_DIR"
		exit 4
        ;;
    esac
fi

$JBOSS_SKRIPT configure $CONFIG_DIR
