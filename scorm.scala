import java.sql._

// Load the JDBC Driver
Class.forName("com.mysql.jdbc.Driver").newInstance()

class Article(
    var values: List[String]
  ) {
  val fields = List("id", "title", "body")
  
  // Empty Constructor sets all values to nothing
  def this() = this(List("", "", ""))
  
  def print = {
    println("id = " + this.values.apply(0) + ", title = " + this.values.apply(1) + ", body = " + this.values.apply(2))
  }
}

object ArticleFinder {
  def find(id : Int) : Article = {
    // Open the connection
    val con = DriverManager.getConnection("jdbc:mysql:///scorm", "root", "")
    
    var pstmt = con.prepareStatement("SELECT * FROM articles WHERE id = ?")
    pstmt.setString(1, id.toString)
    
    // When you call executeQuery, it returns a ResultSet
    // which you can iterate over to get each row
    val rs = pstmt.executeQuery()
    rs.next()
    var lst : List[String] = List(rs.getInt("id").toString, rs.getString("title"), rs.getString("body"))
    
    // Close the connection
    con.close()
    
    new Article(lst)  
  }
}

ArticleFinder.find(1).print