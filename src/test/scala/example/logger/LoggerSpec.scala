package rce.example.logger

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._

import logger._

class LoggerSpec extends AnyFlatSpec {

  import LoggerSpec.logger
  import SimpleLogger._

  "logger level none" should "show error, warn message" in {
    val message = logger(LogLevel.None)

    message should include { errorMessage }
    message should include { warnMessage }
    message should not include { infoMessage }
    message should not include { debugMessage }
    message should not include { traceMessage }
  }

  "logger level error" should "show error message" in {
    val message = logger(LogLevel.Error)

    message should include { errorMessage }
    message should not include { warnMessage }
    message should not include { infoMessage }
    message should not include { debugMessage }
    message should not include { traceMessage }
  }

  "logger level warn" should "show error, warn message" in {
    val message = logger(LogLevel.Warn)

    message should include { errorMessage }
    message should include { warnMessage }
    message should not include { infoMessage }
    message should not include { debugMessage }
    message should not include { traceMessage }
  }

  "logger level info" should "show error, warn, info message" in {
    val message = logger(LogLevel.Info)

    message should include { errorMessage }
    message should include { warnMessage }
    message should include { infoMessage }
    message should not include { debugMessage }
    message should not include { traceMessage }
  }

  "logger level debug" should "show error, warn, info, debug message" in {
    val message = logger(LogLevel.Debug)

    message should include { errorMessage }
    message should include { warnMessage }
    message should include { infoMessage }
    message should include { debugMessage }
    message should not include { traceMessage }
  }

  "logger level trace" should "show error, warn, info, debug, trace message" in {
    val message = logger(LogLevel.Trace)

    message should include { errorMessage }
    message should include { warnMessage }
    message should include { infoMessage }
    message should include { debugMessage }
    message should include { traceMessage }
  }
}

object LoggerSpec {
  def logger(level: LogLevel.Value) = {

    Logger.makeScope() {
      val captor = new Logger.OutputCaptor

      Logger.setOutput(captor.printStream)
      Logger.setLevel(level)

      (new SimpleLogger).logging()

      captor.getOutputAsString
    }
  }
}

class SimpleLogger extends LazyLogging {

  import SimpleLogger._

  def logging() = {
    logger.error(errorMessage)
    logger.warn(warnMessage)
    logger.info(infoMessage)
    logger.debug(debugMessage)
    logger.trace(traceMessage)
  }
}

object SimpleLogger {
  val errorMessage = "[ERROR]\terror message"
  val warnMessage  = "[WARN]\twarning message"
  val infoMessage  = "[INFO]\tinformation message"
  val debugMessage = "[DEBUG]\tdebug message"
  val traceMessage = "[TRACE]\ttrace message"
}
