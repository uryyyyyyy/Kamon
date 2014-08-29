/*
 * =========================================================================================
 * Copyright © 2013-2014 the kamon project <http://kamon.io/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 * =========================================================================================
 */
package kamon.metric

import akka.actor.ActorSystem
import com.typesafe.config.Config
<<<<<<< Updated upstream
import kamon.metric.instrument.{ Counter, Histogram }
=======
import kamon.metric.instrument.{Counter, Histogram}
>>>>>>> Stashed changes

case class RouterMetrics(name: String) extends MetricGroupIdentity {
  val category = RouterMetrics
}

object RouterMetrics extends MetricGroupCategory {
  val name = "router"

  case object ProcessingTime extends MetricIdentity { val name = "processing-time" }
  case object TimeInMailbox extends MetricIdentity { val name = "time-in-mailbox" }
  case object Errors extends MetricIdentity { val name = "errors" }

  case class RouterMetricsRecorder(processingTime: Histogram, timeInMailbox: Histogram, errors: Counter) extends MetricGroupRecorder {

    def collect(context: CollectionContext): RouterMetricSnapshot =
      RouterMetricSnapshot(processingTime.collect(context), timeInMailbox.collect(context), errors.collect(context))

    def cleanup: Unit = {
      processingTime.cleanup
      timeInMailbox.cleanup
      errors.cleanup
    }
  }

  case class RouterMetricSnapshot(processingTime: Histogram.Snapshot, timeInMailbox: Histogram.Snapshot,
<<<<<<< Updated upstream
      errors: Counter.Snapshot) extends MetricGroupSnapshot {
=======
                                 errors: Counter.Snapshot) extends MetricGroupSnapshot {
>>>>>>> Stashed changes

    type GroupSnapshotType = RouterMetricSnapshot

    def merge(that: RouterMetricSnapshot, context: CollectionContext): RouterMetricSnapshot =
      RouterMetricSnapshot(
        processingTime.merge(that.processingTime, context),
        timeInMailbox.merge(that.timeInMailbox, context),
        errors.merge(that.errors, context))

    lazy val metrics: Map[MetricIdentity, MetricSnapshot] = Map(
      ProcessingTime -> processingTime,
      TimeInMailbox -> timeInMailbox,
      Errors -> errors)
  }

  val Factory = new MetricGroupFactory {
    type GroupRecorder = RouterMetricsRecorder

    def create(config: Config, system: ActorSystem): RouterMetricsRecorder = {
      val settings = config.getConfig("precision.router")

      val processingTimeConfig = settings.getConfig("processing-time")
      val timeInMailboxConfig = settings.getConfig("time-in-mailbox")

      new RouterMetricsRecorder(
        Histogram.fromConfig(processingTimeConfig),
        Histogram.fromConfig(timeInMailboxConfig),
        Counter())
    }
  }
}
