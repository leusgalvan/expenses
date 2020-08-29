package expenses
package algebra

import cats.effect._
import expenses.domain.expense._
import skunk._
import skunk.codec.all._
import skunk.implicits._
import squants.market.Money

trait Expenses[F[_]] {
  def add(expense: Expense): F[Unit]
}

object LiveExpenses {
  def make[F[_]: Sync](sessionPool: Resource[F, Session[F]]): F[Expenses[F]] =
    Sync[F].delay(new LiveExpenses(sessionPool))
}

final class LiveExpenses[F[_]: Sync] private (sessionPool: Resource[F, Session[F]]) extends Expenses[F] {
  override def add(expense: Expense): F[Unit] = ???
  /*sessionPool.use { session =>
      session.prepare(insertOrder).use { cmd =>
        GenUUID[F].make[OrderId].flatMap { id =>
          val itMap = items.map(x => x.item.uuid -> x.quantity).toMap
          val order = Order(id, paymentId, itMap, total)
          cmd.execute(userId ~ order).as(id)
        }
      }
    }*/
}

private object ExpensesQueries {
  val encoder: Encoder[Expense] = (uuid ~ varchar.contramap[Person](_.name.value) ~ numeric.contramap[Money](_.value) ~
      date ~ varchar.contramap[List[Person]](people => people.mkString(" ")) ~ varchar.contramap[Reference](
        _.reference.value
      ) ~ varchar.contramap[Option[Note]](_.fold("")(_.note.value))).contramap { exp =>
    exp.uuid ~ exp.payer ~ exp.amount ~ exp.date ~ exp.debtors ~ exp.reference ~ exp.note
  }

  def insertExpense: Command[Expense] =
    sql"insert into expenses values ($encoder)".command
}
