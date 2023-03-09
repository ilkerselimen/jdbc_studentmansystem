import java.util.Scanner;

/*
Proje:Student Management System
     -1-Herhangi bir eğitim kurumu için öğrenci yönetim uygulaması geliştiriniz.
     -2-Kullanıcı
               -C:create: öğrenci kayıt
               -R:read: öğrenci veya öğrencileri görüntüleme
               -U:update: id ile öğrenci güncelleme
               -D:delete: id ile öğrenci silme
               -R: ad-soyad ile öğrenci filtreleme
       işlemlerini yapabilmeli.
     -3-öğrenci:id,name,lastname,city,age özelliklerine sahiptir.
 */
public class Runner {
    public static void main(String[] args) {
        start();
    }

    //1-uygulama için menu oluştur
    //students classi olustur
    public static void start(){
        Scanner inp = new Scanner(System.in);
        StudentService service=new StudentService();
        //10-uygulama çalıştırıldığında tablo oluşturulsun
        service.createTable();
        int select;
        do {
            System.out.println("==============================");
            System.out.println("Öğrenci Yönetim Paneli");
            System.out.println("1-Öğrenci Kayıt");
            System.out.println("2-Tüm öğrencileri listele");
            System.out.println("3-Öğrenci güncelle");
            System.out.println("4-Öğrenci sil");
            System.out.println("5-Öğrenci bul");
            System.out.println("6-Ad veya Soyad ile Öğrenci filtrele");
            System.out.println("0-ÇIKIŞ");
            System.out.println("İşlem seçiniz: ");
            select=inp.nextInt();
            inp.nextLine();
            int id;
            switch (select){
                case 1:
                    service.saveStudent();
                    break;
                case 2:
                    service.getAllStudents();
                    break;
                case 3:
                    id=getId(inp);
                    service.updateStudent(id);
                    break;
                case 4:
                    id=getId(inp);
                    service.deleteStudent(id);
                    break;
                case 5:
                    id=getId(inp);
                    Student student=service.getStudentById(id);
                    if (student==null){
                        System.out.println("Öğrenci bulunamadı");
                    }else System.out.println(student);
                    break;
                case 6:
                    service.listStudentsByNameOrLastname();
                    break;
                case 0:
                    System.out.println("İyi günler...");
                    break;
                default:
                    System.out.println("Hatalı giriş!!!");
                break;
            }

        }while (select!=0);
    }

    private static int getId(Scanner inp){
        System.out.println("Öğrenci id: ");
        int id=inp.nextInt();
        inp.nextLine();
        return id;
    }

}
