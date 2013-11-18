"use strict";

module.exports = function(grunt) {

  var path = require('path');

  // Project configuration.
  grunt.initConfig({
    // Metadata.

    pkg: grunt.file.readJSON('package.json'),

    dir: {
      resources: {
        root: "../resources/META-INF/resources",
        richwidgets: "<%= dir.resources.root %>/io.richwidgets",
        jqueryui: "<%= dir.resources.root %>/com.jqueryui",
        bootstrap: "<%= dir.resources.root %>/bootstrap",
        fontawesome: "<%= dir.resources.root %>/font-awesome",
        flot: "<%= dir.resources.root %>/flot"
      },
      bower: {
        root: "bower_components/richwidgets/dist/assets",
        richwidgets: "<%= dir.bower.root %>/richwidgets",
        jqueryui: "<%= dir.bower.root %>/jquery-ui",
        bootstrap: "<%= dir.bower.root %>/bootstrap",
        fontawesome: "<%= dir.bower.root %>/font-awesome",
        flot: "<%= dir.bower.root %>/flot"
      }
    },

        // Task configuration.

    clean: {
      bower: ['bower_components'],
      resources: {
        options: { force: true },
        src: [
          '<%= dir.resources.richwidgets %>',
          '<%= dir.resources.jqueryui %>',
          '<%= dir.resources.bootstrap %>',
          '<%= dir.resources.fontawesome %>',
          '<%= dir.resources.flot %>',
        ]
      }
    },

    exec: {
      bowerInstall: {
        cmd : "bower install"
      }
    },

    copy: {
      richwidgets: {
        files: [
          {
            expand: true,
            cwd: '<%= dir.bower.richwidgets %>',
            src: '**',
            dest: '<%= dir.resources.richwidgets %>'
          }
        ]
      },
      jqueryui: {
        files: [
          {
            expand: true,
            cwd: '<%= dir.bower.jqueryui %>',
            src: '**',
            dest: '<%= dir.resources.jqueryui %>'
          }
        ]
      },
      bootstrap: {
        files: [
          {
            expand: true,
            cwd: '<%= dir.bower.bootstrap %>',
            src: '**',
            dest: '<%= dir.resources.bootstrap %>'
          }
        ]
      },
      fontawesome: {
        files: [
          {
            expand: true,
            cwd: '<%= dir.bower.fontawesome %>',
            src: '**',
            dest: '<%= dir.resources.fontawesome %>'
          }
        ]
      },
      flot: {
        files: [
          {
            expand: true,
            cwd: '<%= dir.bower.flot %>',
            src: '**',
            dest: '<%= dir.resources.flot %>'
          }
        ]
      }
    },

    subgrunt: {
      richwidgets: {
        'bower_components/richwidgets' : ['bower', 'dist']
      }
    }
  });

  // These plugins provide necessary tasks.
  grunt.loadNpmTasks('grunt-subgrunt');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-exec');

  grunt.registerTask('build', ['subgrunt:richwidgets']);

  grunt.registerTask('copy-resources', ['clean:resources', 'copy']);

  grunt.registerTask('dist', ['clean:bower', 'exec:bowerInstall', 'copy-resources']);

  // Default task.
  grunt.registerTask('default', ['dist']);

};
