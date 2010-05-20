#!/bin/sh

usage()
{
cat << EOF
usage: $0 options

This script will check out RichFaces 4.0 modules.  By default this will checkout only the primary project modules.

MODULE OPTIONS
   -c      Include cdk modules
   -t      Include tests, and docs modules
   -e      Include examples, and archetype modules
   -s      Include sandbox modules
   -a      Include all of the above

BASIC OPTIONS:
   -h      Show this message
   -d      Destination directory, otherwise the PWD is used 
   -r      Checkout in readonly mode from anonsvn
   -v      Be more verbose
EOF
}

work()
{

if [ "$READONLY" -eq "1" ]
then
   SVNBASE="http://anonsvn.jboss.org/repos/richfaces/root"
else
   SVNBASE="https://svn.jboss.org/repos/richfaces/root"
fi

if [ "$VERBOSE" -eq "0" ]
then
   SVNARGS="--quiet"
fi
  
if  [ -d $DESTINATION ]
then
   echo "Checking out to $DESTINATION"
else
   echo "Creating directory $DESTINATION to checkout to"
   mkdir $DESTINATION
fi

FINAL_LIST=( ${MAIN_MODULE_ARRAY[@]} )

if [ "$INCL_CDK" -eq "1" ]
then
   FINAL_LIST=( ${FINAL_LIST[@]} ${CDK_MODULE_ARRAY[@]} )
fi

if [ "$INCL_DOCS_TESTS" -eq "1" ]
then
   FINAL_LIST=( ${FINAL_LIST[@]} ${DOC_QE_MODULE_ARRAY[@]} )
fi

if [ "$INCL_EXAMPLES_ARCHETYPE" -eq "1" ]
then
   FINAL_LIST=( ${FINAL_LIST[@]} ${EXAMPLE_ARCHETYPE_MODULE_ARRAY[@]} )
fi

if [ "$INCL_SANDBOX" -eq "1" ]
then
   FINAL_LIST=( ${FINAL_LIST[@]} ${SANDBOX_MODULE_ARRAY[@]} )
fi

for module in "${FINAL_LIST[@]}"
do
   url="$SVNBASE/$module/trunk"
   moduledir=$DESTINATION/$module/trunk

   echo
   echo =================================

   if [ -d $moduledir ]
   then
      echo "Updating $module"
      svncmd="svn up $SVNARGS $DESTINATION/$module"
   else
      echo "Checking out $module"
      svncmd="svn co $SVNARGS $url $DESTINATION/$module"
   fi

   echo =================================

   $svncmd
  
done

}

DESTINATION=`pwd`
READONLY=0
VERBOSE=0
SVNBASE=
SVNARGS=
WORK=1
INCL_CDK=0
INCL_DOCS_TESTS=0
INCL_EXAMPLES_ARCHETYPE=0
INCL_SANDBOX=0

# Listing of all modules to be checked out
# $1 = path to module ( before trunk/tag/branch )
MAIN_MODULE_ARRAY=(
       "build/parent" 
       "build/bom" 
       "build/resources"  
       "commons"
       "core" 
       "dist"
       "ui/core"
       "ui/dist"
       # other ui modules when in place 
       # "ui/inputs"
      )

CDK_MODULE_ARRAY=(
       "cdk"
       "cdk-sandbox"
      )

DOC_QE_MODULE_ARRAY=(
       "docs"
       "tests"
      )

EXAMPLE_ARCHETYPE_MODULE_ARRAY=(
       # For each example
       # "examples/<example>"
       "examples/dist"
       "examples/core-demo"
       "examples/repeater-demo"
       "examples/richfaces-showcase"

       # For each archetype
       # "archetype/<archetype>"
       # TODO - populate after we have archetypes
      )

SANDBOX_MODULE_ARRAY=(
       # For each example in sandbox
       # "examples-sandbox/<example>"
       
       # For each component in sandbox
       # "ui-sandbox/<component>"
       "ui-sandbox/calendar"
       "ui-sandbox/componentcontrol"
       "ui-sandbox/datafilterslider"
       "ui-sandbox/datascroller"
       "ui-sandbox/drag-drop"
       "ui-sandbox/fileupload"
       "ui-sandbox/numberinputs"
       "ui-sandbox/selects"
       "ui-sandbox/tables"
       "ui-sandbox/togglepanels"
       "ui-sandbox/tree"
       "ui-sandbox/tree-model"
      )

while getopts "tecsahrd:v" OPTION
do
     case $OPTION in
         t)
             INCL_DOCS_TESTS=1
             ;;
         e)
             INCL_EXAMPLES_ARCHETYPE=1
             ;;
         c)
             INCL_CDK=1
             ;;
         s)
             INCL_SANDBOX=1
             ;;
         a)
             INCL_CDK=1
             INCL_DOCS_TESTS=1
             INCL_EXAMPLES_ARCHETYPE=1
             INCL_SANDBOX=1
             ;;
         h)
             usage
             WORK=0
             ;;
         d)
             DESTINATION=$OPTARG
             ;;
         r)
             READONLY=1
             ;;
         v)
             VERBOSE=1
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