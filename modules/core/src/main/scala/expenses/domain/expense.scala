package expenses.domain

import java.time.LocalDate
import java.util.UUID

import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype
import squants.Money

object expense {
  @newtype case class Person(name: NonEmptyString)
  @newtype case class Reference(reference: NonEmptyString)
  @newtype case class Note(note: NonEmptyString)

  // TODO: Maybe refine debtors to enforce payer is not included

  case class Expense(
      uuid: UUID,
      payer: Person,
      amount: Money,
      date: LocalDate,
      debtors: List[Person],
      reference: Reference,
      note: Option[Note]
  )
}
