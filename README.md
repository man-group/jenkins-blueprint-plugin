# Jenkins Recipe Builder

A Jenkins plugin where the build steps are described by a
`.jenkins.yml` file in the soruce code. Inspired by Travis CI.

## Usage

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
