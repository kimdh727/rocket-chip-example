package rce.example.transform

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import firrtl.options.TransformLike

class TransformLikeSpec extends AnyFlatSpec with ScalaCheckPropertyChecks {

  "Add 42 transform" should "add 42" in {

    forAll { (n: Int) =>
      val transformer = Add(42)

      assert(transformer.transform(n) == n + 42)
    }
  }

  "Sub 42 transform" should "subtraction 42" in {

    forAll { (n: Int) =>
      val transformer = Sub(42)

      assert(transformer.transform(n) == n - 42)
    }
  }

  "Mul 42 transform" should "multiply 42" in {

    forAll { (n: Int) =>
      val transformer = Mul(42)

      assert(transformer.transform(n) == n * 42)
    }
  }

  "Div 42 transform" should "divide 42" in {

    forAll { (n: Int) =>
      val transformer = Div(42)

      assert(transformer.transform(n) == n / 42)
    }
  }

  "Add 42 and Sub 42 transform" should "same" in {
    forAll { (n: Int) =>
      whenever(n < 1000 && n > -1000) {
        val transformer = Seq(Add(42), Sub(42))

        assert(transformer.foldLeft(n)((acc, t) => t.transform(acc)) == n)
      }
    }
  }

  "Mul 42 and Div 42 transform" should "same" in {
    forAll { (n: Int) =>
      whenever(n < 1000 && n > -1000) {
        val transformer = Seq(Mul(42), Div(42))

        assert(transformer.foldLeft(n)((acc, t) => t.transform(acc)) == n)
      }
    }
  }
}

abstract class IntTransform extends TransformLike[Int] {
  def name: String = this.getClass.getName
}

case class Add(n: Int) extends IntTransform {

  def transform(x: Int): Int = x + n
}

case class Sub(n: Int) extends IntTransform {

  def transform(x: Int): Int = x - n
}

case class Mul(n: Int) extends IntTransform {

  def transform(x: Int): Int = x * n
}

case class Div(n: Int) extends IntTransform {

  def transform(x: Int): Int = x / n
}
