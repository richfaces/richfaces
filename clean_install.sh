#!/bin/sh

usage()
{
cat << EOF
usage: $0 options

This script will execute "mvn clean install" on all modules in RichFaces 4.0.  With the the various options below.  

MODULE OPTIONS
   -c      Include cdk modules
   -t      Include tests, and docs modules (NOT FUNCTIONAL)
   -e      Include examples, and archetype modules (NOT FUNCTIONAL)
   -s      Include sandbox modules
   -a      Include all of the above

BASIC OPTIONS:
   -h      Show this message
   -d      Root directory, otherwise the PWD is used 
   -x      Use mvn -X option
   -y      Skip enforcement rules - this is needed if using cdk plugin snapshots

TODO Add options for skip test, skip check style etc...
EOF
}

work()
{

if [ "$DEBUG" -eq "1" ]
then
   MVNARGS="-X"
fi

if [ "$SKIP_ENFORCE" -eq "1" ]
then
   MVNARGS="$MVNARGS -Dskip-enforce"
fi
  
if  [ -d "$DESTINATION" ]
then
   echo "Beginning build from base directory: $DESTINATION"
else
   echo "Base directory does not exist, can not build from: $DESTINATION"
   exit 1;
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
   moduledir="$DESTINATION/$module"
   command="mvn $MVNARGS clean install" 
   echo
   echo =================================
   echo "Moving into module directory : $moduledir"
   if [ -d "$moduledir" ]
   then
      echo "Executing command : $command"
      cd $moduledir
      $command
   else
      echo "Module directory does not exist, can not execute command: $command"
      echo =================================
      exit 1;
   fi
   
   echo =================================

done

}

DESTINATION=`pwd`
DEBUG=0
SKIP_ENFORCE=0
MVNARGS=
WORK=1
INCL_CDK=0
INCL_DOCS_TESTS=0
INCL_EXAMPLES_ARCHETYPE=0
INCL_SANDBOX=0

# Listing of all modules to be checked out
# $1 = path to module ( before trunk/tag/branch )
MAIN_MODULE_ARRAY=(
       "build/parent/trunk" 
       "build/bom/trunk" 
       "build/resources/trunk/checkstyle"  
       "commons/trunk"
       "core/trunk" 
       "ui/core/trunk"
       "ui/misc/trunk"
       # other ui modules when in place 
       # TODO NOT SURE WHAT TO DO WITH /DISTS
      )

CDK_MODULE_ARRAY=(
       "cdk/trunk"
       #"cdk-sandbox/trunk/xsd2javadoc"
       #"cdk-sandbox/trunk/maven-resource-dependency-plugin"
      )

DOC_QE_MODULE_ARRAY=(
       # "docs/trunk"
       # "tests/trunk"
      )

EXAMPLE_ARCHETYPE_MODULE_ARRAY=(
       # For each example
       # "examples/<example-name>"
       "examples/core-demo"
       "examples/repeater-demo"
       "examples/richfaces-showcase"
       "examples/dist"

       # For each archetype
       # "archetype/archetype-name"
       # TODO - populate after we have archetypes
      )

SANDBOX_MODULE_ARRAY=(
       # For each example in sandbox
       # "examples-sandbox/<example-name>"
       
       # For each component in sandbox
       # "ui-sandbox/<component-name>"
       "ui-sandbox/tables/trunk"
       "ui-sandbox/datascroller/trunk"
       "ui-sandbox/componentControl/trunk"
       
       # These exist but are just shells
       # "ui-sandbox/calendar"
       # "ui-sandbox/datafilterslider"
       # "ui-sandbox/drag-drop"
       # "ui-sandbox/fileupload"
       # "ui-sandbox/numberinputs"
       # "ui-sandbox/selects"
       # "ui-sandbox/togglepanels"
       # "ui-sandbox/tree"
       # "ui-sandbox/tree-model"
      )

while getopts "tecsahd:xy" OPTION
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
         x)
             DEBUG=1
             ;;
         y)
             SKIP_ENFORCE=1
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