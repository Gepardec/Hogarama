#!/bin/sh

set -x
error=0

if [ -n "$JBOSS_DONT_UNPACK" ]; then
    echo "Don't unpack JBoss" 1>&2
    exit 0
fi

if [ -n "$CODE_READY_WORKSPACE" ]; then
    echo "Don't unpack JBoss on Code Ready Workspace" 1>&2
    rm -rf $JBOSS_HOME; mkdir -p $JBOSS_HOME && cp -r $CODE_READY_JBOSS/* $JBOSS_HOME
    exit $?
fi

if [ -z "$JBossPackage" ]; then
	JBossPackage=$1
fi

if [ ! -f "$JBossPackage" ]; then
    echo "JBoss Package $JBossPackage doesn't exist! Set JBossPackage or use argument" 1>&2
    error=1
fi

if [ -z "$JBOSS_HOME" ]; then
	echo "Error: JBOSS_HOME not set!" 1>&2
	error=1
fi

if [ -e "$JBOSS_HOME" ]; then
    if [ X$JBOSS_FORCE_INSTALL = XTrue ]; then
	    rm -rf $JBOSS_HOME/*
    else
        echo "$JBOSS_HOME exists, will not override it. Remove it manually!" 1>&2
        error=1
    fi
fi

if [ -n "$JBossPatch" -a ! -f "$JBossPatch" ]; then
   	echo "Patch $JBossPatch doesn't exist!" 1>&2
    error=1
fi


test $error = 0 || exit $error

mkdir -p $JBOSS_HOME

TmpInstall=${JBOSS_HOME}/_Tmp$$

unzip -qd $TmpInstall $JBossPackage
mv $TmpInstall/*/* $JBOSS_HOME
rm -r $TmpInstall

if [ -n "$JBossPatch" ]; then
    echo "Install patch $JBossPatch" 1>&2
	$JBOSS_HOME/bin/jboss-cli.sh "patch apply $JBossPatch"
    error=$?
fi

exit $error
