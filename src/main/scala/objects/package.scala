import models.{Identifiable, Picture, Product}
import repo.ProductRepo
import sangria.macros.derive._
import sangria.schema.{InterfaceType, fields, _}

package object objects {
  implicit val PictureType = deriveObjectType[Unit, Picture](
    ObjectTypeDescription("The product picture"),
    DocumentField("url", "Picture CDN URL"))

  val IdentifiableType = InterfaceType(
    "Identifiable",
    "Entity that can be identified",
    fields[Unit, Identifiable](Field("id", StringType, resolve = _.value.id)))

  val ProductType = deriveObjectType[Unit, Product](
    Interfaces(IdentifiableType),
    IncludeMethods("picture"))

  val Id = Argument("id", StringType)

  val QueryType = ObjectType(
    "Query",
    fields[ProductRepo, Unit](
      Field("product",
            OptionType(ProductType),
            description = Some("Returns a product with specific `id`."),
            arguments = Id :: Nil,
            resolve = c => c.ctx.product(c arg Id)),
      Field("products",
            ListType(ProductType),
            description = Some("Returns a list of all available products."),
            resolve = _.ctx.products)
    )
  )

  val schema = Schema(QueryType)
}
