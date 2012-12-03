#!/bin/sh

# to build CKEditor, you need to
# 1. download distribution package including _source folder
# 2. checkout appropriate tag from SVN
# 3. copy _dev from checkout SVN folder to the contents of distribution package
# 4. change ckeditor.pack: from 'kama' as default skin to 'richfaces': _source/skins/richfaces/skin.js
# 5. run _dev/releaser/releaser.sh
# 6. copy contents of _dev/releaser/release/release to JSF resources: org.richfaces.ckeditor
# 7. rename and fix generated skins/richfaces/editor.css to editor.ecss
#     - fix ckeditor.js and skins/richfaces/skin.jsto point to editor.ecss
#     - fix editor.ecss (ECSS compiler complains) - contains invalid #{richSkin....;} and #{url...;} instructions - remove semi-colon
#     - fix editor.ecss - some wrong line-endings (the line ends after ' sign)
# 
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
