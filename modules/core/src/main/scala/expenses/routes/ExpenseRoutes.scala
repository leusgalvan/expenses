package expenses
package routes

import java.time.LocalDate

import cats._
import eu.timepit.refined.api.Refined
import expenses.algebra.Expenses
import expenses.domain.expense._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import squants.market.USD
final class ExpenseRoutes[F[_]: Defer: Monad](
    expenses: Expenses[F]
) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/expenses"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root =>
      expenses.add(
        Expense(
          Person(Refined.unsafeApply("Alexiiiis")),
          USD(10),
          LocalDate.now,
          Set.empty[Person],
          Reference(Refined.unsafeApply("referencia")),
          None
        )
      )
      Ok("jeje")
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
