package rce.example.transform

import org.scalatest.flatspec.AnyFlatSpec

import firrtl.options._
import firrtl.graph.DiGraph
// import logger._

class DependencyManagerSpec extends AnyFlatSpec {
  // Logger.setLevel(LogLevel.Trace)

  it should "linearlize" in {
    import scala.collection.mutable.{LinkedHashMap => Map, LinkedHashSet => Set}

    val edges: Map[Int, Set[Int]] =
      Map(
        // 0 -> Set.empty,
        0 -> Set(1, 2, 3),
        1 -> Set(2, 3, 4),
        2 -> Set.empty,
        3 -> Set(5),
        4 -> Set.empty,
        5 -> Set(4)
      )

    println(DiGraph(edges).getEdgeMap.mkString("\n"))
    println(DiGraph(edges).reverse.getEdgeMap.mkString("\n"))
    // println(DiGraph(edges).linearize)

    println(Seq[Int]().foldLeft(10) { _ + _ })
  }

  it should "step a" in {
    val targets = Seq(Dependency[A])

    val stepManager = new StepManager(targets)

    println(stepManager.invalidateGraph.linearize.mkString("\n"))

    stepManager.transform("")
  }

  it should "step pre a" in {
    val targets = Seq(Dependency[PreA])

    val stepManager = new StepManager(targets)

    // println(stepManager.dependencyGraph.getEdgeMap.mkString("\n"))

    // println(stepManager.dependenciesToGraphviz)

    println(stepManager.invalidateGraph.linearize.reverse.mkString("\n"))

    stepManager.transform("")
  }

  it should "step opt pre a" in {
    val targets = Seq(Dependency[OptPreA])

    val stepManager = new StepManager(targets)

    stepManager.transform("")
  }

  it should "step opt pre of a" in {
    val targets = Seq(Dependency[OptPreOfA])

    val stepManager = new StepManager(targets)

    stepManager.transform("")
  }
}

class StepManager(
    val targets: Seq[Dependency[Step]],
    val currentState: Seq[Dependency[Step]] = Seq.empty,
    val knownObjects: Set[Step] = Set.empty
) extends DependencyManager[String, Step] with Step {

  import collection.immutable.{Set => ISet}

  protected def copy(
      a: Seq[Dependency[Step]],
      b: Seq[Dependency[Step]],
      c: ISet[Step]
  ) = new StepManager(a, b, c)
}

trait Step extends TransformLike[String] with DependencyAPI[Step] {

  def name: String = this.getClass.getName
}

trait IdentityStep extends Step {
  def transform(a: String): String = { println(s"transform::: $name"); a }
}

class A extends IdentityStep {

  override def invalidates(a: Step): Boolean = a == Dependency[PreA].getObject()
}

class PreA extends IdentityStep {

  override def prerequisites = Seq(Dependency[A])
}

class OptPreA extends IdentityStep {

  override def optionalPrerequisites = Seq(Dependency[A])
}

class OptPreOfA extends IdentityStep {

  override def optionalPrerequisiteOf = Seq(Dependency[A])
}
