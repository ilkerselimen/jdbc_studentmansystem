import java.sql.*;

//4-DB ile iletişimde olan class
public class StudentRepository {

    private Connection conn;
    private Statement st;
    private PreparedStatement prst;

    //5-connection için bir metod oluştur
    private void getConnection(){
        try {
            this.conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/jdbc_db","dev_user","password");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    //6-statement oluşturmak için metod
    private void getStatement(){
        try {
            this.st=conn.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
    }

    //7-prepared statement oluşturma metodu
    private void getPreparedStatement(String query){
        try {
            this.prst= conn.prepareStatement(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
    }

    //8-tablo oluşturma
    public void createTable(){
        getConnection();
        getStatement();
        try {
            st.execute("CREATE TABLE IF NOT EXISTS t_student(id SERIAL,name VARCHAR(50),lastname VARCHAR(50),city VARCHAR(20),age INT)");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //12-tabloya veri ekleme
    public void save(Student student) {
        getConnection();
        String query="INSERT INTO t_student(name,lastname,city,age) VALUES(?,?,?,?)";
        getPreparedStatement(query);
        try {
            prst.setString(1,student.getName());
            prst.setString(2,student.getLastName());
            prst.setString(3,student.getCity());
            prst.setInt(4,student.getAge());
            prst.executeUpdate();
            System.out.println("Kayıt işlemi başarıyla gerçekleştirildi.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                prst.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }



    }

    //14-tüm kayıtları görüntüleme
    public void findAll() {
        getConnection();
        getStatement();
        String query="SELECT * FROM t_student";
        try {
            ResultSet resultSet=st.executeQuery(query);
            while (resultSet.next()){
                System.out.print("id: "+resultSet.getInt("id"));
                System.out.print(" - ad: "+resultSet.getString("name"));
                System.out.print(" - soyad: "+resultSet.getString("lastname"));
                System.out.print(" - sehir: "+resultSet.getString("city"));
                System.out.print(" - yas: "+resultSet.getInt("age"));
                System.out.println();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //16-tablodan veri silme
    public void delete(int id) {
        getConnection();
        String query="DELETE FROM t_student WHERE id=?";
        getPreparedStatement(query);
        try {
            prst.setInt(1,id);
            int deleted=prst.executeUpdate();
            if (deleted>0){
                System.out.println("id:"+id+" olan kayıt başarıyla silinmiştir");
            }else System.out.println("id:"+id+" ile eşleşen kayıt bulunamadı");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                prst.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }



}
