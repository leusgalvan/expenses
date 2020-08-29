package expenses

import cats.effect._
import expenses.modules.{ Algebras, HttpApi }
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    AppResources.make[IO]().use { res =>
      for {
        algebras <- Algebras.make[IO](res.psql)
        api <- HttpApi.make[IO](algebras)
        _ <- BlazeServerBuilder[IO](ExecutionContext.global)
              .withHttpApp(api.httpApp)
              .serve
              .compile
              .drain
      } yield ExitCode.Success

    }
}
