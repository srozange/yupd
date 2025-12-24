# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Yupd is a Java-based CLI tool for updating YAML files in remote Git repositories (GitHub and GitLab) using a GitOps approach. Built with Quarkus framework and uses Maven for build management.

## Build and Development Commands

### Build and Test
- **Build project**: `./mvnw clean compile`
- **Run tests**: `./mvnw test` 
- **Full build with verification**: `./mvnw verify`
- **Run with coverage**: `./mvnw verify` (JaCoCo generates reports automatically)

### Development Mode
- **Run in dev mode**: `./mvnw quarkus:dev`
- **Build native executable**: `./mvnw package -Pnative`

### Testing Individual Components
- **Run single test class**: `./mvnw test -Dtest=ClassName`
- **Run integration tests**: `./mvnw verify -Dtest=*IntegrationTest`

## Architecture

The codebase follows **Hexagonal Architecture** (Ports and Adapters):

### Core Structure
```
├── domain/
│   ├── model/           # Business models (GitFile, GitRepository, etc.)
│   ├── ports/in/        # Input ports (GitFileUpdater interface)
│   ├── ports/out/       # Output ports (GitConnector, ContentUpdater interfaces)
│   └── service/         # Business logic implementation (GitFileUpdaterImpl)
├── application/
│   └── cli/             # CLI layer with PicoCLI commands
└── infrastructure/
    ├── git/connector/   # Git provider adapters (GithubConnector, GitlabConnector)
    ├── update/          # Content updaters (YamlPathUpdater, RegexUpdater)
    └── utils/           # Utility classes
```

### Key Components
- **GitFileUpdater** (domain/ports/in): Main business interface
- **GitFileUpdaterImpl** (domain/service): Core business logic orchestrating file updates
- **GitConnector** implementations: Adapter layer for GitHub/GitLab APIs
- **ContentUpdater** implementations: Handle YAML path and regex-based updates
- **ChainContentUpdater**: Applies multiple update criteria in sequence

### Dependencies
- **Quarkus**: Application framework with dependency injection
- **PicoCLI**: Command-line interface
- **YamlPath**: YAML path expression parsing
- **GitHub/GitLab APIs**: Repository integration

## Testing Strategy

- **Unit tests**: Domain logic and individual components
- **Integration tests**: End-to-end scenarios with mocked HTTP responses using WireMock
- **Architecture tests**: Using ArchUnit to enforce hexagonal architecture constraints
- **Test location**: All tests in `src/test/java/` following same package structure as main code

## Key Configuration

- **Java version**: 21
- **Maven wrapper**: Use `./mvnw` commands
- **Main class**: `io.github.yupd.Yupd`
- **Native compilation**: Supported via GraalVM (use `-Pnative` profile)
- **Assembly**: Creates distribution packages in `target/distributions/`

## JSpecify Configuration

The project is configured with JSpecify for null-safety annotations:

- **JSpecify dependency**: `org.jspecify:jspecify:1.0.0` (scope: provided)
- **Root package**: Marked with `@NullMarked` in `/src/main/java/io/github/yupd/package-info.java`
- **All subpackages**: Automatically generated `package-info.java` files with `@NullMarked` via Maven plugin
- **Default behavior**: All types are considered non-null unless explicitly marked with `@Nullable`

### Available annotations:
- `@Nullable` - for values that can be null
- `@NonNull` - explicit non-null (default behavior under `@NullMarked`)
- `@NullUnmarked` - to exclude classes/methods from null-safety checks

### Package-info.java Generation:
The project uses `jspecify-packageinfo-generator-maven-plugin` to automatically generate `package-info.java` files with `@NullMarked` for all subpackages during the `generate-sources` phase. Generated files are placed in `target/generated-sources/packages-info/`.

### NullAway Profile:
```bash
# Static null-safety analysis with ErrorProne + NullAway (enabled by default)
./mvnw compile
```

The NullAway profile is **activated by default** and provides compile-time null-safety checking with ErrorProne integration.

## Coding Standards

- Add comments only when necessary. Explain the why, not the how.
- Use JSpecify annotations for null-safety documentation