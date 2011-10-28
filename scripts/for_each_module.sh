#!/bin/bash

MODULES="archetype-simpleapp build cdk components core dev-examples showcase"

EXECUTE="$@"

SCRIPTS=`dirname $0`
TOPDIR=`readlink -f $SCRIPTS/../..`

for MODULE in $MODULES; do
	pushd "$TOPDIR/$MODULE" >/dev/null
		echo "Running '$EXECUTE' on module '$MODULE'"
		eval "$EXECUTE"
	popd >/dev/null
done
