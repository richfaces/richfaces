#!/bin/bash

MODULES="archetypes build cdk components core dev-examples showcase"

EXECUTE="$@"

SCRIPTS=`dirname $0`
TOPDIR=`readlink -f $SCRIPTS/../..`

#Colors
Brown="$(tput setaf 3)"
Green="$(tput setaf 2)"
NoColor="$(tput sgr0)"
Ruler="$Green################################################$NoColor"

echo $Ruler
for MODULE in $MODULES; do
	pushd "$TOPDIR/$MODULE" >/dev/null
		echo -n $Brown
		echo "[ $MODULE ] > $EXECUTE"
	        echo -n $NoColor
		eval "$EXECUTE"
	popd >/dev/null
done
echo $Ruler
