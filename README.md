# Apache Velocity Templates with JSON input

## Description

This application reads the contents of a JSON file and adds the values to a Velocity Context Object, then merges all the templates present in a directory recursively.

Can be used to generate entire projects with specific directory structure and multiple files.

## Features

- The template files are identified by the "vm" extension, for example, index.html.vm will be merged and become index.html.

- For more information about the Apache Velocity Templates, please refer to: <https://velocity.apache.org/engine/2.3/vtl-reference.html>

- Only UTF-8 encoding is supported for the template files.

- The regular files will be copied from the templates directory to the target directory without changes.

- The directories and filenames can have variables in the name that will be replaced with the values defined in the JSON file, however, the syntax used is ADirectory{xVariable}/FileName{yVariable}.ext

- The paths can have multiple variables but just only one Array Variable, if that is the case,  multiple files will be generated and the Variables available in the template of that file are the fields of the iterated variable. 

## Installation

### Preconditions

Apache Maven is needed for compiling and installing.

### Maven Installation

Clone this repository and run:

`mvn clean install`

This will add the dependencies required in your local m2 repository.

Add the plug-in to the build.gradle file:

```
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath group: 'io.github.aoguerrero',
        name: 'vjtemplates', 
        version: '0.1'
    }
}
apply plugin: 'io.github.aoguerrero.vjtemplates'

```

## Usage

### As a Gradle Plugin

Use:

`gradle projectSkeleton`

By default it will look for the templates in the current path and the templates directory.

### As a standalone application

This library can be used as a standalone application providing three parameters in the command line:

- Input Directory, where the templates are stored.

- Output Directory, the target of the merged files.

- JSON File, the path of the file with the variables defined.

## TO-DO

- Load templates from URL.

- Incremental artifact generation.

