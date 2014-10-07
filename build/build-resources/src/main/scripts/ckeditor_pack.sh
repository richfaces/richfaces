#!/bin/sh

# to build CKEditor, you need to:
#
# 1. download distribution package including _source folder
# 2. checkout appropriate tag from SVN
# 3. copy _dev from checkout SVN folder to the contents of distribution package
# 4. copy _source from richfaces/components/input/ui/src/main/resources/META-INF/resources/org.richfaces.ckeditor to ckeditor/_source
# 5. change ckeditor.pack: from 'kama' as default skin to 'richfaces': _source/skins/richfaces/skin.js
# 6. run _dev/releaser/releaser.sh
# 7. create directory _dev/releaser/release/release/org.richfaces.ckeditor
# 8. run this script from inside of _dev/releaser/release/release directory
# 9. copy contents of created org.richfas.ckeditor JSF resources: org.richfaces.ckeditor
# 10. rename and fix compiled ckeditor.js and skins/richfaces/editor.css to editor.ecss
#     - fix ckeditor.js and skins/richfaces/skin.js to point to editor.ecss
#     - fix editor.ecss (ECSS compiler complains) - contains invalid #{a4jSkin....;} and #{url...;} instructions - remove semi-colon
#              $ sed -ri 's/#\{a4jSkin([^;]+);/#{a4jSkin\1/g' editor.ecss
#              $ sed -ri 's/#\{resource([^;]+);/#{resource\1/g' editor.ecss
#     - fix editor.ecss - some wrong line-endings (the line ends after ' sign)
# 
# What does this script do: takes all the resources required by CKEditor to work properly
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
