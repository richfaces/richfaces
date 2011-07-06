#!/bin/bash

MODULES="archetypes build cdk components core dev-examples showcase"
SCRIPTS=`dirname $0`
TOPDIR=`readlink -f $SCRIPTS/../..`
BRANCH="develop"
CURL=/usr/bin/curl

fork_all_modules() {
	get_resty_if_required
	. $SCRIPTS/resty
	resty https://api.github.com
	for MODULE in $MODULES; do
		if [[ "$USERNAME" != "richfaces" ]]; then
			RESULT=$((POST /repos/richfaces/$MODULE/forks -u "$USERNAME:$PASSWORD" -v) 2>&1)
			if [[ $RESULT =~ "Status: 401 Unauthorized" ]]; then
				echo "Github username / password is incorrect. Exiting."
				exit 1
			elif [[ $RESULT =~ "Status: 201 Created" ]]; then
				echo "Successfully forked $MODULE in Github."
			else
				echo "Unsure of what happened when forking $MODULE in Github. Exiting."
				exit 1
			fi
		fi
	done
}

checkout_all_modules() {
	pushd $TOPDIR >/dev/null
		for MODULE in $MODULES; do
			if [[ ! -d "$MODULE" ]]; then
				git clone $QUIET "$BASE/$MODULE.git"
			else
				echo "Module $MODULE already exists. Skipping over it."
				continue
			fi
			if [[ "$USERNAME" != "richfaces" ]]; then
				pushd $MODULE >/dev/null
					git remote add upstream "https://github.com/richfaces/$MODULE.git"
				popd >/dev/null
			fi
		done
	popd >/dev/null
}

pull_upstream_all_modules() {
        pushd $TOPDIR >/dev/null
                for MODULE in $MODULES; do
                        if [[ ! -d "$MODULE" ]]; then
                                echo "Module $MODULE does not exist. Skipping over it."
                        fi
                        if [[ "$USERNAME" != "richfaces" ]]; then
                                pushd $MODULE >/dev/null
					RESULT=`git stash`
                                        git pull $QUIET --rebase upstream $BRANCH
					if [[ ! $RESULT =~ "No local changes to save" ]]; then
						git stash $QUIET pop
					fi
                                popd >/dev/null
			else
				pushd $MODULE >/dev/null
					RESULT=`git stash`
                                        git pull $QUIET --rebase origin $BRANCH
					if [[ ! $RESULT =~ "No local changes to save" ]]; then
                                                git stash $QUIET pop
                                        fi
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
   -h      Show this message.
   -v      Be verbose.
   -p      Pull updates rather than clone fresh modules.
   -f      Automatically fork the source before cloning it (you will be prompted for your github password).
   -t      Transport one of http, git or ssh (default).
   -u      Github username to checkout with required for http transport or to ensure checkout from your forked modules.
   -m      Specify the modules to clone from github in a space seperated quoted string ie. -m "core cdk components". You may also use "all" as an alias for ($MODULES).
   -b      Specify the branch to pull updates from defaults to "develop".
EOF
}

get_resty_if_required() {
	if [[ ! -f $SCRIPTS/resty ]]; then
		echo "Fetching resty to allow us fork on Github."
		curl -s -L http://github.com/micha/resty/raw/master/resty > $SCRIPTS/resty
	fi
}

QUIET="-q"
USERNAME=richfaces
PASSWORD=""
TYPE=ssh
PULL=false
FORK=false
CURL_FOUND=false

while getopts "hvpfu:b:t:m:" OPTION
do
     case $OPTION in
	h)
		usage
		exit
		;;
	v)
		QUIET=""
		;;
        p)
		PULL=true
		;;
	f)
		FORK=true
                type -P $CURL >/dev/null && CURL_FOUND=true || CURL_FOUND=false
		if [[ CURL_FOUND == false ]]; then
			echo "$CURL not found you cannot use automatic forking."
			exit 1
		fi
		if [[ $USERNAME != "richfaces" ]]; then
			read -p "Enter your Github password:" -s PASSWORD
			echo ""
		fi
		;;
	u)
		USERNAME=$OPTARG
		;;
	b)
		BRANCH=$OPTARG
		;;
        t)
        	TYPE=$OPTARG
        	;;
	m)
		MODULES=`echo "$OPTARG" | sed "s/all/$MODULES/g"`
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

if [[ $PULL == false ]]; then
	if [[ $FORK == true && $USERNAME != "richfaces" ]]; then
		fork_all_modules
	fi
	checkout_all_modules
else
	pull_upstream_all_modules
fi
