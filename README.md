# Jenkins YAML Project

A Jenkins plugin that lets you describe projects using a YAML file in
the repo itself.

Inspired by Travis CI.

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
$ cp target/yamlproject.hpi ~/.jenkins/plugins/
```
