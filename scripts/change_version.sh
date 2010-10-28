#!/bin/sh

usage()
{
cat << EOF
usage: $0 options

This script will search all pom.xml and change <version>ORIG</version> to <version>NEW</version>  

BASIC OPTIONS:
   -h      Show this message
   -d      Destination directory, otherwise the PWD is used 
   -o      Version to replace - such as 4-SNAPSHOT
   -n      Version to replace with - such as 5
   
   BUGS: This is traverse into tags as well, so some may need to be reverted
   
EOF
}

work()
{
  
if  [ -d "$DESTINATION" ]
then
   echo "Beginning version update from base directory: $DESTINATION"
else
   echo "Base directory does not exist, can not update version from: $DESTINATION"
   exit 1;
fi

   echo
   echo =================================
   echo "Changing <version>$ORIG_VERSION</version> into <version>$NEW_VERSION</version>"

   find $DESTINATION -name "pom.xml" -path *trunk/* | xargs perl -pi -e "s/<version>$ORIG_VERSION<\/version>/<version>$NEW_VERSION<\/version>/"
   
   echo =================================

}

DESTINATION=`pwd`
WORK=1
ORIG_VERSION=
NEW_VERSION=

while getopts "n:o:d:h" OPTION
do
     case $OPTION in
         n)
             NEW_VERSION=$OPTARG
             ;;
         o)
             ORIG_VERSION=$OPTARG
             ;;
         d)
             DESTINATION=$OPTARG
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

if [ "$WORK" -eq "1" ] || [ "$#" -eq "0" ]
then
   work;
fi