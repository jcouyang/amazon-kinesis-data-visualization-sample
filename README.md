# Amazon Kinesis Data Visualization Sample Application

## Features

The Amazon Kinesis Data Visualization Sample Application contains three components:

- [X] A [KPL][kpl] producer to send data to [Amazon Kinesis][kinesis].
- [X] A [KCL][kcl] consumer to compute the number of HTTP requests a resource received, and the HTTP referrer of them, over a sliding window.
- [ ] An embedded web server and real time chart to display counts as they are computed.

The application will create one Amazon Kinesis stream with two shards and two Amazon DynamoDB tables in your AWS account.

## Getting Started
1. auth to your aws account
2. `./cloudformation/create-stack`
2. `cd producer && sbt run`
3. open another terminal and `cd consumer && sbt run`
4. enjoy the logs

[kinesis]: http://aws.amazon.com/kinesis
[kcl]: https://docs.aws.amazon.com/streams/latest/dev/developing-consumers-with-kcl.html
[kpl]: https://docs.aws.amazon.com/streams/latest/dev/developing-producers-with-kpl.html
