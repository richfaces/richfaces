#!/bin/bash

MODULES="archetypes build cdk components core dev-examples showcase"
SCRIPTS=`dirname $0`
TOPDIR=`readlink -f $SCRIPTS/../..`

check_all_modules_does_not_exists() {
	for MODULE in $MODULES; do
		if [ "$MODULE" != "build" -a -d "$TOPDIR/$MODULE" ]; then
			echo "Module \"$MODULE\" already checked out on \"$TOPDIR/$MODULE\""
			exit 1
		fi
	done
}

checkout_all_modules() {
	pushd $TOPDIR >/dev/null
		for MODULE in $MODULES; do
			if [ ! -d "$MODULE" ]; then
				git clone "$BASE/$MODULE.git"
			fi
		done
	popd
}

TYPE=ssh

while getopts "t:" OPTION
do
     case $OPTION in
         t)
             TYPE=$OPTARG
             ;;
     esac
done

case "$TYPE" in
	http)
		BASE=https://lfryc@github.com/richfaces
		;;
	git)
		BASE=git://github.com/richfaces
		;;
	ssh)
		BASE=git@github.com:richfaces
		;;
	[?])
		echo "supported types: http, git, ssh"
		exit 1
		;;
esac

check_all_modules_does_not_exists
checkout_all_modules
