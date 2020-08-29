package expenses.modules

import cats._
import cats.effect._
import cats.implicits._
import expenses.algebra.{ Expenses, LiveExpenses }
import skunk._

object Algebras {
  def make[F[_]: Concurrent: Parallel: Timer](
      sessionPool: Resource[F, Session[F]]
  ): F[Algebras[F]] =
    for {
      expenses <- LiveExpenses.make[F](sessionPool)
    } yield new Algebras[F](expenses)
}

final class Algebras[F[_]] private (
    val expenses: Expenses[F]
)
