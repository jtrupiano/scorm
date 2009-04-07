import java.sql._

// Load the JDBC Driver
Class.forName("com.mysql.jdbc.Driver").newInstance()

// Establish a connection to a database called test on localhost
// this will login as root with a blank password, so adjust as necessary
// The database should exist, so create from the commmand line with:
//     mysqladmin -u root create test
val con = DriverManager.getConnection("jdbc:mysql:///scorm", "root", "")

try {
  
  if(!con.isClosed()) {
    println("Successfully connected to MySQL server using TCP/IP...")
  }

  // To execute a SQL Query, you have to create Statement object
  // and then call executeUpdate on it, passing the SQL query as a String
  var stmt = con.createStatement()
  stmt.executeUpdate("DROP TABLE IF EXISTS articles")
  stmt.executeUpdate("CREATE TABLE articles ("+
    "id int(11) NOT NULL auto_increment, "+
    "title varchar(255), " +
    "body tinytext, "+
    "PRIMARY KEY (id))")
  stmt.close()
  
  // A prepared statement allows you to do use query parameters
  var pstmt = con.prepareStatement(
    "INSERT INTO articles (title, body) VALUES (?, ?)",
    Statement.RETURN_GENERATED_KEYS)
  
  // Set the values for the query parameters
  // Yes, JDBC uses a 1-based index, instead of 0-based
  pstmt.setString(1, "First Post!")
  pstmt.setString(2, "Blah Blah Blah")
  
  // Execute the insert
  pstmt.execute()
  
  // Generated Keys will give us access to the ID that was created by the INSERT
  var rs = pstmt.getGeneratedKeys()
  if(rs.next()) {
    println("Created Article #"+rs.getInt(1))
  }    
  
  pstmt = con.prepareStatement("SELECT * FROM articles")
  
  // When you call executeQuery, it returns a ResultSet
  // which you can iterate over to get each row
  rs = pstmt.executeQuery()
  while(rs.next()) {
    println("Article id="+rs.getInt("id")+
      " title="+rs.getString("title")+
      " body="+rs.getString("body"))
  }
  
} catch {
  case e => e.printStackTrace()
} finally {
  con.close()
}
