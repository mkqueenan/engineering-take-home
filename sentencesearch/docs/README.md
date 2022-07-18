# Sentence Search

This project is the submission of Kevin Queenan aimed to satisfy the requirements listed in PROMPT.md which was provided
by project44.

## Description

This is a Java application using the Spring framework. Most of the heavy lifting is performed by Lucene to generate an
index from a corpus of documents and to enable querying upon said index via HTTP in order to retrieve a list of relevant
documents along with the associated query term frequency.

## Quick Start

### Dependencies

* [Maven](https://formulae.brew.sh/formula/maven)
* [Java 17](https://formulae.brew.sh/formula/openjdk)
* [IntelliJ IDEA](https://www.jetbrains.com/idea/)

### Installation

You have two options. They are the following:

1. Clone the repo
   from [GitHub](https://github.com/mkqueenan/p44-take-home) ```git clone git@github.com:mkqueenan/p44-take-home.git```
2. Download the .zip attachment to my submission via email and unzip the project

Once you've retrieved the source code, navigate to the parent directory. Open the pom.xml via IntelliJ. Once the project
has been opened, execute the typical commands to get a new Java repo up and running. Those would be ```clean```
and ```package``` on the right-hand upper-most pane of the IDE labeled "Maven". At this point, the project dependencies
should be satisfied and a JAR file should have been generated in the target directory. When you're ready to run the
program, open IntelliJ, navigate to the top of your screen, click ```Run``` and
select ```Run 'SentencesearchApplication'``` which should have a green play button icon next to the command.

### Execution

Before starting the application, please navigate to the ```application.properties``` file in your IDE. This file is for
making changes to the application without requiring compilation. These K:V pairs are self-explanatory. For example, if
you intend to change the corpus to be ingested, you should alter the value for the key ```input.directory``` and insert
the path of the directory containing one or more .txt files you desire to be indexed. If you prefer to leverage env vars
instead of modifying the ```application.properties``` file, you can use ```INPUT_DIRECTORY``` instead.

Aside from externalized configuration, you may want to only run the unit tests associated with this project. If that is
the case, navigate to ```sentencesearch/src/test/java``` and right click the directory. Choose ```Run 'All Tests'```.

Assuming the configuration is to your liking and the unit tests are satisfactory, you should run the application
via ```Run 'SentencesearchApplication'```. After the JVM is up and running, use Postman or cURL to make queries as
specified in the PROMPT.md document. For example:

* ```curl GET "localhost:8080/count?word=eggplant"```
  ![Imgur](https://i.imgur.com/0lEBiON.png)
* Postman
* ![Imgur](https://i.imgur.com/ADsLwMM.png)

## Help

If you encounter any issues installing or running the application, I would ensure your machine has a compatible version
of Java installed.

The following is what I have installed on my machine:

```openjdk version "17.0.3" 2022-04-19```

```OpenJDK Runtime Environment Temurin-17.0.3+7 (build 17.0.3+7)```

```OpenJDK 64-Bit Server VM Temurin-17.0.3+7 (build 17.0.3+7, mixed mode)```

I use Homebrew exclusively as my package manager on my dev machines and would encourage you to do the same.

If that doesn't resolve the issue. Please contact me at kevin@queenan.dev or 817-751-5448 and I'll gladly assist.

## Author

Kevin Queenan

## Acknowledgments and References

* [Space Optimizations For Total Ranking by Doug Cutting](https://www.savar.se//media/1181/space_optimizations_for_total_ranking.pdf)
* [A General Guide to the Lucene Scoring Algorithm](https://lucene.apache.org/core/2_9_4/scoring.html#Algorithm)
* [Lucene Scoring in Greater Detail](https://lucene.apache.org/core/9_2_0/core/org/apache/lucene/search/package-summary.html#scoring)
* [Lucene Indexing in Greater Detail](https://lucene.apache.org/core/9_2_0/core/org/apache/lucene/index/package-summary.html)
* [Lucene Indexing Explored](https://stackoverflow.com/a/43203339)
* [Doug Cutting's Talk on Lucene in 2004](http://lucene.sourceforge.net/talks/pisa/)
* [Lucene Search Complexity Analysis](https://stackoverflow.com/a/12213375)
* [Lucene in Action, Second Edition](https://www.manning.com/books/lucene-in-action-second-edition)