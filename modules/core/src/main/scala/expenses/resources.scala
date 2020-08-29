package expenses

import cats.effect.{ ConcurrentEffect, ContextShift, Resource }
import skunk.{ Session, SessionPool }
import natchez.Trace.Implicits.noop // needed for skunk

final case class AppResources[F[_]](
    psql: Resource[F, Session[F]]
)

object AppResources {

  def make[F[_]: ConcurrentEffect: ContextShift](): Resource[F, AppResources[F]] = {
    def mkPostgreSqlResource(): SessionPool[F] =
      Session
        .pooled[F](
          host = "localhost",
          port = 5432,
          user = "postgresql",
          database = "expenses",
          max = 10
        )

    mkPostgreSqlResource().map(AppResources.apply[F])
  }
}
