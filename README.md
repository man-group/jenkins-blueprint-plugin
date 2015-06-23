# Jenkins Recipe Builder

Build Jenkins projects according to a `.jenkins.yml` file in the
repository. Inspired by Travis CI.

MIT license, see COPYING.

## Developing

You will need to generate a .hpi file.

```bash
# on our artifactory this may require running several times.
$ mvn package
```

Next, download jenkins.jar from http://jenkins-ci.org/ and run it:

```bash
$ java -jar jenkins.war
```

Copy your new plugin into your dev Jenkins instance:

```bash
$ cp target/recipebuilder.hpi ~/.jenkins/plugins/
```

## Installing

Visit the Plugin Manager in your Jenkins instance
(e.g. `http://example.com/pluginManager/advanced`) and upload the
`recipebuilder.hpi` file.
