package expenses
package algebra

import cats.effect._
import expenses.domain.expense._
import skunk.Session

trait Expenses[F[_]] {
  def add(expense: Expense): F[Unit]
}

object LiveExpenses {
  def make[F[_]: Sync](sessionPool: Resource[F, Session[F]]): F[Expenses[F]] =
    Sync[F].delay(new LiveExpenses(sessionPool))
}

final class LiveExpenses[F[_]: Sync] private (sessionPool: Resource[F, Session[F]]) extends Expenses[F] {
  override def add(expense: Expense): F[Unit] = sessionPool.use(_ => Sync[F].unit)
}
