# Rocketchip Example [![GitHub Actions SBT CI](https://github.com/kimdh727/rocket-chip-example/actions/workflows/ci.yml/badge.svg)](https://github.com/kimdh727/rocket-chip-example/actions/workflows/ci.yml)

This Repository contanins [Rocketchip](https://github.com/chipsalliance/rocket-chip) examples.

## SBT CI

```sh
sbt compile
sbt test
```

## Adder

### Adder Test Harness

https://github.com/kimdh727/rocket-chip-example/blob/2e7191dc30ab66a5c0922f1c81978b5d375d9473/src/main/scala/example/adder/AdderTestHarness.scala#L9-L40

### Adder Generator

```sh
sbt "runMain example.adder.AdderGenerator"
```

https://github.com/kimdh727/rocket-chip-example/blob/2e7191dc30ab66a5c0922f1c81978b5d375d9473/src/main/scala/example/adder/AdderGenerator.scala#L7-L35

### Adder Spec

```sh
sbt "testOnly adder.AdderSpec"
```

https://github.com/kimdh727/rocket-chip-example/blob/2e7191dc30ab66a5c0922f1c81978b5d375d9473/src/test/scala/adder/AdderSpec.scala#L11-L24
