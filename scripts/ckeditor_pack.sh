#!/bin/sh

# This script takes all the resources requires by CKEditor to work properly
# and copy them to ./org.richfaces.ckeditor/ folder.
#
# Replace components/input/ui/src/main/resources/META-INF/resources/org.richfaces.ckeditor
# with newly packed ./org.richfaces.ckeditor/.

PACK="images lang plugins skins themes ckeditor.js config.js contents.css"
OUT="org.richfaces.ckeditor"

rm -rf $OUT
mkdir $OUT

for OBJECT in $PACK; do
	cp -r $OBJECT $OUT/
done
