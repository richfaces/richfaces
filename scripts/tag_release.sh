#!/bin/bash

MODULES="archetypes build cdk components core dev-examples showcase"

usage()
{
cat << EOF
usage: $0 options

This script will search all pom.xml and change <version>ORIG</version> to <version>NEW</version>  

BASIC OPTIONS:
   -o      Current version to replace - such as 4.1.0-SNAPSHOT
   -t      Tag name - such as 4.1.0.20110525-M1
   -n      Tag version - such as 4.1.0.Milestone1
   -d      New development version - such as 4.1.1-SNAPSHOT
   -h      Prints this usage help
   
EOF
}

check_all_modules_exists() {
	for MODULE in $MODULES; do
		if [ ! -d "$TOPDIR/$MODULE" ]; then
			echo "Module \"$MODULE\" does not exists on \"$TOPDIR/$MODULE\""
			exit 1
		fi	
	done
}

change_versions() {
	for MODULE in $MODULES; do
		$change_version -d "$TOPDIR/$MODULE" -o "$1" -n "$2"
	done
}

verify_tag_options() {
	if [ -z "$TAG" ]; then
		echo "ERROR: You have to provide tag name (-t <tag_name> option)"
		usage
		exit 1
	fi
	if [ -z "$ORIG_VERSION" ]; then
		echo "ERROR: You have to provide old version (-o <version> option)"
		usage
		exit 1
	fi
	if [ -z "$NEW_VERSION" ]; then
		echo "ERROR: You have to provide tag version (-n <version> option)"
		usage
		exit 1
	fi
	if [ -z "$DEVEL_VERSION" ]; then
		echo "ERROR: You have to provide new development version (-d <version> option)"
		usage
		exit 1
	fi
}

commit_tag() {
	for MODULE in $MODULES; do
		pushd "$TOPDIR/$MODULE" >/dev/null
			git add -A
			git commit -m "Prepare for tagging '${TAG}' for version '${NEW_VERSION}'"
			git tag -a -m "Tag '${TAG}' for version '${NEW_VERSION}'" "${TAG}"
		popd
	done
}

commit_devel() {
	for MODULE in $MODULES; do
		pushd "$TOPDIR/$MODULE" >/dev/null
			git add -A
			git commit -m "Preparing for new development version '${DEVEL_VERSION}'"
		popd
	done
}



ORIG_VERSION=
NEW_VERSION=
TAG=
WORK=1

SCRIPTS=`dirname $0`
TOPDIR=`readlink -f $SCRIPTS/../..`

change_version=$SCRIPTS/change_version.sh

if [ "$#" -eq 0 ]; then
	usage
	exit 1
fi

while getopts "t:n:o:d:h" OPTION
do
     case $OPTION in
         t)
	     TAG=$OPTARG
             ;;
         n)
             NEW_VERSION=$OPTARG
	     ;;
         o)
             ORIG_VERSION=$OPTARG
             ;;
	 d)
	     DEVEL_VERSION=$OPTARG
	     ;;
         h)
             usage
             WORK=0
             ;;
         [?])
             usage;
             WORK=0
             ;;
     esac
done

if [ "$WORK" -eq 0 ]; then
	exit 1
fi

if [ -n "$TAG" ]; then
	verify_tag_options
	check_all_modules_exists
	change_versions "$ORIG_VERSION" "$NEW_VERSION"
	commit_tag
	change_versions "$NEW_VERSION" "$DEVEL_VERSION"
	commit_devel
fi	
