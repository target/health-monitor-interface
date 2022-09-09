# health-monitor-interface

Provides a shareable health monitoring interface. This allows libraries to implement health checks
that can be provided to their users. For example, the lite-for-jdbc library provides a default health
check implementation that can be added to an application's list of health checks.

## HealthCheckAll

`healthCheckAll` executes checks for a list of monitors asynchronously. Any error thrown by a check
will be caught and transformed to an unhealthy response.

# Gradle Setup

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    // TODO INSERT NON-TARGET ARTFICAT HERE
}
```

# Development

## Building

Health Monitor Interface uses standard gradle tasks

```shell
./gradlew clean build
```

## Discussions

We're using [GitHub Discussions](https://github.com/target/health-monitor-interface/discussions),
so please chat with us there to talk about any questions, ideas, or changes you're thinking about.

## Issues

Please report issues in the issues section of the repository

## Contributing

Fork the repository and submit a pull request containing the changes, targeting the `main` branch.
and detail the issue it's meant to address.

### Code review standards

Code reviews will look for consistency with existing code standards and naming conventions.

### Testing standards

All changes should include sufficient testing to prove it is working as intended.

~~~~
