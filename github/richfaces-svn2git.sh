#!/bin/bash

# Program locations
xmlstarlet=/usr/bin/xmlstarlet
git=/usr/bin/git

phase1_dir=import-phase1
phase2_dir=import-phase2
svnroot=http://anonsvn.jboss.org/repos/richfaces/
svnauthors=$(dirname $0)/svn.authors
get_resources=1
get_parent=1
get_docs=1
get_main=1
publish=0
ignore_paths="^(?:3.*|archive|tests)"

fix_tags()
{
   # Convert svn remote tags to lightweight git tags
   $git for-each-ref --format='%(refname)' refs/remotes/tags/* | while read tag_ref; do
      tag=${tag_ref#refs/remotes/tags/}
      tree=$( $git rev-parse "$tag_ref": )

      # find the oldest ancestor for which the tree is the same
      parent_ref="$tag_ref";
      while [ $( $git rev-parse --quiet --verify "$parent_ref"^: ) = "$tree" ]; do
         parent_ref="$parent_ref"^
      done
      parent=$( $git rev-parse "$parent_ref" );

      # if this ancestor is in trunk then we can just tag it
      # otherwise the tag has diverged from trunk and it's actually more like a
      # branch than a tag
      merge=$( $git merge-base "refs/remotes/trunk" $parent );
      if [ "$merge" = "$parent" ]; then
          target_ref=$parent
      else
          echo "tag has diverged: $tag"
          target_ref="$tag_ref"
      fi
      target_ref=$parent

      tag_name=$( $git log -1 --pretty="format:%an" "$tag_ref" )
      tag_email=$( $git log -1 --pretty="format:%ae" "$tag_ref" )
      tag_date=$( $git log -1 --pretty="format:%ai" "$tag_ref" )
      $git log -1 --pretty='format:%s' "$tag_ref" | GIT_COMMITTER_NAME="$tag_name" GIT_COMMITTER_EMAIL="$tag_email" GIT_COMMITTER_DATE="$tag_date" $git tag -a -F - "$tag" "$target_ref"

      $git update-ref -d "$tag_ref"
   done
}

fetch_svn()
{
   mkdir -p $1
   cd $1
   $git svn init ${svnroot}${2} --no-metadata --no-minimize-url --trunk=trunk --tags=tags
   $git config svn.authorsfile ${svnauthors}
   if [[ "x${3}x" -ne "xx" ]]; then
    startrev=${3}
   else
    startrev=$(svn log --stop-on-copy ${svnroot}trunk -q | tail -2 | head -1 | awk '{print substr($1,2)}')
   fi
   headrev=$(svn info ${svnroot}${2}trunk | awk '/^Revision/ {print $2}')
   $git svn fetch --ignore-paths=${ignore_paths} --revision=${startrev}:${headrev}
   fix_tags
   $git gc
   cd ..
}
mkdir -p $phase1_dir
cd $phase1_dir

if [[ $get_parent -eq 1 ]]; then
   fetch_svn parent modules/build/parent/
fi

if [[ $get_main -eq 1 ]]; then
   startrev=$(svn log --stop-on-copy ${svnroot}trunk -q | tail -2 | head -1 | awk '{print substr($1,2)}')
   fetch_svn main "" ${startrev} 
fi

if [[ $get_docs -eq 1 ]]; then
   fetch_svn docs modules/docs/
fi

if [[ $get_resources -eq 1 ]]; then
   fetch_svn resources modules/build/resources
fi

cd ..

mkdir -p $phase2_dir
cd $phase2_dir

if [[ $publish -eq 1 ]]; then
 if [[ $get_parent -eq 1 ]]; then
   $git clone ../$phase1_dir/parent
   cd parent
   $git remote add github git@github.com:seam/parent.git
   $git remote -v
   cd ..
 fi
fi
