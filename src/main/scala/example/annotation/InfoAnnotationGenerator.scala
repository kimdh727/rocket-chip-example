package rce.example.annotation

import rce.util.GeneratorUtil

object InfoAnnotationGenerator extends App {
  GeneratorUtil(module = classOf[InfoModule])
}
