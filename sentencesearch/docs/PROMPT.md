# Convey Engineering Take-Home Exercise

Hi! We're excited you're interested in coming to work with us. Below is an exercise that will help us get know your
skills and strengths. We expect the exercise to take between two to four hours to complete.

You may use any programming language you like, but we recommend Java and include
a [boilerplate app](https://github.com/getconvey/interview-boilerplates) to save you time.

We will evaluate your program for correctness. We will also consider its design, efficiency, and readability. You should
test the code enough to know it's correct and are encouraged to include tests in your submission. Additionally please
include a summary of how your code works, list any trade-offs or assumptions, and a runtime analysis.

Please submit your source files in a regular zip or tar.gz file to us at enginterviews@getconvey.com. You may also send
any clarification or process questions to us at this address. Please also let us know how much time you spent and any
other feedback.

Feel free to use standard libraries and internet references, however all work must be your own.

Thanks and good luck!

## Sentence Search

Estimated time: 2 to 4 hours

Given a file containing one sentence per line implement a server that responds to queries
like `curl localhost:8080/count?word=eggplant`. The response should be a json document that lists all of the sentences
containing the word `eggplant` together with the number of times the word occurs in each sentence.

It should be possible to specify the file at the time the server is deployed by changing values in the configuration
file.

In greater detail, the endpoint should be `/count` and the query parameter `word`. The returned json document should
have two properties, `word`, the word provided in the `word` parameter, and `sentences`, an array of objects where each
object has a `sentence` property and a `count` property.

For example, though we do not expect formatted/indented output.

```json
{
  "word": "eggplant",
  "sentences": [
    {
      "sentence": "Eggplant or not eggplant?",
      "count": 2
    }
    {
      "sentence": "Many people don't like eggplant.",
      "count": 1
    }
  ]
}
```

If there is more than one sentence containing the word they should be ordered by decreasing `count` value with ties
broken by sentence text. You can assume words are separated by whitespace. A file of example sentences is provided
in [example.txt](https://gist.github.com/chadbay/33341bc81284a64426c6eeecfb860af0) and you can choose to read this as a
file or from the classpath as a resource. We're expecting to be able to run the server with a different file without
recompiling.
