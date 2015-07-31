# Go Plugin for Bamboo User's Guide

- [Getting Started](#getting-started)
  - [Requirements](#requirements)
  - [Adding the Go Capability](#adding-the-go-capability)
    - [Automatic Configuration](#automatic-configuration)
    - [Manual Configuration](#manual-configuration)
      - [Local Agents](#local-agents)
      - [Remote Agents](#remote-agents)
- [Using the Go Plugin for Bamboo Build Tasks](#using-the-go-plugin-for-bamboo-build-tasks)
  - [Notes](#notes)
  - [Go Dependency Fetcher Task](#go-dependency-fetcher-task)
    - [Parameters](#parameters)
  - [Go Test Runner Task](#go-test-runner-task)
    - [Parameters](#parameters)
  - [Go Test Parser Task](#go-test-parser-task)
    - [Parameters](#parameters)
  - [Go Builder Task](#go-builder-task)
    - [Parameters](#parameters)

# Getting Started

## Requirements

Any agent you wish to use for building Go projects must have its own copy of [Go](http://golang.org/doc/install)
installed locally.  If you plan on building Go projects that include dependencies, you should also install Godep via `go
get github.com/tools/godep`.

## Adding the Go Capability

In order to use the Go tasks in a build, you must add the Go capability for one or more agents.  The following sections
explain how to configure the Go capability for local and remote agents.

Keep in mind that Bamboo has a hierarchy for capability values, from most general (applies to local and remote agents)
to most specific (applies only to a specific remote agent):

* Server capabilities
* Shared remote capabilities
* `bamboo-capabilities.properties`

### Automatic Configuration

* Make sure you are logged in to your Bamboo server as an administrator.
* Click the administrator dropdown and choose **Overview**.
* Click **Server capabilities** from the left-hand side of the page.

From this page you can attempt to have the Go capability automatically discovered by clicking the **Detect server
capabilities** button.  By clicking this button the Go Plugin for Bamboo will:

* Search for the `go` executable on the Bamboo server's `PATH` or, failing that, search for it within the directory
referred to by the `GOROOT` environment variable.
* Search for the `godep` executable on the Bamboo server's `PATH` or, failing that, search for it within the directory
referred to by the `GOPATH` environment variable.

Keep in mind that automatic configuration is based on the Bamboo server's environment and Go capability values may not
be valid for remote agents.  If so, make sure to override Go capability values for remote agents as necessary.

### Manual Configuration

#### Local Agents

1. Make sure you are logged in to your Bamboo server as an administrator.
2. Click the administrator dropdown and choose **Overview**.
3. Click **Server capabilities** from the left-hand side of the page.
4. To configure the path of the `go` executable:
  1. In the **Add capability** section select **Go** from the **Capability type** dropdown.
  2. Enter the path to the `go` executable in the **Path** text field.
5. To configure the path of the `godep` executable:
  1. In the **Add capability** section select **Godep** from the **Capability type** dropdown.
  2. Enter the path to the `godep` executable in the **Path** text field.

#### Remote Agents

To define the Go capability for all remote agents:

1. Make sure you are logged in to your Bamboo server as an administrator.
2. Click the administrator dropdown and choose **Agents**.
3. Click **Shared remote capabilities** from the right-hand side of the page.
4. To configure the path of the `go` executable:
  1. In the **Add capability** section select **Go** from the **Capability type** dropdown.
  2. Enter the path to the `go` executable in the **Path** text field.
5. To configure the path of the `godep` executable:
  1. In the **Add capability** section select **Godep** from the **Capability type** dropdown.
  2. Enter the path to the `godep` executable in the **Path** text field.

To define the Go capability for a specific remote agent, edit the `bamboo-capabilities.properties` file for the agent
and set:

* `capability.go` to the path of the `go` executable.
* `capability.go.godep` to the path of the `godep` executable.

# Using the Go Plugin for Bamboo Build Tasks

## Notes

* The Go Plugin for Bamboo operates by treating your build's working directory as the value of the `GOPATH` environment
variable.  For this reason, when you use the **Source Code Checkout** task, you should set the value of the **Checkout
Directory** text field to a relative path consistent with Go coding standards.  For example, if your Go project is
located at `https://github.com/username/myproject`, you would set the value of the **Checkout Directory** text field to
`src/github.com/username/myproject`.  This path will also be used for other build tasks.
* In general, the build tasks for a Go project should be, in this order:
  * Source Code Checkout
  * Go Dependency Fetcher
  * Go Test Runner
  * Go Test Parser (reads the test output generated by the Go Test Runner task)
  * Go Builder (build after testing since binaries should not be built if tests fail)
* Follow the [tutorial](tutorial.md) for a step-by-step example of the Go Plugin for Bamboo build tasks being used to
fetch a Go project's dependencies, run tests, and produce an executable artifact.

## Go Dependency Fetcher Task

The **Go Dependency Fetcher** task will retrieve a Go project's dependencies provided they have been defined using
[Godep](https://github.com/tools/godep), i.e. there is a `Godeps/Godeps.json` file available in the Go project's source
tree.

### Parameters

* **GOROOT**

  The value of the `GOROOT` environment variable for the Go installation used to build the Go project.  The Go Plugin
  for Bamboo will attempt to automatically set this value using either the value of the `GOROOT` environment variable
  defined on the Bamboo server or by using `/path/to/go/bin/..`.  If the default value isn't appropriate for your
  build you must change it.
* **Source path**

  A relative path pointing to the directory where your Go project source code has been checked out.  This path must
  follow Go conventions.  For example, if your project is located at `https://github.com/username/myproject`, the value
  of this parameter should be `github.com/username/myproject`.

## Go Test Runner Task

The **Go Test Runner Task** will run a Go project's tests and save the output for parsing by the [Go Test Parser Task](#go-test-parser-task).

### Parameters

* **GOROOT**

  The value of the `GOROOT` environment variable for the Go installation used to build the Go project.  The Go Plugin
  for Bamboo will attempt to automatically set this value using either the value of the `GOROOT` environment variable
  defined on the Bamboo server or by using `/path/to/go/bin/...`.  If the default value isn't appropriate for your
  build you must change it.
* **Source path**

  A relative path pointing to the directory where your Go project source code has been checked out.  This path must
  follow Go conventions.  For example, if your project is located at `https://github.com/username/myproject`, the value of this parameter should be `github.com/username/myproject`.
* **Packages to test**

  The names of the packages to test, with a newline separating package names.  You can pass flags by specifying them
  before or after the package name (e.g. `./... -myFlag`).  Note that you do not need to specify `-v` as it is
  automatically added.  Generally you will want to use the default value of `./...` to test all packages in your Go
  project.
* **Go test log path**

  The path where Go test logs should be stored, as a relative path (e.g. `_goTestOutput/`).
* **Save Go test output to build log**

  When checked, the output of `go test` invoked by the Go Test Runner Task will be echoed in the build log.
* **Environment variables**

  Additional environment variables to use during testing.

## Go Test Parser Task

The **Go Test Parser Task** will parse Go test logs and integrate them with Bamboo's native test reporting capabilities.
This build task is intended to run after the [Go Test Runner Task](#go-test-runner-task), but will still work without
that build task so long as there are `go test` logs available.

### Parameters

* **Go test result file pattern**

  The path where Go test logs are stored, as a relative path (e.g., `_goTestOutput/*.log`).

## Go Builder Task

The **Go Builder Task** will build and install a Go project.  Generally you should run this build task after testing
completes in order to prevent the Go project from being built after failed tests.

### Parameters

* **GOROOT**

  The value of the `GOROOT` environment variable for the Go installation used to build the Go project.  The Go Plugin
  for Bamboo will attempt to automatically set this value using either the value of the `GOROOT` environment variable
  defined on the Bamboo server or by using `/path/to/go/bin/...`.  If the default value isn't appropriate for your
  build you must change it.
* **Source path**

  A relative path pointing to the directory where your Go project source code has been checked out.  This path must
  follow Go conventions.  For example, if your project is located at `https://github.com/username/myproject`, the value of this parameter should be `github.com/username/myproject`.
* **Packages to build**

  The names of the packages to build, with a newline separating package names.  You can pass flags by specifying them
  before or after the package name (e.g. `./... -myFlag`).  Generally you will want to use the default value of `./...`
  to build all packages in your Go project.

* **Environment variables**

  Additional environment variables to use during building.
