package ru.scala.learning.utils
import com.github.tminglei.slickpg._

trait MyPostgresProfile extends ExPostgresProfile
  with PgArraySupport
  with PgDate2Support
  with PgRangeSupport
  with PgHStoreSupport
  with PgPlayJsonSupport
  with PgSearchSupport
//  with PgPostGISSupport
  with PgNetSupport
  with PgLTreeSupport {
  def pgjson = "jsonb" // jsonb support is in postgres 9.4.0 onward; for 9.3.x use "json"

  // Add back `capabilities.insertOrUpdate` to enable native `upsert` support; for postgres 9.5+
  //override protected def computeCapabilities: Set[Capability] =
  //  super.computeCapabilities + JdbcProfile.capabilities.insertOrUpdate

  override val api = MyAPI

  object MyAPI extends API with ArrayImplicits
    with DateTimeImplicits
    //  with JsonImplicits
    with NetImplicits
    with LTreeImplicits
    with RangeImplicits
    with HStoreImplicits
    with SearchImplicits
    with SearchAssistants {
    implicit val intListTypeMapper = new SimpleArrayJdbcType[Int]("integer").to(_.toList)
    implicit val intListListTypeMapper = new AdvancedArrayJdbcType[List[Int]]("integer[]",
      utils.SimpleArrayUtils.fromString[List[Int]](s =>
        scala.util.Try(s.substring(5, s.length - 1).split(",").map(_.trim.toInt).toList).getOrElse(List())
      )(_).orNull,
      utils.SimpleArrayUtils.mkString[List[Int]](_.toString)
    ).to(_.toList)
  }
}

object MyPostgresProfile extends MyPostgresProfile
