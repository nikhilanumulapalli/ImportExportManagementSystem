import org.scalatest.{BeforeAndAfter, FunSuite}
import Customer.SearchProduct
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import Product.Product

class ProductTest extends FunSuite with BeforeAndAfter with MockitoSugar{
  test("search products through filter"){
    val productSearchService = mock[SearchProduct]

    when(productSearchService.search("tv")).thenReturn(
      Seq(Product("9 inch-tv", 120.0, 110.0, "jdc", List(), "jdhc123", 10),
        Product("10 inch-tv", 120.0, 110.0, "jdc", List(), "jdhc123", 10)))

    val products = productSearchService.search("tv")
  
    assert(products.map(x => x.name.contains("tv")).reduce(_ && _))
  }
}
