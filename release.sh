#!/bin/sh

usage()
{
cat << EOF
usage: $0 options

** WARNING: DO NOT USE UNLESS FOLLOWING PROJECT PROCESS **
 
This script will release all modules in RichFaces 4.0 distributions.  WARNING: DO NOT USE UNLESS FOLLOWING PROJECT PROCESS

BASIC OPTIONS:
   -h      Show this message
   -b      Root directory, otherwise the PWD is used 
   -x      Use mvn -X option
   
Rules:
- Modules should only import other modules bom's for versions, not set specific version themselves
- All interproject versions must be set with variables in the for of: <version_prop>.version
   - Example: "org.richfaces.commons.version"
   - <version_prop> is set in the array below
- All sub-modules of a single t/b/t module must share versions
   - Setting <autoVersionSubmodules> to true
- <parent> references within the same module must use <relativePath>
   - This is needed because during release prepare the module parent is not installed
   - For example: commons-api must reference commons-parent as ../parent/pom.xml
   - Note: this is not needed for referencing parents outside of module
      - Such as richfaces-parent - it is released already

EOF
}

work()
{

if [ "$DEBUG" -eq "1" ]
then
   MVNARGS="-X"
fi
  
if  [ -d "$BASE" ]
then
   echo "$ECHO_MARK Beginning build from base directory: $BASE"
else
   echo "$ECHO_MARK Base directory does not exist, can not build from: $BASE"
   exit 1;
fi

for module_meta in "${MAIN_MODULE_ARRAY[@]}"
do
   set -- $module_meta
   #for each module I need
   MODULE_PATH=$1
   REL_VER=$2
   DEV_VER=$3
   TAG_BASE=$4
   VER_PROP_BASE=$5
   
   #derived variables
   TAG_NAME="$TAG_BASE-$REL_VER"
   MODULE_BASE=$BASE/$1
   MODULE_TRUNK=$MODULE_BASE/$TRUNK_EXT
   MODULE_TAG=$MODULE_BASE/tags/$TAG_NAME
   MVN_PREPARE_CMD="mvn $MVNARGS release:prepare --batch-mode -DdevelopmentVersion=$DEV_VER -DreleaseVersion=$REL_VER -Dtag=$TAG_NAME"
   MVN_PERFORM_CMD="mvn $MVNARGS --batch-mode release:perform"
   VER_PROP="$VER_PROP_BASE.version"
   
   echo
   echo "================================= START"
   echo "$ECHO_MARK Releasing $VER_PROP_BASE "
   echo "$ECHO_MARK Module Path : $MODULE_BASE"
   echo "$ECHO_MARK Module Trunk Path : $MODULE_TRUNK"
   echo "$ECHO_MARK Module Tag Path : $MODULE_TAG"
   echo "$ECHO_MARK Development Version : $DEV_VER"
   echo "$ECHO_MARK Release Version : $REL_VER"
   echo "$ECHO_MARK Tag Name : $TAG_NAME"
   echo "$ECHO_MARK Version Prop : $VER_PROP"
   echo ----------------------------------
   echo "$ECHO_MARK Moving into $MODULE_TRUNK"
   
   if [ -d "$MODULE_TRUNK" ]
   then
       cd $MODULE_TRUNK
       
       echo "$ECHO_MARK Excuting Maven Release Prepare Command: $MVN_PREPARE_CMD"
       $MVN_PREPARE_CMD
       if [ $? -eq "0" ]
       then
        echo "$ECHO_MARK Maven Release Prepare Command Completed"
       else
        echo "$ECHO_MARK Maven Release Prepare Command Failed"
        exit 1;
       fi
       
       # NOTE: This might not be needed, because all modules in a project share a version
       # echo "$ECHO_MARK Fixing maven-release-plugin error with updating import versions in tag: $TAG_NAME"
       # TODO - Need to verify next dev version gets updated correctly by plugin
       # find $MODULE_TAG -name "pom.xml" | xargs perl -pi -e "s/<$VER_PROP>.+<\/$VER_PROP>/<$VER_PROP>$REL_VER<\/$VER_PROP>/"
       # svn ci $MODULE_TAG -m "[RichFaces Release Script] Checking in release-plugin fix for import version updates"
       # echo "$ECHO_MARK Fixing maven-release-plugin error complete"
       
       echo "$ECHO_MARK Executing Maven Release Perform Command: $MVN_PERFORM_CMD"
       $MVN_PERFORM_CMD
       if [ $? -eq "0" ]
       then
        echo "$ECHO_MARK Maven Release Perform Command Completed"
       else
        echo "$ECHO_MARK Maven Release Perform Command Failed"
        exit 1;
       fi
       
       echo "$ECHO_MARK Updating Intermodule Module Version References"
       echo "$ECHO_MARK $ECHO_MARK Replacing $VER_PROP properties to: $REL_VER in all modules"
       
       # Only replace in pom.xml that are in a trunk, not in a tag, or branch
       #   - This is because only modules that have not released yet should be referring to this
       find $BASE -name "pom.xml" -path *trunk/* | xargs perl -pi -e "s/<$VER_PROP>.+<\/$VER_PROP>/<$VER_PROP>$REL_VER<\/$VER_PROP>/"
       if [ $? -eq "0" ]
       then
        echo "$ECHO_MARK Version References Updated"
       else
        echo "$ECHO_MARK Version Reference Update Failed"
        exit 1;
       fi
       
       svn ci $BASE . -m "[RichFaces Release Script] Updated intermodule version of $VER_PROP properties to: $REL_VER"
       if [ $? -eq "0" ]
       then
        echo "$ECHO_MARK Commit of Version References Update Completed"
       else
        echo "$ECHO_MARK Commit of Version References Update Failed"
        exit 1;
       fi
       
       # TODO need to handle changing these to next $DEV_VER after release is complete
       #   - Will have to populate an array with data for this.
       
       echo "================================= END"
   else
      # TODO someday create an automatic rollback
      echo "$ECHO_MARK Module directory does not exist, can not execute release - you must now rollback previous changes"
      echo =================================
      exit 1;
   fi

done

}

BASE=`pwd`
TRUNK_EXT="trunk"
ECHO_MARK="==> "
DEBUG=0
MVNARGS=
WORK=1

# Listing of all modules to be released
# TODO might be read from prop file
#    http://tldp.org/LDP/abs/html/arrays.html#SCRIPTARRAY
# $1 = path to module trunk/head
# $2 = release version
# $3 = dev version
# $4 = tag name base - "-<release version" appended
# $5 = version property - required for updating intermodule dependencies modules
MAIN_MODULE_ARRAY=(
       #"build/bom 4.0.0.Alpha2 4.0.0-SNAPSHOT richfaces-bom org.richfaces.bom" 
       #"commons 4.0.0.Alpha2 4.0.0-SNAPSHOT richfaces-commons org.richfaces.commons"
       #"core 4.0.0.Alpha2 4.0.0-SNAPSHOT richfaces-core org.richfaces.core"
       #"cdk 4.0.0.Alpha2 4.0.0-SNAPSHOT richfaces-cdk org.richfaces.cdk"
       #"ui/core 4.0.0.Alpha2 4.0.0-SNAPSHOT richfaces-ui-core org.richfaces.ui.core"
       #"ui/misc 4.0.0.Alpha2 4.0.0-SNAPSHOT richfaces-ui-misc org.richfaces.ui.misc"
       #"ui/iteration 4.0.0.Alpha2 4.0.0-SNAPSHOT richfaces-ui-iteration org.richfaces.ui.iteration"
       #"ui/dist 4.0.0.Alpha2 4.0.0-SNAPSHOT richfaces-ui-dist org.richfaces.ui"
       #"archetypes/richfaces-archetype-simpleapp 4.0.0.Alpha2 4.0.0-SNAPSHOT richfaces-archetype-simpleapp org.richfaces.archetypes.richfaces-archetype-simpleapp"
       #"examples/core-demo 4.0.0.Alpha2 4.0.0-SNAPSHOT richfaces-core-demo org.richfaces.examples.core-demo"
       #"examples/richfaces-showcase 4.0.0.Alpha2 4.0.0-SNAPSHOT richfaces-showcase org.richfaces.examples.richfaces-showcase"
       "docs 4.0.0.Alpha2 4.0.0-SNAPSHOT richfaces-docs org.richfaces.docs"
       "dist 4.0.0.Alpha2 4.0.0-SNAPSHOT  richfaces-dist org.richfaces.dist"
      )

while getopts "hb:x" OPTION
do
     case $OPTION in
         h)
             usage
             WORK=0
             ;;
         b)
             BASE=$OPTARG
             ;;
         x)
             DEBUG=1
             ;;
         [?])
             usage;
             WORK=0
             ;;
     esac
done

if [ "$WORK" -eq "1" ] || [ "$#" -eq "0" ]
then
   work;
fi