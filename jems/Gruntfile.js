/*global module:false*/
module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    concat: {},
    lint: {},
    min: {},
    qunit: {},
    server: {},
    test: {},
    //watch: {},
    jshint: {},
    uglify: {},

    copy: {
      main: {
        files: [
          {expand: true, cwd: 'src/main/webapp/WEB-INF/jems-views/app/', src: ['**'], dest: 'src/main/webapp/WEB-INF/jems-views/dist/'}
        ]
      }
    },
    watch: {
      files: 'src/main/webapp/WEB-INF/jems-views/app/**/*',
      tasks: 'copy'
    },
    'string-replace': {
      dist: {
        files: {
          "src/main/webapp/WEB-INF/jems-views/dist/": 'src/main/webapp/WEB-INF/jems-views/dist/**/*.jsp'
        },
        options: {
          replacements: [{
            pattern: 'href="styles',
            replacement: 'href="/styles'
          }, {
          pattern: 'src="scripts',
          replacement: 'src="/scripts'
          }]
        }
      }
    }
  });

  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-string-replace');

  grunt.registerTask('default', ['copy','string-replace','watch']);

};
