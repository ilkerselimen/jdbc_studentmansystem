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
        try {//idye unique constrainti eklenmeli
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
        String sql="SELECT * FROM t_student";
        try {
            ResultSet resultSet=st.executeQuery(sql);
            System.out.println("+"+"-".repeat(79)+"+");
            System.out.printf("| %-5s| %-20s| %-20s| %-20s| %-5s|\n", "id","ad","soyad","sehir","yas");
            while (resultSet.next()){
                System.out.printf("| %-5d| %-20s| %-20s| %-20s| %-5d|\n", resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("lastname"),
                        resultSet.getString("city"),
                        resultSet.getInt("age"));
            }
            System.out.println("+"+"-".repeat(79)+"+");
        } catch (SQLException e) {
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

//18-id ile tek bir kayıt dönme
    public Student findStudentById(int id) {
        Student student=null;
        getConnection();
        String query="SELECT * FROM t_student WHERE id=?";//+id
        try {
            getPreparedStatement(query);
            prst.setInt(1,id);
            ResultSet rs=prst.executeQuery();
            if (rs.next()){
                student=new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setLastName(rs.getString("lastname"));
                student.setCity(rs.getString("city"));
                student.setAge(rs.getInt("age"));
            }
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
        return student;
    }

    //20-veri güncelleme
    public void update(Student student) {
        getConnection();
        String query="UPDATE t_student SET name=?,lastname=?,city=?,age=? where id=?";
        getPreparedStatement(query);
        try {
            prst.setString(1,student.getName());
            prst.setString(2,student.getLastName());
            prst.setString(3,student.getCity());
            prst.setInt(4,student.getAge());
            prst.setInt(5,student.getId());
            int updated=prst.executeUpdate();
            if (updated>0){
                System.out.println("Öğrenci başarıyla güncellenmiştir.");
            }else System.out.println("Hata oluştu");
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
