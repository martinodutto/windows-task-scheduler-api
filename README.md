# About windows-task-scheduler-api

This is a lightweight library to deal with the [Windows task scheduler](https://docs.microsoft.com/en-us/windows/win32/taskschd/task-scheduler-start-page) from a Java application.

Every provided functionality relies, under the hood, on the `schtasks.exe` Windows command tool.

## Which Java version do I need?

This library targets Java 8 or later.

## Current version :radioactive:

The software is still in beta, especially because I need to test things thoroughly.

## Usage

It's as simple as using one of the classes provided into the `cloud.martinodutto.wtsapi.api` package, depending on which operation you need to perform.

For example, to query for the information concerning a specific task:

```Java
Query q = Query.of(LocalConfigurationParameters.getInstance());
List<Map<String, String>> taskInfos = q.queryForTask(myTaskName);
```

Implementations of the `ConfigurationParameters` interface will help you provide several custom parameters to deal with remote systems or special `schtasks.exe` paths.
Check out `LocalConfigurationParameters` if you just need to use the task scheduler from a local computer.

All the classes you need to use reflect the name of the operation that the Windows command will perform, so you will find:

|     Class     | Description |
| ------------- | ------------- |
| `Delete`      | Deletes one or more tasks |
| `Run`         | Immediately runs a scheduled task  |
| `End`         | Stops a running scheduled task |
| `Query`       | Retrieves information for one or more tasks |
| `Create`      | :warning: Still working on it :warning: |
| `Change`      | :warning: Still working on it :warning: |

Any of the previous classes can be created in just one way: using the `of` static method.

## Dependency management

Even though this project is built with Maven, this library is still unavailable on any public repository. :cry:

So you'll have to build it by yourself.
But don't panic! I am planning to deploy it somewhere, as soon as I can. :wink:

## Contributing

Pull requests are welcome!
