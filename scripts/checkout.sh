#!/bin/bash

MODULES="archetypes build cdk components core dev-examples showcase"
SCRIPTS=`dirname $0`
TOPDIR=`readlink -f $SCRIPTS/../..`

check_all_modules_does_not_exists() {
	for MODULE in $MODULES; do
		if [[ "$MODULE" != "build" ]] && [[ -d "$TOPDIR/$MODULE" ]]; then
			echo "Module \"$MODULE\" already checked out on \"$TOPDIR/$MODULE\""
			exit 1
		fi
	done
}

checkout_all_modules() {
	pushd $TOPDIR >/dev/null
		for MODULE in $MODULES; do
			if [[ ! -d "$MODULE" ]]; then
				git clone "$BASE/$MODULE.git"
			fi
			if [[ "$USERNAME" != "richfaces" ]]; then
				pushd $MODULE >/dev/null
					git remote add upstream "https://github.com/richfaces/$MODULE.git"
				popd >/dev/null
			fi
		done
	popd >/dev/null
}

usage() {
	cat << EOF
usage: $0 options

Clones the modules ($MODULES) from github using either your forked modules or the richfaces modules. If cloning forked modules it will automatically set the upstream remote.

OPTIONS:
   -h      Show this message
   -t      Transport one of http, git or ssh (default)
   -u      Username to checkout with required for http transport or to ensure checkout from your forked modules
EOF
}

USERNAME=richfaces
TYPE=ssh

while getopts "hu:t:" OPTION
do
     case $OPTION in
	h)
		usage
		exit
		;;
	u)
		USERNAME=$OPTARG
		;;
        t)
        	TYPE=$OPTARG
        	;;
	?)
		usage
		exit 1
		;;	
     esac
done

case "$TYPE" in
	http)
		BASE=https://$USERNAME@github.com/$USERNAME
		;;
	git)
		BASE=git://github.com/$USERNAME
		;;
	ssh)
		BASE=git@github.com:$USERNAME
		;;
	[?])
		echo "supported types: http, git, ssh"
		exit 1
		;;
esac

check_all_modules_does_not_exists
checkout_all_modules
