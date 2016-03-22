package product;

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import io.gatling.http.config.HttpProtocolBuilder.toHttpProtocol
import io.gatling.http.request.builder.HttpRequestBuilder.toActionBuilder

class StockSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:8080/api/transaction/async/")
    .acceptHeader("text/html,application/xhtml+xml,application/json;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val feeder = csv("search.csv").circular

  val scn = scenario("StockSimulation")
  .during(100){
     feed(feeder)
    .exec(http("stock_operation")
    .get("${sku}/${size}")
    .check(status.is(200))
    .check(bodyString.is("${sku}")))
	}
  setUp(
    scn.inject(atOnceUsers(100))
  ).protocols(httpConf)
}
