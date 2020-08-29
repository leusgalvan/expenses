package expenses.domain

import java.time.LocalDate

import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype
import squants.Money

object expense {
  @newtype case class Person(name: NonEmptyString)
  @newtype case class Reference(reference: NonEmptyString)
  @newtype case class Note(note: NonEmptyString)

  // TODO: Maybe refine debtors to enforce payer is not included

  case class Expense(
      payer: Person,
      amount: Money,
      date: LocalDate,
      debtors: Set[Person],
      reference: Reference,
      note: Option[Note]
  )
}
