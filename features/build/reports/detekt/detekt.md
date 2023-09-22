# detekt

## Metrics

## Complexity Report

## Findings (2)

### formatting, TrailingCommaOnCallSite (2)

Rule to mandate/forbid trailing commas at call sites

[Documentation](https://detekt.dev/docs/rules/formatting#trailingcommaoncallsite)

* /Users/rodrigosicarelli/Workspace/Educacional/kplatform/features/details/build.gradle.kts:9:15
```
Missing trailing comma before ")"
```
```kotlin
6      kotlin("android")
7  }
8  
9  androidLibrary(
!                ^ error
10     compilationOptionsBuilder = {
11         optIn(ExperimentalCoroutinesApi)
12     }

```

* /Users/rodrigosicarelli/Workspace/Educacional/kplatform/features/home/build.gradle.kts:9:15
```
Missing trailing comma before ")"
```
```kotlin
6      kotlin("android")
7  }
8  
9  androidLibrary(
!                ^ error
10     compilationOptionsBuilder = {
11         optIn(ExperimentalMaterial3)
12     }

```

generated with [detekt version 1.23.1](https://detekt.dev/) on 2023-09-22 16:47:32 UTC
